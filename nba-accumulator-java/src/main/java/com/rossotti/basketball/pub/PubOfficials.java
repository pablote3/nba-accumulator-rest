package com.rossotti.basketball.pub;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PubOfficials {
	private final URI self;
	private final List<PubOfficial> officials = new ArrayList<>();

	@JsonCreator
	public PubOfficials(@JsonProperty("self") URI self, 
					@JsonProperty("officials") List<PubOfficial> officials) {
		this.self = self;
		if (officials != null) {
			this.officials.addAll(officials);
		}
	}

	public URI getSelf() {
		return self;
	}
	public List<PubOfficial> getOfficials() {
		return officials;
	}
}
