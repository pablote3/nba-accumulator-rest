package com.rossotti.basketball.app.gateway;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.rossotti.basketball.dao.model.Game;

public class Startup {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {
					"classpath:si-config.xml",
					"classpath:applicationContext.xml"
		});
		System.out.println("begin findGamesActivator");
		GameGateway gameGateway = (GameGateway) context.getBean("gameGateway");
		Game game = gameGateway.findGames("2015-01-07");
		System.out.println("end findGamesActivator " + game.getBoxScoreHome().getTeam().getTeamKey());
		context.close();
	}
}