package com.rossotti.basketball.app.service;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.rossotti.basketball.client.dto.OfficialDTO;
import com.rossotti.basketball.dao.exception.NoSuchEntityException;
import com.rossotti.basketball.dao.model.GameOfficial;
import com.rossotti.basketball.dao.model.Official;
import com.rossotti.basketball.dao.model.StatusCodeDAO;
import com.rossotti.basketball.dao.repository.OfficialRepository;

@SuppressWarnings("CanBeFinal")
@RunWith(MockitoJUnitRunner.class)
public class OfficialServiceTest {
	@Mock
	private OfficialRepository officialRepo;

	@InjectMocks
	private OfficialService officialService;

	@Test(expected=NoSuchEntityException.class)
	public void getGameOfficials_notFound() {
		when(officialRepo.findOfficial(anyString(), anyString(), (LocalDate) anyObject()))
				.thenReturn(createMockOfficial("", "", StatusCodeDAO.NotFound));
		List<GameOfficial> officials = officialService.getGameOfficials(createMockOfficialDTOs(), new LocalDate(2015, 8, 26));
		Assert.assertTrue(officials.size() == 0);
	}

	@Test
	public void getGameOfficials_found() {
		when(officialRepo.findOfficial(anyString(), anyString(), (LocalDate) anyObject()))
				.thenReturn(createMockOfficial("Adams", "Samuel", StatusCodeDAO.Found))
				.thenReturn(createMockOfficial("Coors", "Adolph", StatusCodeDAO.Found));
		List<GameOfficial> officials = officialService.getGameOfficials(createMockOfficialDTOs(), new LocalDate(2015, 11, 26));
		Assert.assertEquals(2, officials.size());
		Assert.assertEquals("Coors", officials.get(1).getOfficial().getLastName());
		Assert.assertEquals("Adolph", officials.get(1).getOfficial().getFirstName());
	}

	private OfficialDTO[] createMockOfficialDTOs() {
		OfficialDTO[] officials = new OfficialDTO[2];
		officials[0] = createMockOfficialDTO("Adams", "Samuel");
		officials[1] = createMockOfficialDTO("Coors", "Adolph");
		return officials;
	}

	private OfficialDTO createMockOfficialDTO(String lastName, String firstName) {
		OfficialDTO official = new OfficialDTO();
		official.setLast_name(lastName);
		official.setFirst_name(firstName);
		return official;
	}

	private Official createMockOfficial(String lastName, String firstName, StatusCodeDAO statusCode) {
		Official official = new Official();
		official.setLastName(lastName);
		official.setFirstName(firstName);
		official.setStatusCode(statusCode);
		return official;
	}
}