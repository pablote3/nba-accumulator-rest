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

import com.rossotti.basketball.dao.exception.DuplicateEntityException;
import com.rossotti.basketball.dao.model.Official;
import com.rossotti.basketball.dao.repository.OfficialDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class OfficialRepositoryTest {

	@Autowired
	private OfficialDAO officialDAO;

	//'Joe', 'LateCall', '2009-07-01', '2010-06-30'

	@Test
	public void findOfficialByName_Found_FromDate() {
		Official findOfficial = officialDAO.findOfficial("LateCall", "Joe", new LocalDate("2009-07-01"));
		Assert.assertEquals("96", findOfficial.getNumber());
		Assert.assertTrue(findOfficial.isFound());
	}

	@Test
	public void findOfficialByName_Found_ToDate() {
		Official findOfficial = officialDAO.findOfficial("LateCall", "Joe", new LocalDate("2010-06-30"));
		Assert.assertEquals("96", findOfficial.getNumber());
		Assert.assertTrue(findOfficial.isFound());
	}

	@Test
	public void findOfficialByName_NotFound_LastName() {
		Official findOfficial = officialDAO.findOfficial("LateCalls", "Joe", new LocalDate("2009-07-01"));
		Assert.assertTrue(findOfficial.isNotFound());
	}

	@Test
	public void findOfficialByName_NotFound_FirstName() {
		Official findOfficial = officialDAO.findOfficial("LateCall", "Joey", new LocalDate("2009-07-01"));
		Assert.assertTrue(findOfficial.isNotFound());
	}

	@Test
	public void findOfficialByName_NotFound_BeforeAsOfDate() {
		Official findOfficial = officialDAO.findOfficial("LateCall", "Joe", new LocalDate("2009-06-30"));
		Assert.assertTrue(findOfficial.isNotFound());
	}

	@Test
	public void findOfficialByName_NotFound_AfterAsOfDate() {
		Official findOfficial = officialDAO.findOfficial("LateCall", "Joe", new LocalDate("2010-07-01"));
		Assert.assertTrue(findOfficial.isNotFound());
	}

	//'Mike', 'MissedCall', '2009-07-01', '2010-06-30'
	//'Mike', 'MissedCall', '2010-07-01', '2011-06-30'

	@Test
	public void findOfficialsByName() {
		List<Official> findOfficials = officialDAO.findOfficials("MissedCall","Mike");
		Assert.assertEquals(2, findOfficials.size());
	}

	@Test
	public void findOfficialsByName_NotFound_LastName() {
		List<Official> findOfficials = officialDAO.findOfficials("MissedCalls", "Mike");
		Assert.assertEquals(0, findOfficials.size());
	}

	@Test
	public void findOfficialsByName_NotFound_FirstName() {
		List<Official> findOfficials = officialDAO.findOfficials("MissedCall", "Mikey");
		Assert.assertEquals(0, findOfficials.size());
	}
	
	@Test
	public void findOfficialsByDateRange_Found() {
		List<Official> officials = officialDAO.findOfficials(new LocalDate("2009-10-31"));
		Assert.assertEquals(2, officials.size());
	}

	@Test
	public void findOfficialsByDateRange_NotFound() {
		List<Official> findOfficials = officialDAO.findOfficials(new LocalDate("1909-10-31"));
		Assert.assertEquals(0, findOfficials.size());
	}

	//'Hefe', 'QuestionableCall', '2005-07-01', '2006-06-30'

	@Test
	public void createOfficial_Created() {
		Official createOfficial = officialDAO.createOfficial(createMockOfficial("BadCall", "Melvin", new LocalDate("2012-07-01"), new LocalDate("2012-07-01")));
		Official findOfficial = officialDAO.findOfficial("BadCall", "Melvin", new LocalDate("2012-07-01"));
		Assert.assertTrue(createOfficial.isCreated());
		Assert.assertEquals("999", findOfficial.getNumber());
	}

	@Test(expected=DuplicateEntityException.class)
	public void createOfficial_OverlappingDates() {
		officialDAO.createOfficial(createMockOfficial("QuestionableCall", "Hefe", new LocalDate("2005-07-01"), new LocalDate("2006-06-30")));
	}

	@Test(expected=PropertyValueException.class)
	public void createOfficial_MissingRequiredData() {
		Official createOfficial = new Official();
		createOfficial.setLastName("missing-required-data");
		createOfficial.setFirstName("missing-required-data");
		officialDAO.createOfficial(createOfficial);
	}

	//'Mike', 'MissedCall', '2009-07-01', '2010-06-30'

	@Test
	public void updateOfficial_Updated() {
		Official updateOfficial = officialDAO.updateOfficial(updateMockOfficial("MissedCall", "Mike", new LocalDate("2009-07-01"), new LocalDate("2010-06-30")));
		Official findOfficial = officialDAO.findOfficial("MissedCall", "Mike", new LocalDate("2009-07-01"));
		Assert.assertTrue(updateOfficial.isUpdated());
		Assert.assertEquals("998", findOfficial.getNumber());
	}

	@Test
	public void updateOfficial_NotFound() {
		Official updateOfficial = officialDAO.updateOfficial(updateMockOfficial("Sandin", "Erik", new LocalDate("2009-06-30"), new LocalDate("2009-06-30")));
		Assert.assertTrue(updateOfficial.isNotFound());
	}

	@Test(expected=DataIntegrityViolationException.class)
	public void updateOfficial_MissingRequiredData() {
		Official updateOfficial = updateMockOfficial("MissedCall", "Mike", new LocalDate("2009-07-01"), new LocalDate("2010-06-30"));
		updateOfficial.setNumber(null);
		officialDAO.updateOfficial(updateOfficial);
	}

	//'Limo', 'TerribleCall', '2005-07-01', '2006-06-30');

	@Test
	public void deleteOfficial_Deleted() {
		Official deleteOfficial = officialDAO.deleteOfficial("TerribleCall", "Limo", new LocalDate("2005-07-01"));
		Official findOfficial = officialDAO.findOfficial("TerribleCall", "Limo", new LocalDate("2005-07-01"));
		Assert.assertTrue(deleteOfficial.isDeleted());
		Assert.assertTrue(findOfficial.isNotFound());
	}

	@Test
	public void deleteOfficial_NotFound() {
		Official deleteOfficial = officialDAO.deleteOfficial("Limoe", "TerribleCall", new LocalDate("2005-07-01"));
		Assert.assertTrue(deleteOfficial.isNotFound());
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
