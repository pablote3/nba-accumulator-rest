package com.rossotti.basketball.app.service;

import java.util.List;

import javax.ws.rs.core.Response;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.app.resource.ClientSource;
import com.rossotti.basketball.client.dto.GameDTO;
import com.rossotti.basketball.client.service.FileClientService;
import com.rossotti.basketball.client.service.RestClientService;
import com.rossotti.basketball.dao.model.BoxScore;
import com.rossotti.basketball.dao.model.Game;
import com.rossotti.basketball.dao.model.GameStatus;
import com.rossotti.basketball.dao.model.BoxScore.Result;
import com.rossotti.basketball.dao.pub.PubGame;
import com.rossotti.basketball.dao.repository.GameRepository;
import com.rossotti.basketball.util.DateTimeUtil;

@Service
public class GameService {
	@Autowired
	private GameRepository gameRepo;

	@Autowired
	private PropertyService propertyService;

	@Autowired
	private RestClientService restClientService;

	@Autowired
	private FileClientService fileClientService;
	
	private final Logger logger = LoggerFactory.getLogger(OfficialService.class);

	public List<Game> findByDate(LocalDate gameDate) {
		return gameRepo.findByDate(gameDate);
	}

	public Game findByDateTeam(LocalDate gameDate, String teamKey) {
		return gameRepo.findByDateTeam(gameDate, teamKey);
	}

	public LocalDateTime findPreviousGameDateTime(LocalDate gameDate, String teamKey) {
		return gameRepo.findPreviousGameDateTimeByDateTeam(gameDate, teamKey);
	}

	public List<Game> findByDateTeamSeason(LocalDate gameDate, String teamKey) {
		return gameRepo.findByDateTeamSeason(gameDate, teamKey);
	}

	public Game updateGame(Game game) {
		return gameRepo.updateGame(game);
	}

	public Game scoreGame(Game game) {
		BoxScore awayBoxScore = game.getBoxScoreAway();
		BoxScore homeBoxScore = game.getBoxScoreHome();
		String awayTeamKey = awayBoxScore.getTeam().getTeamKey();
		String homeTeamKey = homeBoxScore.getTeam().getTeamKey();
		LocalDateTime gameDateTime = game.getGameDateTime();
		LocalDate gameDate = DateTimeUtil.getLocalDate(gameDateTime);
		
		String event = DateTimeUtil.getStringDateNaked(gameDateTime) + "-" + awayTeamKey + "-at-" + homeTeamKey;

		logger.info('\n' + "Scheduled game ready to be scored: " + event);

//		GameDTO gameDTO = null;
//		ClientSource clientSource = propertyService.getProperty_ClientSource("accumulator.source.boxScore");
//		if (clientSource == ClientSource.File) {
//			gameDTO = fileClientService.retrieveBoxScore(event);
//		}
//		else if (clientSource == ClientSource.Api) {
//			gameDTO = restClientService.retrieveBoxScore(event);
//		}
//		if (gameDTO.httpStatus == 200) {
//			game.setStatus(GameStatus.Completed);
//			awayBoxScore.updateTotals(gameDTO.away_totals);
//			homeBoxScore.updateTotals(gameDTO.home_totals);
//			awayBoxScore.updatePeriodScores(gameDTO.away_period_scores);
//			homeBoxScore.updatePeriodScores(gameDTO.home_period_scores);
//			awayBoxScore.setBoxScorePlayers(rosterPlayerService.getBoxScorePlayers(gameDTO.away_stats, gameDate, awayTeamKey));
//			homeBoxScore.setBoxScorePlayers(rosterPlayerService.getBoxScorePlayers(gameDTO.home_stats, gameDate, homeTeamKey));
//			game.setGameOfficials(officialService.getGameOfficials(gameDTO.officials, gameDate));
//			awayBoxScore.setTeam(teamService.findTeam(awayTeamKey, gameDate));
//			homeBoxScore.setTeam(teamService.findTeam(homeTeamKey, gameDate));
//
//			if (gameDTO.away_totals.getPoints() > gameDTO.home_totals.getPoints()) {
//				awayBoxScore.setResult(Result.Win);
//				homeBoxScore.setResult(Result.Loss);
//			}
//			else {
//				awayBoxScore.setResult(Result.Loss);
//				homeBoxScore.setResult(Result.Win);
//			}
//
//			awayBoxScore.setDaysOff((short)DateTimeUtil.getDaysBetweenTwoDateTimes(gameService.findPreviousGameDateTime(gameDate, awayTeamKey), gameDateTime));
//			homeBoxScore.setDaysOff((short)DateTimeUtil.getDaysBetweenTwoDateTimes(gameService.findPreviousGameDateTime(gameDate, homeTeamKey), gameDateTime));
//
//			Game updatedGame = gameService.updateGame(game);
//			if (updatedGame.isUpdated()) {
//				logger.info("Game Scored " + awayTeamKey +  " " + awayBoxScore.getPoints() + " " + homeTeamKey +  " " + homeBoxScore.getPoints());
//				PubGame pubGame = game.toPubGame(uriInfo, homeTeamKey);
//				return Response.ok(pubGame)
//					.link(uriInfo.getAbsolutePath(), "game")
//					.build();
//			}
//			else {
//				logger.info("Unable to update game - " + updatedGame.getStatus());
//				return Response.status(404).build();
//			}
//		}
		return game;
	}
}