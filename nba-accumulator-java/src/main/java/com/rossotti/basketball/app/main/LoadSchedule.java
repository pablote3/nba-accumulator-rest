package com.rossotti.basketball.app.main;

import com.rossotti.basketball.app.service.GameService;
import com.rossotti.basketball.app.service.PropertyService;
import com.rossotti.basketball.app.service.TeamService;
import com.rossotti.basketball.dao.model.BoxScore;
import com.rossotti.basketball.dao.model.Game;
import com.rossotti.basketball.dao.model.GameStatus;
import com.rossotti.basketball.dao.model.Team;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringTokenizer;

class LoadSchedule {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		TeamService teamService = (TeamService) context.getBean("teamService");
		GameService gameService = (GameService) context.getBean("gameService");
		PropertyService propertyService = (PropertyService) context.getBean("propertyService");

		Path path =  Paths.get(propertyService.getProperty_Path("loader.fileSchedule")).resolve(System.getProperty("fileName"));
		File file = path.toFile();

		BufferedReader bufRdr = null;
		try {
			bufRdr = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line;
		Game game;
		LocalDate gameLocalDate;
		LocalDateTime gameLocalDateTime;
		DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("MM/dd/yyyy");
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm");
		BoxScore boxScoreHome;
		BoxScore boxScoreAway;
		Team teamHome;
		Team teamAway;
		int i = 0;

		//read each line of text file
		try {
			assert bufRdr != null;
			bufRdr.readLine();								                //jump over header line
			while((line = bufRdr.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, ",");
				String gameDate = st.nextToken();                           //start date
				String gameDateTime = gameDate + " " + st.nextToken();      //start time (ET)
				String awayTeam = st.nextToken().trim();
				String homeTeam = st.nextToken().trim();

				gameLocalDate = new LocalDate().parse(gameDate, dateFormatter);
				gameLocalDateTime = new LocalDateTime().parse(gameDateTime, dateTimeFormatter);

				teamHome = teamService.findTeamByLastName(homeTeam, gameLocalDate);
				teamAway = teamService.findTeamByLastName(awayTeam, gameLocalDate);

				game = new Game();
				game.setGameDateTime(gameLocalDateTime);
				game.setStatus(GameStatus.Scheduled);
				game.setSeasonType(Game.SeasonType.Regular);

				boxScoreAway = new BoxScore();
				boxScoreAway.setLocation(BoxScore.Location.Away);
				boxScoreAway.setTeam(teamAway);
				game.addBoxScore(boxScoreAway);

				boxScoreHome = new BoxScore();
				boxScoreHome.setLocation(BoxScore.Location.Home);
				boxScoreHome.setTeam(teamHome);
				game.addBoxScore(boxScoreHome);

				gameService.createGame(game);
				System.out.println("i = " + i++ + " " + teamAway.getFullName() + " at " + teamHome.getFullName());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		//close the file
		try {
			bufRdr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}