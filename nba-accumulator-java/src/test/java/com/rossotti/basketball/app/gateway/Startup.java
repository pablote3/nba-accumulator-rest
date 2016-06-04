package com.rossotti.basketball.app.gateway;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Startup {

	@SuppressWarnings({ "resource", "unused" })
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:si-config.xml");
		while (true) {}
	}
}