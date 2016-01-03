package com.rossotti.basketball.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table (name="boxScorePlayer")
public class BoxScorePlayer {
	public BoxScorePlayer() {}

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

	@ManyToOne()
	@JoinColumn(name="boxScoreId", referencedColumnName="id", nullable=false)
	private BoxScore boxScore;
	public BoxScore getBoxScore() {
		return boxScore;
	}
	public void setBoxScore(BoxScore boxScore) {
		this.boxScore = boxScore;
	}

	@ManyToOne()
	@JoinColumn(name="rosterPlayerId", referencedColumnName="id", nullable=false)
	private RosterPlayer rosterPlayer;
	public RosterPlayer getRosterPlayer() {
		return rosterPlayer;
	}
	public void setRosterPlayer(RosterPlayer rosterPlayer) {
		this.rosterPlayer = rosterPlayer;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="position", length=5, nullable=false)
	private Position position;
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	public enum Position {
		PG,
		SG,
		SF,
		PF,
		C,
		G,
		F
	}

	@Column(name="starter", nullable=true)
	private boolean starter;
	public boolean getStarter() {
		return starter;
	}
	public void setStarter(boolean starter) {
		this.starter = starter;
	}

	@Column(name="minutes", nullable=true)
	private Short minutes;
	public Short getMinutes() {
		return minutes;
	}
	public void setMinutes(Short minutes) {
		this.minutes = minutes;
	}

	@Column(name="points", nullable=true)
	private Short points;
	public Short getPoints() {
		return points;
	}
	public void setPoints(Short points) {
		this.points = points;
	}

	@Column(name="assists", nullable=true)
	private Short assists;
	public Short getAssists() {
		return assists;
	}
	public void setAssists(Short assists) {
		this.assists = assists;
	}

	@Column(name="turnovers", nullable=true)
	private Short turnovers;
	public Short getTurnovers() {
		return turnovers;
	}
	public void setTurnovers(Short turnovers) {
		this.turnovers = turnovers;
	}

	@Column(name="steals", nullable=true)
	private Short steals;
	public Short getSteals() {
		return steals;
	}
	public void setSteals(Short steals) {
		this.steals = steals;
	}

	@Column(name="blocks", nullable=true)
	private Short blocks;
	public Short getBlocks() {
		return blocks;
	}
	public void setBlocks(Short blocks) {
		this.blocks = blocks;
	}

	@Column(name="fieldGoalAttempts", nullable=true)
	private Short fieldGoalAttempts;
	public Short getFieldGoalAttempts() {
		return fieldGoalAttempts;
	}
	public void setFieldGoalAttempts(Short fieldGoalAttempts) {
		this.fieldGoalAttempts = fieldGoalAttempts;
	}

	@Column(name="fieldGoalMade", nullable=true)
	private Short fieldGoalMade;
	public Short getFieldGoalMade() {
		return fieldGoalMade;
	}
	public void setFieldGoalMade(Short fieldGoalMade) {
		this.fieldGoalMade = fieldGoalMade;
	}

	@Column(name="fieldGoalPercent", nullable=true)
	private Float fieldGoalPercent;
	public Float getFieldGoalPercent() {
		return fieldGoalPercent;
	}
	public void setFieldGoalPercent(Float fieldGoalPercent) {
		this.fieldGoalPercent = fieldGoalPercent;
	}

	@Column(name="threePointAttempts", nullable=true)
	private Short threePointAttempts;
	public Short getThreePointAttempts() {
		return threePointAttempts;
	}
	public void setThreePointAttempts(Short threePointAttempts) {
		this.threePointAttempts = threePointAttempts;
	}

	@Column(name="threePointMade", nullable=true)
	private Short threePointMade;
	public Short getThreePointMade() {
		return threePointMade;
	}
	public void setThreePointMade(Short threePointMade) {
		this.threePointMade = threePointMade;
	}

	@Column(name="threePointPercent", nullable=true)
	private Float threePointPercent;
	public Float getThreePointPercent() {
		return threePointPercent;
	}
	public void setThreePointPercent(Float threePointPercent) {
		this.threePointPercent = threePointPercent;
	}

	@Column(name="freeThrowAttempts", nullable=true)
	private Short freeThrowAttempts;
	public Short getFreeThrowAttempts() {
		return freeThrowAttempts;
	}
	public void setFreeThrowAttempts(Short freeThrowAttempts) {
		this.freeThrowAttempts = freeThrowAttempts;
	}

	@Column(name="freeThrowMade", nullable=true)
	private Short freeThrowMade;
	public Short getFreeThrowMade() {
		return freeThrowMade;
	}
	public void setFreeThrowMade(Short freeThrowMade) {
		this.freeThrowMade = freeThrowMade;
	}

	@Column(name="freeThrowPercent", nullable=true)
	private Float freeThrowPercent;
	public Float getFreeThrowPercent() {
		return freeThrowPercent;
	}
	public void setFreeThrowPercent(Float freeThrowPercent) {
		this.freeThrowPercent = freeThrowPercent;
	}

	@Column(name="reboundsOffense", nullable=true)
	private Short reboundsOffense;
	public Short getReboundsOffense() {
		return reboundsOffense;
	}
	public void setReboundsOffense(Short reboundsOffense) {
		this.reboundsOffense = reboundsOffense;
	}

	@Column(name="reboundsDefense", nullable=true)
	private Short reboundsDefense;
	public Short getReboundsDefense() {
		return reboundsDefense;
	}
	public void setReboundsDefense(Short reboundsDefense) {
		this.reboundsDefense = reboundsDefense;
	}

	@Column(name="personalFouls", nullable=true)
	private Short personalFouls;
	public Short getPersonalFouls() {
		return personalFouls;
	}
	public void setPersonalFouls(Short personalFouls) {
		this.personalFouls = personalFouls;
	}

	public String toString() {
		return new StringBuffer()
			.append("  id: " + this.id + "\n")
			.append("  position: " + this.position + "\n")
			.append("  points: " + this.points + "\n")
			.append("  assists: " + this.assists + "\n")
			.append("  turnovers: " + this.turnovers + "\n")
			.append("  steals: " + this.steals + "\n")
			.append("  blocks: " + this.blocks)
			.toString();
	}
}