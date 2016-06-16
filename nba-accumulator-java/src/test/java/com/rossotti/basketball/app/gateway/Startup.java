package com.rossotti.basketball.app.gateway;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Startup {
	@SuppressWarnings({ "resource", "unused" })
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {
					"classpath:si-config.xml",
					"classpath:applicationContext.xml"
		});
	}
}