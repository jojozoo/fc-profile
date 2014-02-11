package com.orientalcomics.test.controllers;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Path;
import net.paoding.rose.web.annotation.rest.Get;


@Path("/")
public class TestController {

	@Get("hello")
	public String hello(Invocation inv){
		return "@hello";
	}
}
