package com.rossotti.basketball.client.dto;

public class StatsDTO {
	private StatusCodeDTO statusCode;
	public StatusCodeDTO getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(StatusCodeDTO statusCode) {
		this.statusCode = statusCode;
	}
	public Boolean isFound() {
		return statusCode == StatusCodeDTO.Found;
	}
	public Boolean isNotFound() {
		return statusCode == StatusCodeDTO.NotFound;
	}
	public Boolean isJsonException() {
		return statusCode == StatusCodeDTO.ClientException;
	}
}
