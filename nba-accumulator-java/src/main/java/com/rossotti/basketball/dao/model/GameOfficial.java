package com.rossotti.basketball.dao.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.rossotti.basketball.dao.pub.PubGameOfficial;
import com.rossotti.basketball.dao.pub.PubOfficial;

@Entity
@Table (name="gameOfficial")
public class GameOfficial {
	public GameOfficial() {}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="gameId", referencedColumnName="id", nullable=false)
	@JsonBackReference(value="gameOfficial-to-game")
	private Game game;
	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}

	@ManyToOne(cascade=CascadeType.MERGE)
	@JoinColumn(name="officialId", referencedColumnName="id", nullable=false)
	private Official official;
	public Official getOfficial() {
		return official;
	}
	public void setOfficial(Official official) {
		this.official = official;
	}

	public String toString() {
		return new StringBuffer()
			.append("  id: " + this.id + "\n")
			.append("  official: " + this.official.toString() + "\n")
			.toString();
	}
	
	public PubGameOfficial toPubGameOfficial(UriInfo uriInfo) {
		PubOfficial pubOfficial = this.getOfficial().toPubOfficial(uriInfo);
		return new PubGameOfficial(pubOfficial);
	}
}