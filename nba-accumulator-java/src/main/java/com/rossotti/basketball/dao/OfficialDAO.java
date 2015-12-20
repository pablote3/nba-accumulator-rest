package com.rossotti.basketball.dao;

import java.util.List;

import org.joda.time.LocalDate;

import com.rossotti.basketball.model.Official;

public interface OfficialDAO {
	public Official findOfficial(String lastName, String firstName, LocalDate asOfDate);
	public List<Official> findOfficials(String lastName, String firstName);
	public List<Official> findOfficials(LocalDate asOfDate);
	public Official createOfficial(Official official);
	public Official updateOfficial(Official official);
	public Official deleteOfficial(String lastName, String firstName, LocalDate asOfDate);
}
