package com.polimi.mw2016.rest.imageserver.resource;

import java.util.Enumeration;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;

import com.polimi.mw2016.rest.imageserver.Secured;
import com.polimi.mw2016.rest.imageserver.model.AuthzToken;
import com.polimi.mw2016.rest.imageserver.service.TokenService;
import com.polimi.mw2016.rest.imageserver.service.UserService;

//@Secured
@Path("/token")
public class TokenEndpoint {

	@Inject UserService us;
	@Inject TokenService ts;
	
	@Context SecurityContext securityContext;
	
	private final String ACCESS_TOKEN_EXPIRATION = "3600"; //1ora

	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Produces("application/json")
	public Response authorize(@Context HttpServletRequest request) throws OAuthSystemException {
		/*
		System.out.println("AUTHORIZATION SERVER - /token - authScheme" + securityContext.getAuthenticationScheme());
		if(!securityContext.getAuthenticationScheme().equals("Bearer")){
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}		
		*/
		
		//The Default OAuth Authorization Server class that validates whether a given HttpServletRequest is a valid OAuth Token request. 
		OAuthTokenRequest oauthRequest = null; 
		OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());

		System.out.println("AUTHORIZATION SERVER - /token - request parameters:");
		printParameters(request);
		
		AuthzToken authzToken = null;
		
		try {
			oauthRequest = new OAuthTokenRequest(request);   
			
			authzToken = ts.getAuthzToken((oauthRequest.getParam(OAuth.OAUTH_CODE)));
			
			//check expiration
			if(authzToken.isExpired()){
				System.out.println("AUTHORIZATION SERVER - /token - authorization code expired");
				throw new OAuthSystemException("authorization_token expired");
			}
			
			// do checking for different grant types (we implement only the grant_type "authorization_code")
			if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.AUTHORIZATION_CODE.toString())) {
				System.out.println("AUTHORIZATION SERVER - /token -  authorization code: " +oauthRequest.getParam(OAuth.OAUTH_CODE));
				
				//check if clientID/secret is valid
				String clientId = String.valueOf(authzToken.getClient().getIdClient());
				String clientSecret = authzToken.getClient().getSecret();
				if(!clientId.equals(oauthRequest.getParam(OAuth.OAUTH_CLIENT_ID)) || 
						!clientSecret.equals(oauthRequest.getParam(OAuth.OAUTH_CLIENT_SECRET))) {
					OAuthResponse response = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
				            .setError(OAuthError.TokenResponse.INVALID_CLIENT).setErrorDescription("client_id/secret not valid")
				            .buildJSONMessage();
				    return Response.status(response.getResponseStatus()).entity(response.getBody()).build();
				}
				
			}
			
		} catch (OAuthProblemException e) {
			OAuthResponse res = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST).error(e)
					.buildJSONMessage();
			return Response.status(res.getResponseStatus()).entity(res.getBody()).build();
		} catch (Exception e) {
			 OAuthResponse response = OAuthASResponse
			            .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
			            .setError(OAuthError.TokenResponse.INVALID_GRANT)
			            .setErrorDescription("invalid authorization code")
			            .buildJSONMessage();
			        return Response.status(response.getResponseStatus()).entity(response.getBody()).build();
		}
		
		//if client ok generate token		
		String accessToken = oauthIssuerImpl.accessToken();
		OAuthResponse response = OAuthASResponse
		    .tokenResponse(HttpServletResponse.SC_OK)
		    .setAccessToken(accessToken)
		    .setExpiresIn(ACCESS_TOKEN_EXPIRATION)
		    .buildJSONMessage();
		System.out.println("TOKEN accessToken: " + accessToken);

		//access t
		ts.addAccessToken(accessToken, Integer.valueOf(ACCESS_TOKEN_EXPIRATION), authzToken);

		return Response.status(response.getResponseStatus()).entity(response.getBody()).build();
		
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
			System.out.println("> " + paramName + ": " + sysoutValues);
		}
		System.out.println("> ");
	}
	
	/*
	@GET
	@Consumes("application/x-www-form-urlencoded")
	@Produces("application/json")
	public Response authorizeGet(@Context HttpServletRequest request) throws OAuthSystemException {

		OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());

		OAuthResponse response = OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK)
				.setAccessToken(oauthIssuerImpl.accessToken()).setExpiresIn("3600").buildJSONMessage();

		System.out.println("TOKEN @GET generatedToken: " + oauthIssuerImpl.accessToken());

		return Response.status(response.getResponseStatus()).entity(response.getBody()).build();
	}
	 */
}
