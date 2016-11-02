package com.rossotti.basketball.dao.model;

public class AppGame {
	private AppStatus appStatus;
	public AppStatus getAppStatus() {
		return appStatus;
	}
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
	public Boolean isAppRosterUpdate() {
		return appStatus == AppStatus.RosterUpdate;
	}
	public Boolean isAppRosterComplete() {
		return appStatus == AppStatus.RosterComplete;
	}
	public Boolean isAppRosterError() {
		return appStatus == AppStatus.RosterError;
	}
	public Boolean isAppOfficialError() {
		return appStatus == AppStatus.OfficialError;
	}
	public Boolean isAppTeamError() {
		return appStatus == AppStatus.TeamError;
	}
	private Game game;
	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	private String rosterLastTeam;
	public String getRosterLastTeam() {
		return rosterLastTeam;
	}
	public void setRosterLastTeam(String rosterLastTeam) {
		this.rosterLastTeam = rosterLastTeam;
	}
}
