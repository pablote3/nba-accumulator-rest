package com.rossotti.basketball.app.service;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.client.dto.OfficialDTO;
import com.rossotti.basketball.dao.exception.NoSuchEntityException;
import com.rossotti.basketball.dao.model.GameOfficial;
import com.rossotti.basketball.dao.model.Official;
import com.rossotti.basketball.dao.repository.OfficialRepository;

@Service
public class OfficialService {
	@Autowired
	private OfficialRepository officialRepo;

	private final Logger logger = LoggerFactory.getLogger(OfficialService.class);

	public List<GameOfficial> getGameOfficials(OfficialDTO[] officials, LocalDate gameDate) {
		List<GameOfficial> gameOfficials = new ArrayList<GameOfficial>();
		GameOfficial gameOfficial;
		Official official;
		for (int i = 0; i < officials.length; i++) {
			String lastName = officials[i].getLast_name();
			String firstName = officials[i].getFirst_name();
			official = officialRepo.findOfficial(lastName, firstName, gameDate);
			if (official.isNotFound()) {
				logger.info("Official not found " + firstName + " " + lastName);
				throw new NoSuchEntityException(Official.class);
			}
			else {
				gameOfficial = new GameOfficial();
				gameOfficial.setOfficial(official);
				gameOfficials.add(gameOfficial);
			}
		}
		return gameOfficials;
	}
}