package com.rossotti.basketball.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
	private Game game;
	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}

	@ManyToOne(cascade=CascadeType.ALL)
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
			.append("  officialId: " + this.official.getId() + "\n")
			.toString();
	}
}