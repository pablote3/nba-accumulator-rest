package com.rossotti.basketball.client.dto;

import org.joda.time.DateTime;

public class StandingsDTO {
	public int httpStatus;
	public DateTime standings_date;
	public StandingDTO[] standing;
}
