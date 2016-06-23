package com.polimi.mw2016.rest.imageserver.resource;

import java.util.Enumeration;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;


@Path("printParam")
public class QueryParamPrinterEndpoint {

	@GET
	public JsonObject printParam(@Context HttpServletRequest request){
		return getParameters(request);
	}
	
	private JsonObject getParameters(HttpServletRequest request) {
		
		JsonObjectBuilder job = Json.createObjectBuilder();
		
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
        	String paramName = parameterNames.nextElement();
        	String[] paramValues = request.getParameterValues(paramName);
        	String sysoutValues = "";
			for (int i = 0; i < paramValues.length; i++) {
				String paramValue = paramValues[i];
				sysoutValues = sysoutValues + paramValue;
			}
			job.add(paramName, sysoutValues);
        }
		return job.build();
	}
}
