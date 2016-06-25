package com.rossotti.basketball.client.dto;

public class StatsDTO {
	private StatusCode statusCode;
	public StatusCode getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(StatusCode statusCode) {
		this.statusCode = statusCode;
	}
	public Boolean isFound() {
		return statusCode == StatusCode.Found;
	}
	public Boolean isNotFound() {
		return statusCode == StatusCode.NotFound;
	}
	public Boolean isJsonException() {
		return statusCode == StatusCode.ClientException;
	}
}
