package com.polimi.mw2016.rest.imageserver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.ws.rs.NameBinding;

/*
 * The defined name-binding annotation @Secured will be used to decorate a filter class, 
 * which implements ContainerRequestFilter, allowing you to handle the request. 
 */

@NameBinding // @NameBinding, a meta-annotation used to create name-binding annotations for filters and interceptors.
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD })
public @interface Secured { 
	
}
