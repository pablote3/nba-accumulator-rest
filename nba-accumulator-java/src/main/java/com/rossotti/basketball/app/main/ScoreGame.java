package com.rossotti.basketball.app.main;

import com.rossotti.basketball.app.integration.GatewayService;
import com.rossotti.basketball.app.integration.ServiceProperties;
import com.rossotti.basketball.dao.model.AppStandings;
import com.rossotti.basketball.util.DateTimeUtil;
import org.joda.time.LocalDate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

class ScoreGame {
	public static void main(String[] args) {
		System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");
		System.setProperty(org.slf4j.impl.SimpleLogger.SHOW_DATE_TIME_KEY, "TRUE");
		System.setProperty(org.slf4j.impl.SimpleLogger.DATE_TIME_FORMAT_KEY, "yyyy-MM-dd HH:mm:ss");
		ApplicationContext context = new ClassPathXmlApplicationContext("si-config.xml", "applicationContext.xml");
		ServiceProperties properties = new ServiceProperties();

		if (System.getProperty("gameDate") != null) {
			String gameDate = System.getProperty("gameDate");
			if (gameDate.isEmpty()) {
				properties.setGameDate(DateTimeUtil.getStringDate(new LocalDate().minusDays(1)));
			}
			else {
				if (DateTimeUtil.isDate(gameDate)) {
					properties.setGameDate(gameDate);
				}
				else {
					System.out.println("Invalid gameDate argument");
					System.exit(1);
				}
			}
		}
		else {
			System.out.println("Need to supply gameDate argument");
			System.exit(1);
		}

		if (System.getProperty("gameTeam") != null) {
			String gameTeam =  System.getProperty("gameTeam");
			if (gameTeam.isEmpty()) {
				properties.setGameTeam("");
			}
			else {
				properties.setGameTeam(gameTeam);
			}
		}
		else {
			System.out.println("Need to supply gameTeam argument");
			System.exit(1);
		}

		if (properties.getGameTeam().isEmpty()) {
			System.out.println("\n" + "begin gatewayService for gameDate = " + properties.getGameDate());
		}
		else {
			System.out.println("\n" + "begin gatewayService for gameDate = " + properties.getGameDate() + " and gameTeam = " + properties.getGameTeam());
		}

		GatewayService gatewayService = (GatewayService) context.getBean("gatewayService");
		AppStandings appStandings = gatewayService.processGames(properties);
		((ConfigurableApplicationContext)context).close();
		if (appStandings != null && appStandings.getStandings() != null) {
			System.out.println("end gatewayService, processed " + appStandings.getStandings().size() + " standings" + "\n");
		}
		else {
			System.out.println("end gatewayService, no games processed");
		}
	}
}