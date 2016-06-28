package com.rossotti.basketball.app.service;

import java.util.List;

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
import com.rossotti.basketball.dao.exception.NoSuchEntityException;
import com.rossotti.basketball.dao.model.BoxScore;
import com.rossotti.basketball.dao.model.Game;
import com.rossotti.basketball.dao.model.GameStatus;
import com.rossotti.basketball.dao.model.Official;
import com.rossotti.basketball.dao.model.RosterPlayer;
import com.rossotti.basketball.dao.model.BoxScore.Result;
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

	@Autowired
	private OfficialService officialService;

	@Autowired
	private RosterPlayerService rosterPlayerService;

	@Autowired
	private TeamService teamService;

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
		try {
			BoxScore awayBoxScore = game.getBoxScoreAway();
			BoxScore homeBoxScore = game.getBoxScoreHome();
			String awayTeamKey = awayBoxScore.getTeam().getTeamKey();
			String homeTeamKey = homeBoxScore.getTeam().getTeamKey();
			LocalDateTime gameDateTime = game.getGameDateTime();
			LocalDate gameDate = DateTimeUtil.getLocalDate(gameDateTime);

			String event = DateTimeUtil.getStringDateNaked(gameDateTime) + "-" + awayTeamKey + "-at-" + homeTeamKey;

			if (game.isScheduled()) {
				logger.info('\n' + "Scheduled game ready to be scored: " + event);

				GameDTO gameDTO = null;
				ClientSource clientSource = propertyService.getProperty_ClientSource("accumulator.source.boxScore");
				if (clientSource == ClientSource.File) {
					gameDTO = fileClientService.retrieveBoxScore(event);
				}
				else if (clientSource == ClientSource.Api) {
					gameDTO = restClientService.retrieveBoxScore(event);
				}
				if (gameDTO.isFound()) {
					awayBoxScore.updateTotals(gameDTO.away_totals);
					homeBoxScore.updateTotals(gameDTO.home_totals);
					awayBoxScore.updatePeriodScores(gameDTO.away_period_scores);
					homeBoxScore.updatePeriodScores(gameDTO.home_period_scores);
					awayBoxScore.setBoxScorePlayers(rosterPlayerService.getBoxScorePlayers(gameDTO.away_stats, gameDate, awayTeamKey));
					homeBoxScore.setBoxScorePlayers(rosterPlayerService.getBoxScorePlayers(gameDTO.home_stats, gameDate, homeTeamKey));
					game.setGameOfficials(officialService.getGameOfficials(gameDTO.officials, gameDate));
					awayBoxScore.setTeam(teamService.findTeam(awayTeamKey, gameDate));
					homeBoxScore.setTeam(teamService.findTeam(homeTeamKey, gameDate));

					if (gameDTO.away_totals.getPoints() > gameDTO.home_totals.getPoints()) {
						awayBoxScore.setResult(Result.Win);
						homeBoxScore.setResult(Result.Loss);
					}
					else {
						awayBoxScore.setResult(Result.Loss);
						homeBoxScore.setResult(Result.Win);
					}

					awayBoxScore.setDaysOff((short)DateTimeUtil.getDaysBetweenTwoDateTimes(findPreviousGameDateTime(gameDate, awayTeamKey), gameDateTime));
					homeBoxScore.setDaysOff((short)DateTimeUtil.getDaysBetweenTwoDateTimes(findPreviousGameDateTime(gameDate, homeTeamKey), gameDateTime));
					game.setStatus(GameStatus.Completed);
					Game updatedGame = updateGame(game);
					if (updatedGame.isUpdated()) {
						logger.info("Game Scored " + awayTeamKey +  " " + awayBoxScore.getPoints() + " " + homeTeamKey +  " " + homeBoxScore.getPoints());
					}
					else if (updatedGame.isNotFound()) {
						logger.info("Unable to find game for update - " + updatedGame.getStatus());
						game.setStatus(GameStatus.ServerError);
					}
				}
				else if (gameDTO.isNotFound()) {
					logger.info('\n' + "" + " unable to find game");
					game.setStatus(GameStatus.ClientError);
				}
			}
			else {
				logger.info('\n' + "" + game.getStatus() + " game not eligible to be scored: " + event.toString());
				game.setStatus(GameStatus.ServerError);
			}
		}
		catch (NoSuchEntityException nse) {
			if (nse.getEntityClass().equals(Official.class)) {
				logger.info("Official not found - need to add official");
			}
			else if (nse.getEntityClass().equals(RosterPlayer.class)) {
				logger.info("Roster Player not found - need to rebuild active roster");
			}
			game.setStatus(GameStatus.ClientError);
		}
		catch (Exception e) {
			logger.info("unexpected exception = " + e);
			game.setStatus(GameStatus.ServerError);
		}
		return game;
	}
}