package com.rossotti.basketball.pub;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PubBoxScores {

	private final List<PubBoxScore> boxScores = new ArrayList<>();

	@JsonCreator
	public PubBoxScores(@JsonProperty("boxScores") List<PubBoxScore> boxScores) {
		if (boxScores != null) {
			this.boxScores.addAll(boxScores);
		}
	}

	public List<PubBoxScore> getBoxScores() {
		return boxScores;
	}
}
