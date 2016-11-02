package com.rossotti.basketball.dao.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.rossotti.basketball.client.dto.BoxScoreDTO;
import com.rossotti.basketball.dao.pub.PubBoxScore;
import com.rossotti.basketball.dao.pub.PubBoxScorePlayer;

import javax.persistence.*;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="boxScore")
public class BoxScore {
	public BoxScore() {}

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
	@JoinColumn(name="gameId", referencedColumnName="id", nullable=false)
	@JsonBackReference(value="boxScore-to-game")
	private Game game;
	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}

	@ManyToOne()
	@JoinColumn(name="teamId", referencedColumnName="id", nullable=false)
	@JsonBackReference(value="boxScore-to-team")
	private Team team;
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}

	@OneToMany(mappedBy="boxScore", fetch = FetchType.LAZY, cascade= CascadeType.ALL)
	private List<BoxScorePlayer> boxScorePlayers = new ArrayList<BoxScorePlayer>();
	public List<BoxScorePlayer> getBoxScorePlayers()  {
		return boxScorePlayers;
	}
	@JsonManagedReference(value="boxScorePlayer-to-boxScore")
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
	@Column(name="result", length=4)
	private Result result;
	public Result getResult() {
		return result;
	}
	public void setResult(Result result) {
		this.result = result;
	}
	public enum Result {
		Win,
		Loss,
		Inc
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

	@Column(name="pointsPeriod1")
	private Short pointsPeriod1;
	public Short getPointsPeriod1() {
		return pointsPeriod1;
	}
	public void setPointsPeriod1(Short pointsPeriod1) {
		this.pointsPeriod1 = pointsPeriod1;
	}

	@Column(name="pointsPeriod2")
	private Short pointsPeriod2;
	public Short getPointsPeriod2() {
		return pointsPeriod2;
	}
	public void setPointsPeriod2(Short pointsPeriod2) {
		this.pointsPeriod2 = pointsPeriod2;
	}

	@Column(name="pointsPeriod3")
	private Short pointsPeriod3;
	public Short getPointsPeriod3() {
		return pointsPeriod3;
	}
	public void setPointsPeriod3(Short pointsPeriod3) {
		this.pointsPeriod3 = pointsPeriod3;
	}

	@Column(name="pointsPeriod4")
	private Short pointsPeriod4;
	public Short getPointsPeriod4() {
		return pointsPeriod4;
	}
	public void setPointsPeriod4(Short pointsPeriod4) {
		this.pointsPeriod4 = pointsPeriod4;
	}

	@Column(name="pointsPeriod5")
	private Short pointsPeriod5;
	public Short getPointsPeriod5() {
		return pointsPeriod5;
	}
	public void setPointsPeriod5(Short pointsPeriod5) {
		this.pointsPeriod5 = pointsPeriod5;
	}

	@Column(name="pointsPeriod6")
	private Short pointsPeriod6;
	public Short getPointsPeriod6() {
		return pointsPeriod6;
	}
	public void setPointsPeriod6(Short pointsPeriod6) {
		this.pointsPeriod6 = pointsPeriod6;
	}

	@Column(name="pointsPeriod7")
	private Short pointsPeriod7;
	public Short getPointsPeriod7() {
		return pointsPeriod7;
	}
	public void setPointsPeriod7(Short pointsPeriod7) {
		this.pointsPeriod7 = pointsPeriod7;
	}

	@Column(name="pointsPeriod8")
	private Short pointsPeriod8;
	public Short getPointsPeriod8() {
		return pointsPeriod8;
	}
	public void setPointsPeriod8(Short pointsPeriod8) {
		this.pointsPeriod8 = pointsPeriod8;
	}

	@Column(name="daysOff")
	private Short daysOff;
	public Short getDaysOff() {
		return daysOff;
	}
	public void setDaysOff(Short daysOff) {
		this.daysOff = daysOff;
	}

	public String toString() {
		return ("\n" + this.team.getTeamKey() + "\n") +
				"  id: " + this.id + "\n" +
				"  location: " + this.location + "\n" +
				"  result: " + this.result + "\n" +
				"  points: " + this.points + "\n" +
				"  assists: " + this.assists + "\n" +
				"  turnovers: " + this.turnovers + "\n" +
				"  steals: " + this.steals + "\n" +
				"  blocks: " + this.blocks;
	}

	public PubBoxScore toPubBoxScore(UriInfo uriInfo) {
		List<PubBoxScorePlayer> listPubBoxScorePlayer = new ArrayList<PubBoxScorePlayer>();
		if (this.getBoxScorePlayers().size() > 0) {
			for (BoxScorePlayer boxScorePlayer : this.getBoxScorePlayers()) {
				PubBoxScorePlayer pubBoxScorePlayer = boxScorePlayer.toPubBoxScorePlayer(uriInfo);
				listPubBoxScorePlayer.add(pubBoxScorePlayer);
			}
		}
		if (this.getResult() == null) {
			return new PubBoxScore(
					this.getLocation(),
					this.getResult(),
					this.getTeam().toPubTeam(uriInfo));
		}
		else {
			return new PubBoxScore(
					this.getLocation(),
					this.getResult(),
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
					this.getPointsPeriod1(),
					this.getPointsPeriod2(),
					this.getPointsPeriod3(),
					this.getPointsPeriod4(),
					this.getPointsPeriod5(),
					this.getPointsPeriod6(),
					this.getPointsPeriod7(),
					this.getPointsPeriod8(),
					this.getDaysOff(),
					listPubBoxScorePlayer,
					this.getTeam().toPubTeam(uriInfo));
		}
	}

	public void updateTotals(BoxScoreDTO stats) {
		this.setMinutes(stats.getMinutes());
		this.setPoints(stats.getPoints());
		this.setAssists(stats.getAssists());
		this.setTurnovers(stats.getTurnovers());
		this.setSteals(stats.getSteals());
		this.setBlocks(stats.getBlocks());
		this.setFieldGoalAttempts(stats.getField_goals_attempted());
		this.setFieldGoalMade(stats.getField_goals_made());
		this.setFieldGoalPercent(stats.getField_goal_percentage());
		this.setThreePointAttempts(stats.getThree_point_field_goals_attempted());
		this.setThreePointMade(stats.getThree_point_field_goals_made());
		this.setThreePointPercent(stats.getThree_point_percentage());
		this.setFreeThrowAttempts(stats.getFree_throws_attempted());
		this.setFreeThrowMade(stats.getFree_throws_made());
		this.setFreeThrowPercent(stats.getFree_throw_percentage());
		this.setReboundsOffense(stats.getOffensive_rebounds());
		this.setReboundsDefense(stats.getDefensive_rebounds());
		this.setPersonalFouls(stats.getPersonal_fouls());
	}

	public void updatePeriodScores(int[] periodScores) {
		this.setPointsPeriod1((short)periodScores[0]);
		this.setPointsPeriod2((short)periodScores[1]);
		this.setPointsPeriod3((short)periodScores[2]);
		this.setPointsPeriod4((short)periodScores[3]);
		if(periodScores.length > 4)
			this.setPointsPeriod5((short)periodScores[4]);
		if(periodScores.length > 5)
			this.setPointsPeriod6((short)periodScores[5]);
		if(periodScores.length > 6)
			this.setPointsPeriod7((short)periodScores[6]);
		if(periodScores.length > 7)
			this.setPointsPeriod8((short)periodScores[7]);
	}
}