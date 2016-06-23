package com.polimi.mw2016.rest.imageserver;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.StringTokenizer;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.internal.util.Base64;

import com.polimi.mw2016.rest.imageserver.model.AccessToken;
import com.polimi.mw2016.rest.imageserver.model.AuthzToken;
import com.polimi.mw2016.rest.imageserver.model.User;
import com.polimi.mw2016.rest.imageserver.service.UserService;

@Secured 
@Provider
@ApplicationScoped
@Priority(Priorities.AUTHENTICATION) // specify the execution order (in case we have more than one filter)
public class AuthenticationFilter implements ContainerRequestFilter {
	
	private EntityManager em;
	
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
    	System.out.println("FILTER in");
    	em = Persistence.createEntityManagerFactory("imageserver_pu").createEntityManager();   	
    	
        // Get the HTTP Authorization header from the request
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        System.out.println("FILTER AuthorizationHeader: " + authorizationHeader);
        
        // Authorization header MUST be either equal to "Bearer my_token" or "Basic my_base64_credentials"
        if(authorizationHeader != null && authorizationHeader.startsWith("Basic ")){
        	
        	// Extract the user and the password from the HTTP Authorization header
        	String encodedCredentials = authorizationHeader.substring("Basic".length()).trim();
        	String decodedCredentials = Base64.decodeAsString(encodedCredentials);
        	StringTokenizer tokenizer = new StringTokenizer(decodedCredentials, ":");
			String username = tokenizer.nextToken();
			String password = tokenizer.nextToken();
			password = UserService.encryptPassword(password);
			System.out.println("FILTER user: " + username + " password: " + password);
			
			// Validate the user
			try{
				validateUser(username, password, requestContext);
			} catch (Exception e) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            }
        	
        } else if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
        	
        	// Extract the token from the HTTP Authorization Header
            String token = authorizationHeader.substring("Bearer".length()).trim();
            System.out.println("FILTER token: " + token);
        	
            // Validate the token
            try {
                validateToken(token, requestContext);
            } catch (Exception e) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            }
            
        } else {
            throw new NotAuthorizedException("Authorization Header must be provided");
        }
        System.out.println("FILTER out");
    }

    private void validateToken(String token, ContainerRequestContext requestContext) throws Exception {	
    	/*
    	List<PathSegment> listPs = requestContext.getUriInfo().getPathSegments();

    	if(!listPs.isEmpty() && listPs.get(listPs.size()-1).getPath().equals("token")){
    		
    		List<AuthzToken> queryResult = em.createNamedQuery("Authztoken.findByAuthzToken", AuthzToken.class)
					.setParameter("authzToken", token)
					.getResultList();
			if (!queryResult.isEmpty()){
				//check expiration
				AuthzToken authzToken = queryResult.get(0);
				if(!authzToken.isExpired()){
					//set sec context
					requestContext.setSecurityContext(setNewSecurityContext(authzToken.getUser(), "Bearer"));
				}else {
					System.out.println("FILTER expired token");
					throw new Exception();
				}			
			}else {
				System.out.println("FILTER token not valid");
				throw new Exception();
			}
    	} else if(!listPs.isEmpty()){*/
    		
	        List<AccessToken> queryResult = em.createNamedQuery("Accesstoken.findByAccessToken", AccessToken.class)
					.setParameter("accessToken", token)
					.getResultList();
			if (!queryResult.isEmpty()){
				//check expiration
				AccessToken accessToken = queryResult.get(0);
				if(!accessToken.isExpired()){
					//set sec context
					requestContext.setSecurityContext(setNewSecurityContext(accessToken.getUser(), "Bearer"));
				}else {
					System.out.println("FILTER validateToken() token expired.");
					throw new Exception();
				}			
			}else {
				System.out.println("FILTER validateToken() token not found.");
				throw new Exception();
			}
    	/*}else{
    		System.out.println("FILTER validateToken() endpoint not valid.");
			throw new Exception();
    	}*/
    }
    
    private void validateUser(String username, String password, ContainerRequestContext requestContext)throws Exception {
    	//check username
		List<User> queryResult = em.createNamedQuery("User.findByUsername", User.class)
				.setParameter("username", username)
				.getResultList();
		if (!queryResult.isEmpty()){
			//user found, check password
			User user  = queryResult.get(0);
			if(user.getPassword().equals(password)){
				// login OK -> set security context
				requestContext.setSecurityContext(setNewSecurityContext(user, "Basic"));
			}else {
				System.out.println("FILTER validateUser() password mismatch");
				throw new Exception();
			}
		} else {
			System.out.println("FILTER validateUser() username not found");
			throw new Exception();
		}
    }

    private SecurityContext setNewSecurityContext(final User u, final String authScheme){		
    	return new SecurityContext() {			    		
			@Override
			public boolean isUserInRole(String role) {
				return u.getUsergroup().equals(role);
			}
			
			@Override
			public boolean isSecure() {
				return true; //true because the entire system runs under https protocol.
			}
			
			@Override
			public Principal getUserPrincipal() {
				return new Principal() {
					
					@Override
					public String getName() {
						return u.getUsername();
					}
				};
			}
			
			@Override
			public String getAuthenticationScheme() {
				return authScheme;
			}
		};
    }
}
