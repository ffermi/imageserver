package com.polimi.mw2016.rest.imageserver.resource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;

import com.polimi.mw2016.rest.imageserver.service.TokenService;

//secured through web.xml
@Path("/authz")
public class AuthzEndpoint {
	
	@Inject TokenService ts;	
	
	private final int AUTHZ_TOKEN_EXPIRATION = 600;
	
    @GET
    public Response authorization(@Context HttpServletRequest request) throws URISyntaxException, OAuthSystemException {
    	
    	//OAuthAuthzRequest extends OAuthRequest (the Abstract OAuth request for the Authorization server).
        OAuthAuthzRequest oauthRequest = null;
        OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator()); // instantiate the token generator object
        
        //print the headers of the request
        System.out.println("AUTHORIZATION SERVER - /authz - request parameters:");
        printParameters(request);
        
        try {
        	// Instantiate a new OAuth request for the Authorization server
            oauthRequest = new OAuthAuthzRequest(request); 

            // Build response according to response_type
            String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);

            
            OAuthASResponse.OAuthAuthorizationResponseBuilder builder = 
            		OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_FOUND); 
            		// The HTTP response status code 302 Found is a common way of performing URL redirection.
            
            //check whether the response_type is equal to "code" or "token" (we implement only "code").
            if (responseType.equals(ResponseType.CODE.toString())) {
            	String authorizationCode = oauthIssuerImpl.authorizationCode(); // generate the authorization code
                builder.setCode(authorizationCode); // set the code as a parameter in the response
                builder.setExpiresIn(String.valueOf(AUTHZ_TOKEN_EXPIRATION)); // set the expiration as a parameter in the response
                persistAccessToken(authorizationCode, request, oauthRequest); //insert the authorizationCode into the database 
                System.out.println("AUTHORIZATION SERVER - /authz - authorization_code:" + authorizationCode);
            }
            
            String redirectURI = oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI); // get the redirect_uri
            final OAuthResponse response = builder.location(redirectURI).buildQueryMessage(); //create the oauth response (query message)
            URI url = new URI(response.getLocationUri());            
            System.out.println("AUTHORIZATION SERVER - /authz - redirectURL: " + url.toString());
            return Response.status(response.getResponseStatus()).location(url).build();
        } catch (OAuthProblemException e) {
            final Response.ResponseBuilder responseBuilder = Response.status(HttpServletResponse.SC_FOUND);
            String redirectUri = e.getRedirectUri();
            
            if (OAuthUtils.isEmpty(redirectUri)) {
                throw new WebApplicationException(
                		responseBuilder.entity("OAuth callback url needs to be provided by client!!!")
                		.build()
                		);
            }
            
            final OAuthResponse response = OAuthASResponse
            		.errorResponse(HttpServletResponse.SC_FOUND)
            		.error(e)
            		.location(redirectUri)
            		.buildQueryMessage();
            
            final URI location = new URI(response.getLocationUri());
            
            return responseBuilder.location(location).build();    
		} catch (Exception ex) {
			final Response.ResponseBuilder responseBuilder = Response.status(HttpServletResponse.SC_BAD_REQUEST);
			responseBuilder.entity("client_id NOT valid.").build();            
            return responseBuilder.build();
		}
    }
 
    private void persistAccessToken(String authzToken,HttpServletRequest req, OAuthAuthzRequest oauthReq) throws Exception{
    	String idClient = oauthReq.getParam(OAuth.OAUTH_CLIENT_ID);
    	String resourceOwner = req.getUserPrincipal().getName();
    	 ts.addAuthzToken(authzToken, AUTHZ_TOKEN_EXPIRATION, idClient, resourceOwner);
    }
    
	private void printParameters(HttpServletRequest request) {
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
        	String paramName = parameterNames.nextElement();
        	String[] paramValues = request.getParameterValues(paramName);
        	String sysoutValues = "";
			for (int i = 0; i < paramValues.length; i++) {
				String paramValue = paramValues[i];
				sysoutValues = sysoutValues + paramValue;
			}
			System.out.println("> " + paramName +": "+ sysoutValues);
        }
        System.out.println("> ");
	}

}
