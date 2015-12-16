package com.rossotti.basketball.dao;

import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rossotti.basketball.models.Player;
import com.rossotti.basketball.models.RosterPlayer;
import com.rossotti.basketball.models.RosterPlayer.Position;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"config/applicationContextTest.xml"})
public class RosterPlayerDaoTest {

	@Autowired
	private RosterPlayerDAO rosterPlayerDAO;

	// 1, 'Luke', 'Puzdrakiewicz', '2002-02-20', 'Luke Puzdrakiewicz'
	// 1, 'chicago-zephyrs','2009-07-01', '9999-12-31'
	// 1, 1, '2009-10-30', '2009-11-03', 'PG'

	@Test
	public void findRosterPlayerByNameBirthdate_Found() {
		RosterPlayer findRosterPlayer = rosterPlayerDAO.findRosterPlayer("Puzdrakiewicz", "Luke", new LocalDate("2002-02-20"), new LocalDate("2009-10-30"), new LocalDate("2009-11-03"));
		Assert.assertEquals("Luke Puzdrakiewicz", findRosterPlayer.getPlayer().getDisplayName());
		Assert.assertEquals(Position.PG, findRosterPlayer.getPosition());
		Assert.assertTrue(findRosterPlayer.isFound());
	}

	@Test
	public void findRosterPlayerByNameBirthdate_NotFound_LastName() {
		RosterPlayer findRosterPlayer = rosterPlayerDAO.findRosterPlayer("Puzdrakiewic", "Luke", new LocalDate("2002-02-20"), new LocalDate("2009-10-30"), new LocalDate("2009-11-03"));
		Assert.assertTrue(findRosterPlayer.isNotFound());
	}

	@Test
	public void findRosterPlayerByNameBirthdate_NotFound_FirstName() {
		RosterPlayer findRosterPlayer = rosterPlayerDAO.findRosterPlayer("Puzdrakiewicz", "Luk", new LocalDate("2002-02-20"), new LocalDate("2009-10-30"), new LocalDate("2009-11-03"));
		Assert.assertTrue(findRosterPlayer.isNotFound());
	}

	@Test
	public void findRosterPlayerByNameBirthdate_NotFound_Birthdate() {
		RosterPlayer findRosterPlayer = rosterPlayerDAO.findRosterPlayer("Puzdrakiewicz", "Luke", new LocalDate("2002-02-21"), new LocalDate("2009-10-30"), new LocalDate("2009-11-03"));
		Assert.assertTrue(findRosterPlayer.isNotFound());
	}

	@Test
	public void findRosterPlayerByNameBirthdate_NotFound_FromDate() {
		RosterPlayer findRosterPlayer = rosterPlayerDAO.findRosterPlayer("Puzdrakiewicz", "Luke", new LocalDate("2002-02-20"), new LocalDate("2009-10-29"), new LocalDate("2009-11-03"));
		Assert.assertTrue(findRosterPlayer.isNotFound());
	}

	@Test
	public void findRosterPlayerByNameBirthdate_NotFound_ToDate() {
		RosterPlayer findRosterPlayer = rosterPlayerDAO.findRosterPlayer("Puzdrakiewicz", "Luke", new LocalDate("2002-02-20"), new LocalDate("2009-10-30"), new LocalDate("2009-11-04"));
		Assert.assertTrue(findRosterPlayer.isNotFound());
	}

	@Test
	public void findRosterPlayerByNameTeam_Found() {
		RosterPlayer findRosterPlayer = rosterPlayerDAO.findRosterPlayer("Puzdrakiewicz", "Luke", "chicago-zephyrs", new LocalDate("2009-10-30"), new LocalDate("2009-11-03"));
		Assert.assertEquals("Luke Puzdrakiewicz", findRosterPlayer.getPlayer().getDisplayName());
		Assert.assertEquals(Position.PG, findRosterPlayer.getPosition());
		Assert.assertEquals("CHI",  findRosterPlayer.getTeam().getAbbr());
		Assert.assertTrue(findRosterPlayer.isFound());
	}

	@Test
	public void findRosterPlayerByNameTeam_NotFound_LastName() {
		RosterPlayer findRosterPlayer = rosterPlayerDAO.findRosterPlayer("Puzdrakiewic", "Luke", "chicago-zephyrs", new LocalDate("2009-10-30"), new LocalDate("2009-11-03"));
		Assert.assertTrue(findRosterPlayer.isNotFound());
	}

	@Test
	public void findRosterPlayerByNameTeam_NotFound_FirstName() {
		RosterPlayer findRosterPlayer = rosterPlayerDAO.findRosterPlayer("Puzdrakiewicz", "Luk", "chicago-zephyrs", new LocalDate("2009-10-30"), new LocalDate("2009-11-03"));
		Assert.assertTrue(findRosterPlayer.isNotFound());
	}

