package com.rossotti.basketball.dao.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.rossotti.basketball.dao.pub.PubBoxScorePlayer;

import javax.persistence.*;
import javax.ws.rs.core.UriInfo;

@Entity
@Table(name="boxScorePlayer")
public class BoxScorePlayer {
	public BoxScorePlayer() {}

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
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
	@JsonBackReference(value="boxScorePlayer-to-boxScore")
	private BoxScore boxScore;
	public BoxScore getBoxScore() {
		return boxScore;
	}
	public void setBoxScore(BoxScore boxScore) {
		this.boxScore = boxScore;
	}

	@ManyToOne()
	@JoinColumn(name="rosterPlayerId", referencedColumnName="id", nullable=false)
	@JsonBackReference(value="boxScorePlayer-to-rosterPlayer")
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

	@Column(name="starter")
	private boolean starter;
	public boolean getStarter() {
		return starter;
	}
	public void setStarter(boolean starter) {
		this.starter = starter;
	}

	@Column(name="minutes")
	private Short minutes;
	public Short getMinutes() {
		return minutes;
	}
	public void setMinutes(Short minutes) {
		this.minutes = minutes;
	}

	@Column(name="points")
	private Short points;
	public Short getPoints() {
		return points;
	}
	public void setPoints(Short points) {
		this.points = points;
	}

	@Column(name="assists")
	private Short assists;
	public Short getAssists() {
		return assists;
	}
	public void setAssists(Short assists) {
		this.assists = assists;
	}

	@Column(name="turnovers")
	private Short turnovers;
	public Short getTurnovers() {
		return turnovers;
	}
	public void setTurnovers(Short turnovers) {
		this.turnovers = turnovers;
	}

	@Column(name="steals")
	private Short steals;
	public Short getSteals() {
		return steals;
	}
	public void setSteals(Short steals) {
		this.steals = steals;
	}

	@Column(name="blocks")
	private Short blocks;
	public Short getBlocks() {
		return blocks;
	}
	public void setBlocks(Short blocks) {
		this.blocks = blocks;
	}

	@Column(name="fieldGoalAttempts")
	private Short fieldGoalAttempts;
	public Short getFieldGoalAttempts() {
		return fieldGoalAttempts;
	}
	public void setFieldGoalAttempts(Short fieldGoalAttempts) {
		this.fieldGoalAttempts = fieldGoalAttempts;
	}

	@Column(name="fieldGoalMade")
	private Short fieldGoalMade;
	public Short getFieldGoalMade() {
		return fieldGoalMade;
	}
	public void setFieldGoalMade(Short fieldGoalMade) {
		this.fieldGoalMade = fieldGoalMade;
	}

	@Column(name="fieldGoalPercent")
	private Float fieldGoalPercent;
	public Float getFieldGoalPercent() {
		return fieldGoalPercent;
	}
	public void setFieldGoalPercent(Float fieldGoalPercent) {
		this.fieldGoalPercent = fieldGoalPercent;
	}

	@Column(name="threePointAttempts")
	private Short threePointAttempts;
	public Short getThreePointAttempts() {
		return threePointAttempts;
	}
	public void setThreePointAttempts(Short threePointAttempts) {
		this.threePointAttempts = threePointAttempts;
	}

	@Column(name="threePointMade")
	private Short threePointMade;
	public Short getThreePointMade() {
		return threePointMade;
	}
	public void setThreePointMade(Short threePointMade) {
		this.threePointMade = threePointMade;
	}

	@Column(name="threePointPercent")
	private Float threePointPercent;
	public Float getThreePointPercent() {
		return threePointPercent;
	}
	public void setThreePointPercent(Float threePointPercent) {
		this.threePointPercent = threePointPercent;
	}

	@Column(name="freeThrowAttempts")
	private Short freeThrowAttempts;
	public Short getFreeThrowAttempts() {
		return freeThrowAttempts;
	}
	public void setFreeThrowAttempts(Short freeThrowAttempts) {
		this.freeThrowAttempts = freeThrowAttempts;
	}

	@Column(name="freeThrowMade")
	private Short freeThrowMade;
	public Short getFreeThrowMade() {
		return freeThrowMade;
	}
	public void setFreeThrowMade(Short freeThrowMade) {
		this.freeThrowMade = freeThrowMade;
	}

	@Column(name="freeThrowPercent")
	private Float freeThrowPercent;
	public Float getFreeThrowPercent() {
		return freeThrowPercent;
	}
	public void setFreeThrowPercent(Float freeThrowPercent) {
		this.freeThrowPercent = freeThrowPercent;
	}

	@Column(name="reboundsOffense")
	private Short reboundsOffense;
	public Short getReboundsOffense() {
		return reboundsOffense;
	}
	public void setReboundsOffense(Short reboundsOffense) {
		this.reboundsOffense = reboundsOffense;
	}

	@Column(name="reboundsDefense")
	private Short reboundsDefense;
	public Short getReboundsDefense() {
		return reboundsDefense;
	}
	public void setReboundsDefense(Short reboundsDefense) {
		this.reboundsDefense = reboundsDefense;
	}

	@Column(name="personalFouls")
	private Short personalFouls;
	public Short getPersonalFouls() {
		return personalFouls;
	}
	public void setPersonalFouls(Short personalFouls) {
		this.personalFouls = personalFouls;
	}

	public String toString() {
		return ("  id: " + this.id + "\n") +
				"  position: " + this.position + "\n" +
				"  points: " + this.points + "\n" +
				"  assists: " + this.assists + "\n" +
				"  turnovers: " + this.turnovers + "\n" +
				"  steals: " + this.steals + "\n" +
				"  blocks: " + this.blocks;
	}

	public PubBoxScorePlayer toPubBoxScorePlayer(UriInfo uriInfo) {
		return new PubBoxScorePlayer(
				this.getPosition(),
				this.getStarter(),
				this.getMinutes(),
				this.getPoints(),
				this.getAssists(),
				this.getTurnovers(),
				this.getSteals(),
				this.getBlocks(),
				this.getFieldGoalAttempts(),
				this.getFieldGoalMade(),
				this.getFieldGoalPercent(),
				this.getThreePointAttempts(),
				this.getThreePointMade(),
				this.getThreePointPercent(),
				this.getFreeThrowAttempts(),
				this.getFreeThrowMade(),
				this.getFreeThrowPercent(),
				this.getReboundsOffense(),
				this.getReboundsDefense(),
				this.getPersonalFouls(),
				this.getRosterPlayer().toPubRosterPlayer(uriInfo));
	}
}