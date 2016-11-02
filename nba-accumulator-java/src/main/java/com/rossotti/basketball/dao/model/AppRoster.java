package com.rossotti.basketball.dao.model;

import java.util.List;

public class AppRoster {
	private AppStatus appStatus;
	public void setAppStatus(AppStatus appStatus) {
		this.appStatus = appStatus;
	}
	public Boolean isAppCompleted() {
		return appStatus == AppStatus.Completed;
	}
	public Boolean isAppClientError() {
		return appStatus == AppStatus.ClientError;
	}
	public Boolean isAppServerError() {
		return appStatus == AppStatus.ServerError;
	}
	private List<RosterPlayer> rosterPlayers;
	public List<RosterPlayer> getRosterPlayers() {
		return rosterPlayers;
	}
	public void setRosterPlayers(List<RosterPlayer> rosterPlayers) {
		this.rosterPlayers = rosterPlayers;
	}
}