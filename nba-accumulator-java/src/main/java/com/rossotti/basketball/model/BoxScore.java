package com.rossotti.basketball.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table (name="boxScore")
public class BoxScore {
	public BoxScore() {}

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
	@JoinColumn(name="gameId", referencedColumnName="id", nullable=false)
	private Game game;
	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}

	@ManyToOne()
	@JoinColumn(name="teamId", referencedColumnName="id", nullable=false)
	private Team team;
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}

	@OneToMany(mappedBy="boxScore", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	private List<BoxScorePlayer> boxScorePlayers = new ArrayList<BoxScorePlayer>();
	public List<BoxScorePlayer> getBoxScorePlayers()  {
		return boxScorePlayers;
	}
	public void setBoxScorePlayers(List<BoxScorePlayer> boxScorePlayers)  {
		this.boxScorePlayers = boxScorePlayers;
	}
	public void addBoxScorePlayer(BoxScorePlayer boxScorePlayer)  {
		this.getBoxScorePlayers().add(boxScorePlayer);
	}
	public void removeBoxScorePlayer(BoxScorePlayer boxScorePlayer)  {
		this.getBoxScorePlayers().remove(boxScorePlayer);
	}

	@Enumerated(EnumType.STRING)
	@Column(name="location", length=5, nullable=false)
	private Location location;
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public enum Location {
		Home,
		Away
	}

	@Enumerated(EnumType.STRING)
	@Column(name="result", length=4, nullable=true)
	private Result result;
	public Result getResult() {
		return result;
	}
	public void setResult(Result result) {
		this.result = result;
	}
	public enum Result {
		Win,
		Loss
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

	@Column(name="pointsPeriod1", nullable=true)
	private Short pointsPeriod1;
	public Short getPointsPeriod1() {
		return pointsPeriod1;
	}
	public void setPointsPeriod1(Short pointsPeriod1) {
		this.pointsPeriod1 = pointsPeriod1;
	}

	@Column(name="pointsPeriod2", nullable=true)
	private Short pointsPeriod2;
	public Short getPointsPeriod2() {
		return pointsPeriod2;
	}
	public void setPointsPeriod2(Short pointsPeriod2) {
		this.pointsPeriod2 = pointsPeriod2;
	}

	@Column(name="pointsPeriod3", nullable=true)
	private Short pointsPeriod3;
	public Short getPointsPeriod3() {
		return pointsPeriod3;
	}
	public void setPointsPeriod3(Short pointsPeriod3) {
		this.pointsPeriod3 = pointsPeriod3;
	}

	@Column(name="pointsPeriod4", nullable=true)
	private Short pointsPeriod4;
	public Short getPointsPeriod4() {
		return pointsPeriod4;
	}
	public void setPointsPeriod4(Short pointsPeriod4) {
		this.pointsPeriod4 = pointsPeriod4;
	}

	@Column(name="pointsPeriod5", nullable=true)
	private Short pointsPeriod5;
	public Short getPointsPeriod5() {
		return pointsPeriod5;
	}
	public void setPointsPeriod5(Short pointsPeriod5) {
		this.pointsPeriod5 = pointsPeriod5;
	}

	@Column(name="pointsPeriod6", nullable=true)
	private Short pointsPeriod6;
	public Short getPointsPeriod6() {
		return pointsPeriod6;
	}
	public void setPointsPeriod6(Short pointsPeriod6) {
		this.pointsPeriod6 = pointsPeriod6;
	}

	@Column(name="pointsPeriod7", nullable=true)
	private Short pointsPeriod7;
	public Short getPointsPeriod7() {
		return pointsPeriod7;
	}
	public void setPointsPeriod7(Short pointsPeriod7) {
		this.pointsPeriod7 = pointsPeriod7;
	}

	@Column(name="pointsPeriod8", nullable=true)
	private Short pointsPeriod8;
	public Short getPointsPeriod8() {
		return pointsPeriod8;
	}
	public void setPointsPeriod8(Short pointsPeriod8) {
		this.pointsPeriod8 = pointsPeriod8;
	}

	@Column(name="daysOff", nullable=true)
	private Short daysOff;
	public Short getDaysOff() {
		return daysOff;
	}
	public void setDaysOff(Short daysOff) {
		this.daysOff = daysOff;
	}

	public String toString() {
		return new StringBuffer()
			.append("\n" + this.team.getTeamKey() + "\n")
			.append("  id: " + this.id + "\n")
			.append("  location: " + this.location + "\n")
			.append("  result: " + this.result + "\n")
			.append("  points: " + this.points + "\n")
			.append("  assists: " + this.assists + "\n")
			.append("  turnovers: " + this.turnovers + "\n")
			.append("  steals: " + this.steals + "\n")
			.append("  blocks: " + this.blocks)
			.toString();
	}
}