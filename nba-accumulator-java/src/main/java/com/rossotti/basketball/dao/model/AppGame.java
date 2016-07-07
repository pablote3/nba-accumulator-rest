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
	private Game game;
	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}
}
