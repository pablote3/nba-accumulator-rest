package com.rossotti.basketball.dao.repository;

import java.util.List;

import org.hibernate.PropertyValueException;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rossotti.basketball.dao.model.Player;
import com.rossotti.basketball.dao.model.RosterPlayer;
import com.rossotti.basketball.dao.model.Team;
import com.rossotti.basketball.dao.repository.RosterPlayerRepository;
import com.rossotti.basketball.dao.model.Position;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class RosterPlayerRepositoryTest {

	@Autowired
	private RosterPlayerRepository rosterPlayerRepo;

	@Test
	public void findRosterPlayerByNameBirthdateAsOfDate_Found() {
		RosterPlayer findRosterPlayer = rosterPlayerRepo.findRosterPlayer("Puzdrakiew'icz", "Luke", new LocalDate("2002-02-20"), new LocalDate("2009-10-30"));
		Assert.assertEquals("Luke Puzdrakiew'icz", findRosterPlayer.getPlayer().getDisplayName());
		Assert.assertEquals(Position.PG, findRosterPlayer.getPosition());
		Assert.assertTrue(findRosterPlayer.isFound());
	}

	@Test
	public void findRosterPlayerByNameBirthdateAsOfDate_NotFound_LastName() {
		RosterPlayer findRosterPlayer = rosterPlayerRepo.findRosterPlayer("Puzdrakiew'ic", "Luke", new LocalDate("2002-02-20"), new LocalDate("2009-10-30"));
		Assert.assertTrue(findRosterPlayer.isNotFound());
	}

	@Test
	public void findRosterPlayerByNameBirthdateAsOfDate_NotFound_FirstName() {
		RosterPlayer findRosterPlayer = rosterPlayerRepo.findRosterPlayer("Puzdrakiew'icz", "Luk", new LocalDate("2002-02-20"), new LocalDate("2009-10-30"));
		Assert.assertTrue(findRosterPlayer.isNotFound());
	}

	@Test
	public void findRosterPlayerByNameBirthdateAsOfDate_NotFound_Birthdate() {
		RosterPlayer findRosterPlayer = rosterPlayerRepo.findRosterPlayer("Puzdrakiew'icz", "Luke", new LocalDate("2002-02-21"), new LocalDate("2009-10-30"));
		Assert.assertTrue(findRosterPlayer.isNotFound());
	}

	@Test
	public void findRosterPlayerByNameBirthdateAsOfDate_NotFound_FromDate() {
		RosterPlayer findRosterPlayer = rosterPlayerRepo.findRosterPlayer("Puzdrakiew'icz", "Luke", new LocalDate("2002-02-20"), new LocalDate("2009-10-29"));
		Assert.assertTrue(findRosterPlayer.isNotFound());
	}

	@Test
	public void findRosterPlayerByNameBirthdateAsOfDate_NotFound_ToDate() {
		RosterPlayer findRosterPlayer = rosterPlayerRepo.findRosterPlayer("Puzdrakiew'icz", "Luke", new LocalDate("2002-02-20"), new LocalDate("2009-11-04"));
		Assert.assertTrue(findRosterPlayer.isNotFound());
	}

	@Test
	public void findRosterPlayerByNameTeamAsOfDate_Found() {
		RosterPlayer findRosterPlayer = rosterPlayerRepo.findRosterPlayer("Puzdrakiew'icz", "Luke", "salinas-cowboys", new LocalDate("2009-10-30"));
		Assert.assertEquals("Luke Puzdrakiew'icz", findRosterPlayer.getPlayer().getDisplayName());
		Assert.assertEquals(Position.PG, findRosterPlayer.getPosition());
		Assert.assertEquals("SAL", findRosterPlayer.getTeam().getAbbr());
		Assert.assertTrue(findRosterPlayer.isFound());
	}

	@Test
	public void findRosterPlayerByNameTeamAsOfDate_NotFound_LastName() {
		RosterPlayer findRosterPlayer = rosterPlayerRepo.findRosterPlayer("Puzdrakiew'ic", "Luke", "chicago-zephyrs", new LocalDate("2009-10-30"));
		Assert.assertTrue(findRosterPlayer.isNotFound());
	}

	@Test
	public void findRosterPlayerByNameTeamAsOfDate_NotFound_FirstName() {
		RosterPlayer findRosterPlayer = rosterPlayerRepo.findRosterPlayer("Puzdrakiew'icz", "Luk", "chicago-zephyrs", new LocalDate("2009-10-30"));
		Assert.assertTrue(findRosterPlayer.isNotFound());
	}

	@Test
	public void findRosterPlayerByNameTeamAsOfDate_NotFound_TeamKey() {
		RosterPlayer findRosterPlayer = rosterPlayerRepo.findRosterPlayer("Puzdrakiew'icz", "Luke", "chicago-zephers", new LocalDate("2009-10-30"));
		Assert.assertTrue(findRosterPlayer.isNotFound());
	}
	
	@Test
	public void findRosterPlayerByNameTeamAsOfDate_NotFound_FromDate() {
		RosterPlayer findRosterPlayer = rosterPlayerRepo.findRosterPlayer("Puzdrakiew'icz", "Luke", "chicago-zephyrs", new LocalDate("2009-10-29"));
		Assert.assertTrue(findRosterPlayer.isNotFound());
	}

	@Test
	public void findRosterPlayerByNameTeamAsOfDate_NotFound_ToDate() {
		RosterPlayer findRosterPlayer = rosterPlayerRepo.findRosterPlayer("Puzdrakiew'icz", "Luke", "chicago-zephyrs", new LocalDate("2009-11-04"));
		Assert.assertTrue(findRosterPlayer.isNotFound());
	}

	@Test
	public void findRosterPlayersByPlayer_Found() {
		List<RosterPlayer> findRosterPlayers = rosterPlayerRepo.findRosterPlayers("Puzdrakiew'icz", "Luke", new LocalDate("2002-02-20"));
		Assert.assertEquals(2, findRosterPlayers.size());
	}

	@Test
	public void findRosterPlayersByPlayer_NotFound() {
		List<RosterPlayer> findRosterPlayers = rosterPlayerRepo.findRosterPlayers("Puzdrakiew'icz", "Luke", new LocalDate("2002-02-21"));
		Assert.assertEquals(0, findRosterPlayers.size());
	}

	@Test
	public void findRosterPlayersByTeam_Found() {
		List<RosterPlayer> findRosterPlayers = rosterPlayerRepo.findRosterPlayers("salinas-cowboys", new LocalDate("2009-10-30"));
		Assert.assertEquals(2, findRosterPlayers.size());
	}

	@Test
	public void findRosterPlayersByTeam_NotFound() {
		List<RosterPlayer> findRosterPlayers = rosterPlayerRepo.findRosterPlayers("chicago-zephyrs", new LocalDate("2009-10-29"));
		Assert.assertEquals(0, findRosterPlayers.size());
	}

	@Test
	public void createRosterPlayer_Created() {
		RosterPlayer createRosterPlayer = rosterPlayerRepo.createRosterPlayer(getMockRosterPlayer(Position.C, new LocalDate("2009-11-05"), new LocalDate("2009-11-30")));
		RosterPlayer findRosterPlayer = rosterPlayerRepo.findRosterPlayer("Puzdrakiewicz", "Thad", new LocalDate("1966-06-02"), new LocalDate("2009-11-05"));
		Assert.assertTrue(createRosterPlayer.isCreated());
		Assert.assertEquals("Puzdrakiewicz", findRosterPlayer.getPlayer().getLastName());
		Assert.assertEquals("Harlem Globetrotter's", findRosterPlayer.getTeam().getFullName());
		Assert.assertEquals(Position.C, findRosterPlayer.getPosition());
	}

	@Test
	public void createRosterPlayer_Duplicate() {
		RosterPlayer createRosterPlayer = rosterPlayerRepo.createRosterPlayer(getMockRosterPlayer(Position.C,  new LocalDate("2009-10-30"), new LocalDate("2009-11-04")));
		Assert.assertTrue(createRosterPlayer.isFound());
	}

	@Test(expected=PropertyValueException.class)
	public void createRosterPlayer_MissingRequiredData() {
		RosterPlayer createRosterPlayer = rosterPlayerRepo.createRosterPlayer(getMockRosterPlayer(null,  new LocalDate("2009-12-01"), new LocalDate("2009-12-15")));
		Assert.assertTrue(createRosterPlayer.isCreated());
	}

	@Test
	public void updateRosterPlayer_Updated() {
		RosterPlayer updateRosterPlayer = rosterPlayerRepo.updateRosterPlayer(getMockRosterPlayer(Position.G, new LocalDate("2009-10-30"), new LocalDate("2009-11-04")));
		RosterPlayer findRosterPlayer = rosterPlayerRepo.findRosterPlayer("Puzdrakiewicz", "Thad", new LocalDate("1966-06-02"), new LocalDate("2009-10-30"));
		Assert.assertTrue(updateRosterPlayer.isUpdated());
		Assert.assertEquals(Position.G, findRosterPlayer.getPosition());
	}

	@Test
	public void updateRosterPlayer_NotFound() {
		RosterPlayer updateRosterPlayer = rosterPlayerRepo.updateRosterPlayer(getMockRosterPlayer(Position.G, new LocalDate("2009-10-29"), new LocalDate("2009-11-04")));
		Assert.assertTrue(updateRosterPlayer.isNotFound());
	}

	@Test(expected=DataIntegrityViolationException.class)
	public void updateRosterPlayer_Exception_MissingRequiredData() {
		RosterPlayer updateRosterPlayer = rosterPlayerRepo.updateRosterPlayer(getMockRosterPlayer(null, new LocalDate("2009-10-30"), new LocalDate("2009-11-04")));
		rosterPlayerRepo.updateRosterPlayer(updateRosterPlayer);
	}

	@Test
	public void deleteRosterPlayer_Deleted() {
		RosterPlayer deleteRosterPlayer = rosterPlayerRepo.deleteRosterPlayer("Puzdrakiewicz", "Thad", new LocalDate("1966-06-02"), new LocalDate("2010-01-10"));
		RosterPlayer findRosterPlayer = rosterPlayerRepo.findRosterPlayer("Puzdrakiewicz", "Thad", new LocalDate("1966-06-02"), new LocalDate("2010-01-10"));
		Assert.assertTrue(deleteRosterPlayer.isDeleted());
		Assert.assertTrue(findRosterPlayer.isNotFound());
	}

	@Test
	public void deleteRosterPlayer_NotFound() {
		RosterPlayer deleteRosterPlayer = rosterPlayerRepo.deleteRosterPlayer("Puzdrakiewicz", "Thad", new LocalDate("1966-06-10"), new LocalDate("2010-01-11"));
		Assert.assertTrue(deleteRosterPlayer.isNotFound());
	}

	private RosterPlayer getMockRosterPlayer(Position position, LocalDate fromDate, LocalDate toDate) {
		RosterPlayer rosterPlayer = new RosterPlayer();
		rosterPlayer.setPlayer(getMockPlayer());
		rosterPlayer.setTeam(getMockTeam());
		rosterPlayer.setFromDate(fromDate);
		rosterPlayer.setToDate(toDate);
		rosterPlayer.setPosition(position);
		rosterPlayer.setNumber("99");
		return rosterPlayer;
	}

	private Player getMockPlayer() {
		Player player = new Player();
		player.setId(2L);
		player.setLastName("Puzdrakiewicz");
		player.setFirstName("Thad");
		player.setBirthdate(new LocalDate("1966-06-02"));
		return player;
	}
	
	private Team getMockTeam() {
		Team team = new Team();
		team.setId(2L);
		team.setTeamKey("harlem-globetrotters");
		return team;
	}
}
