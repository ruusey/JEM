package com.lawnbuzz.rest;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import javax.ws.rs.NameBinding;
import java.lang.annotation.RetentionPolicy;
@NameBinding
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Secured { }
