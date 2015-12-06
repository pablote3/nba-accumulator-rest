package com.rossotti.basketball.dao;

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

import com.rossotti.basketball.dao.exceptions.DuplicateEntityException;
import com.rossotti.basketball.dao.exceptions.NoSuchEntityException;
import com.rossotti.basketball.models.Official;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"config/applicationContextTest.xml"})
public class OfficialDaoTest {

	@Autowired
	private OfficialDAO officialDAO;

	//'Joe', 'LateCall', '2009-07-01', '2010-06-30'

	@Test
	public void findOfficialByName_MatchFromDate() {
		Official official = officialDAO.findOfficial("LateCall", "Joe", new LocalDate("2009-07-01"), new LocalDate("2009-07-01"));
		Assert.assertEquals("96", official.getNumber());
	}

	@Test
	public void findOfficialByName_MatchToDate() {
		Official official = officialDAO.findOfficial("LateCall", "Joe", new LocalDate("2010-06-30"), new LocalDate("2010-06-30"));
		Assert.assertEquals("96", official.getNumber());
	}

	@Test
	public void findOfficialByName_MatchDateRange() {
		Official official = officialDAO.findOfficial("LateCall", "Joe", new LocalDate("2009-07-01"), new LocalDate("2010-06-30"));
		Assert.assertEquals("96", official.getNumber());
	}

	@Test(expected=NoSuchEntityException.class)
	public void findOfficialByName_NoSuchEntityException_Key() {
		officialDAO.findOfficial("LateCalls", "Joe", new LocalDate("2009-07-01"), new LocalDate("2009-07-01"));
	}

	@Test(expected=NoSuchEntityException.class)
	public void findOfficialByName_NoSuchEntityException_BeforeAsOfDate() {
		officialDAO.findOfficial("LateCall", "Joe", new LocalDate("2009-06-30"), new LocalDate("2009-06-30"));
	}

	@Test(expected=NoSuchEntityException.class)
	public void findOfficialByName_NoSuchEntityException_AfterAsOfDate() {
		officialDAO.findOfficial("LateCall", "Joe", new LocalDate("2010-07-01"), new LocalDate("2010-07-01"));
	}

	//'Mike', 'MissedCall', '2009-07-01', '2010-06-30'
	//'Mike', 'MissedCall', '2010-07-01', '2011-06-30'
	//'Joe', 'LateCall', '2009-07-01', '2010-06-30'

	@Test
	public void findOfficialsByName() {
		List<Official> officials = officialDAO.findOfficials("MissedCall","Mike");
		Assert.assertEquals(2, officials.size());
	}

	@Test(expected=NoSuchEntityException.class)
	public void findOfficialsByName_NoSuchEntityException() {
		officialDAO.findOfficials("MissedCall", "Mikey");
	}
	
	@Test
	public void findOfficialsByDateRange() {
		List<Official> officials = officialDAO.findOfficials(new LocalDate("2009-10-31"), new LocalDate("2010-06-30"));
		Assert.assertEquals(2, officials.size());
	}

	@Test(expected=NoSuchEntityException.class)
	public void findOfficialsByDateRange_NoSuchEntityException() {
		officialDAO.findOfficials(new LocalDate("1909-10-31"), new LocalDate("1910-06-30"));
	}

	//'Hefe', 'QuestionableCall', '2005-07-01', '2006-06-30'

	@Test
	public void createOfficial() {
		officialDAO.createOfficial(createMockOfficial("BadCall", "Melvin", new LocalDate("2012-07-01"), new LocalDate("2012-07-01")));
		Official official = officialDAO.findOfficial("BadCall", "Melvin", new LocalDate("2012-07-01"), new LocalDate("2012-07-01"));
		Assert.assertEquals("999", official.getNumber());
	}

	@Test
	public void createOfficial_NonOverlappingDates() {
		officialDAO.createOfficial(createMockOfficial("QuestionableCall", "Hefe", new LocalDate("2012-07-01"), new LocalDate("9999-12-31")));
		Official official = officialDAO.findOfficial("QuestionableCall", "Hefe", new LocalDate("2012-07-01"), new LocalDate("9999-12-31"));
		Assert.assertEquals("999", official.getNumber());
	}

	@Test(expected=DuplicateEntityException.class)
	public void createOfficial_OverlappingDates() {
		officialDAO.createOfficial(createMockOfficial("QuestionableCall", "Hefe", new LocalDate("2005-07-01"), new LocalDate("2006-06-30")));
	}

	@Test(expected=PropertyValueException.class)
	public void createOfficial_MissingRequiredData() {
		Official official = new Official();
		official.setLastName("missing-required-data");
		official.setFirstName("missing-required-data");
		officialDAO.createOfficial(official);
	}

	//'Mike', 'MissedCall', '2009-07-01', '2010-06-30'

	@Test
	public void updateOfficial() {
		officialDAO.updateOfficial(updateMockOfficial("MissedCall", "Mike", new LocalDate("2009-07-01"), new LocalDate("2010-06-30")));
		Official official = officialDAO.findOfficial("MissedCall", "Mike", new LocalDate("2009-07-01"), new LocalDate("2010-06-30"));
		Assert.assertEquals("998", official.getNumber());
	}

	@Test(expected=NoSuchEntityException.class)
	public void updateOfficial_NoSuchEntityException_Key() {
		officialDAO.updateOfficial(updateMockOfficial("Sandin", "Erik", new LocalDate("2009-06-30"), new LocalDate("2009-06-30")));
	}

	@Test(expected=DataIntegrityViolationException.class)
	public void updateOfficial_MissingRequiredData() {
		Official official = updateMockOfficial("MissedCall", "Mike", new LocalDate("2009-07-01"), new LocalDate("2010-06-30"));
		official.setNumber(null);
		officialDAO.updateOfficial(official);
	}

	//'Limo', 'TerribleCall', '2005-07-01', '2006-06-30');

	@Test(expected=NoSuchEntityException.class)
	public void deleteOfficial() {
		officialDAO.deleteOfficial("Limo", "TerribleCall", new LocalDate("2005-07-01"), new LocalDate("2006-06-30"));
		officialDAO.findOfficial("Limo", "TerribleCall", new LocalDate("2005-07-01"), new LocalDate("2006-06-30"));
	}

	@Test(expected=NoSuchEntityException.class)
	public void deleteOfficial_NoSuchEntityException_Key() {
		officialDAO.deleteOfficial("Limoe", "TerribleCall", new LocalDate("2005-07-01"), new LocalDate("2006-06-30"));
	}

	private Official createMockOfficial(String lastName, String firstName, LocalDate fromDate, LocalDate toDate) {
		Official official = new Official();
		official.setLastName(lastName);
		official.setFirstName(firstName);
		official.setFromDate(fromDate);
		official.setToDate(toDate);
		official.setNumber("999");
		return official;
	}
	
	private Official updateMockOfficial(String lastName, String firstName, LocalDate fromDate, LocalDate toDate) {
		Official official = new Official();
		official.setLastName(lastName);
		official.setFirstName(firstName);
		official.setFromDate(fromDate);
		official.setToDate(toDate);
		official.setNumber("998");
		return official;
	}
}
