package com.rossotti.basketball.dao;

import java.util.List;

import org.joda.time.LocalDate;

import com.rossotti.basketball.models.Official;

public interface OfficialDAO {
	public Official findOfficial(String lastName, String firstName, LocalDate fromDate, LocalDate toDate);
	public List<Official> findOfficials(String lastName, String firstName);
	public List<Official> findOfficials(LocalDate fromDate, LocalDate toDate);
	public void createOfficial(Official official);
	public void updateOfficial(Official official);
	public void deleteOfficial(String lastName, String firstName, LocalDate fromDate, LocalDate toDate);
}