	@Test
	public void findRosterPlayerByNameTeam_NotFound_TeamKey() {
		RosterPlayer findRosterPlayer = rosterPlayerDAO.findRosterPlayer("Puzdrakiewicz", "Luke", "chicago-zephers", new LocalDate("2009-10-30"), new LocalDate("2009-11-03"));
		Assert.assertTrue(findRosterPlayer.isNotFound());
	}
	
	@Test
	public void findRosterPlayerByNameTeam_NotFound_FromDate() {
		RosterPlayer findRosterPlayer = rosterPlayerDAO.findRosterPlayer("Puzdrakiewicz", "Luke", "chicago-zephyrs", new LocalDate("2009-10-29"), new LocalDate("2009-11-03"));
		Assert.assertTrue(findRosterPlayer.isNotFound());
	}

	@Test
	public void findRosterPlayerByNameTeam_NotFound_ToDate() {
		RosterPlayer findRosterPlayer = rosterPlayerDAO.findRosterPlayer("Puzdrakiewicz", "Luke", "chicago-zephyrs", new LocalDate("2009-10-30"), new LocalDate("2009-11-04"));
		Assert.assertTrue(findRosterPlayer.isNotFound());
	}

	// 1, 'chicago-zephyrs','2009-07-01', '9999-12-31'
	// 1, 'Luke', 'Puzdrakiewicz', '2002-02-20', 'Luke Puzdrakiewicz'
	// 2, 'Thad', 'Puzdrakiewicz', '1966-06-02', 'Thad Puzdrakiewicz'
	// 1, 1, 2, '2009-10-30', '2009-11-03', 'PG'
	// 2, 1, 3, '2009-10-30', '2009-11-04', 'C'

	@Test
	public void findRosterPlayersByName_Found() {
		List<RosterPlayer> findRosterPlayers = rosterPlayerDAO.findRosterPlayers("chicago-zephyrs", new LocalDate("2009-10-30"));
		Assert.assertEquals(2, findRosterPlayers.size());
	}

	@Test
	public void findRosterPlayersByName_NotFound() {
		List<RosterPlayer> findRosterPlayers = rosterPlayerDAO.findRosterPlayers("chicago-zephyrs", new LocalDate("2009-10-29"));
		Assert.assertEquals(0, findRosterPlayers.size());
	}

	// Create tests - execute in service layer

	//'Thad', 'Puzdrakiewicz', '1966-06-10', 'Thad Puzdrakiewicz'
	// 2, 1, 3, '2009-10-30', '2009-11-04', 'C'

	@Test
	public void updateRosterPlayer_Updated() {
		RosterPlayer updateRosterPlayer = rosterPlayerDAO.updateRosterPlayer(updateMockRosterPlayer("Puzdrakiewicz", "Thad", Position.G, new LocalDate("1966-06-02"), new LocalDate("2009-10-30"), new LocalDate("2009-11-04")));
		RosterPlayer findRosterPlayer = rosterPlayerDAO.findRosterPlayer("Puzdrakiewicz", "Thad", new LocalDate("1966-06-02"), new LocalDate("2009-10-30"), new LocalDate("2009-11-04"));
		Assert.assertTrue(updateRosterPlayer.isUpdated());
		Assert.assertEquals(Position.G, findRosterPlayer.getPosition());
	}

	@Test
	public void updatePlayer_NotFound() {
		RosterPlayer updateRosterPlayer = rosterPlayerDAO.updateRosterPlayer(updateMockRosterPlayer("Puzdrakiewicz", "Thad", Position.G, new LocalDate("1966-06-02"), new LocalDate("2009-10-29"), new LocalDate("2009-11-04")));
		Assert.assertTrue(updateRosterPlayer.isNotFound());
	}

	@Test(expected=DataIntegrityViolationException.class)
	public void updatePlayer_Exception_MissingRequiredData() {
		RosterPlayer updateRosterPlayer = rosterPlayerDAO.updateRosterPlayer(updateMockRosterPlayer("Puzdrakiewicz", "Thad", null, new LocalDate("1966-06-02"), new LocalDate("2009-10-30"), new LocalDate("2009-11-04")));
		rosterPlayerDAO.updateRosterPlayer(updateRosterPlayer);
	}

	// Delete tests - execute in service layer

	private RosterPlayer updateMockRosterPlayer(String lastName, String firstName, Position position, LocalDate birthdate, LocalDate fromDate, LocalDate toDate) {
		Player player = new Player();
		player.setLastName(lastName);
		player.setFirstName(firstName);
		player.setBirthdate(birthdate);
		RosterPlayer rosterPlayer = new RosterPlayer();
		rosterPlayer.setFromDate(fromDate);
		rosterPlayer.setToDate(toDate);
		rosterPlayer.setNumber("99");
		rosterPlayer.setPosition(position);
		rosterPlayer.setPlayer(player);
		return rosterPlayer;
	}
}