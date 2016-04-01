insert into team VALUES(1, 'CHI', 'Chicago', 'East', 'Central', 'Chicago', '2009-07-01', 'Chicago Zephyrs', 'Zephyrs', 'Chicago Coliseum', 'IL', 'chicago-zephyrs', '9999-12-31');
insert into team VALUES(2, 'HAR', 'Harlem', 'East', 'Atlantic', 'Harlem', '2009-07-01', 'Harlem Globetrotters', 'Globetrotters', 'Rucker Park', 'NY', 'harlem-globetrotters', '2010-06-30');
insert into team VALUES(3, 'STL', 'St. Louis', 'East', 'Southwest', 'St. Louis', '2009-07-01', 'St. Louis Bombers', 'Bombers', 'St. Louis Arena', 'MO', 'st-louis-bombers', '2010-06-30');
insert into team VALUES(4, 'SAL', 'Salinas', 'West', 'Pacific', 'Salinas', '2010-07-01', 'Salinas Cowboys', 'Cowboys', 'Salinas High School', 'CA', 'salinas-cowboys', '9999-12-31');
insert into team VALUES(5, 'STL', 'Baltimore', 'East', 'Southeast', 'Baltimore', '2005-07-01', 'Baltimore Bullets', 'Bullets', 'Baltimore Coliseum', 'MD', 'baltimore-bullets', '2006-06-30');
insert into team VALUES(6, 'CLE', 'Relels', 'East', 'Central', 'Cleveland', '2010-07-01', 'Cleveland Rebels', 'Rebels', 'Cleveland Arena', 'OH', 'cleveland-rebels', '9999-12-31');
insert into team VALUES(7, 'ROC', 'Rochester', 'East', 'Atlantic', 'Rochester', '2008-07-01', 'Rochester Royals', 'Royals', 'Edgerton Park Arena', 'NY', 'rochester-royals', '2009-06-30');
insert into team VALUES(8, 'STL', 'St. Louis', 'East', 'Southwest', 'St. Louis', '2010-07-01', 'St. Louis Bombers', 'Bombers', 'St. Louis Multiplex', 'MO', 'st-louis-bombers', '2011-06-30');
insert into team VALUES(10, 'DET', 'Detroit', 'East', 'Central', 'Detroit', '2010-07-01', 'Detroit Pistons', 'Pistons', 'Palace of Auburn Hills', 'MI', 'detroit-pistons', '9999-12-31');

insert into official VALUES(1, 'Joe', '2009-07-01', 'LateCall', '96', '2010-06-30');
insert into official VALUES(2, 'Mike', '2009-07-01', 'MissedCall', '97', '2010-06-30');
insert into official VALUES(3, 'Mike', '2010-07-01', 'MissedCall', '98', '2011-06-30');
insert into official VALUES(4, 'Hefe', '2005-07-01', 'QuestionableCall', '99', '2006-06-30');
insert into official VALUES(5, 'Limo', '2005-07-01', 'TerribleCall', '100', '2006-06-30');
insert into official VALUES(10, 'Zach', '2010-10-30', 'Zarba', '28', '9999-12-31');
insert into official VALUES(11, 'Brian', '2010-04-25', 'Forte', '100', '9999-12-31');
insert into official VALUES(12, 'Eli', '2010-11-05', 'Roe', '45', '9999-12-31');

insert into player VALUES(1, '2002-02-20', 'Sacramento, CA, USA', 'Luke Puzdrakiewicz', 'Luke', 78, 'Puzdrakiewicz', 175);
insert into player VALUES(2, '1966-06-02', 'Sacramento, CA, USA', 'Thad Puzdrakiewicz', 'Thad', 81, 'Puzdrakiewicz', 210);
insert into player VALUES(3, '2000-03-13', 'Sacramento, CA, USA', 'Thad Puzdrakiewicz', 'Thad', 79, 'Puzdrakiewicz', 180);
insert into player VALUES(4, '1969-09-08', 'Sacramento, CA, USA', 'Michelle Puzdrakiewicz', 'Michelle', 75, 'Puzdrakiewicz', 170);
insert into player VALUES(5, '1966-06-10', 'Sacramento, CA, USA', 'Junior Puzdrakiewicz', 'Junior', 80, 'Puzdrakiewicz', 240);
insert into player VALUES(10, '1990-03-04', 'Saginaw, Michigan, USA', 'Andre Drummond', 'Andre', 79, 'Drummond', 230);
insert into player VALUES(11, '1989-09-02', 'Philadelphia, Pennsylvania, USA', 'Marcus Morris', 'Marcus', 81, 'Morris', 235);
insert into player VALUES(12, '1993-02-18', 'Thomaston, Georgia, USA', 'Kentavious Caldwell-Pope', 'Kentavious', 78, 'Caldwell-Pope', 204);
insert into player VALUES(13, '1990-04-16', 'Pordenone, Italy', 'Reggie Jackson', 'Reggie', 75, 'Jackson', 208);

insert into rosterPlayer (id, playerId, teamId, fromDate, toDate, number, position) VALUES(1, 1, 1, '2009-11-30', '9999-12-31', '21', 'SG');
insert into rosterPlayer (id, playerId, teamId, fromDate, toDate, number, position) VALUES(2, 1, 4, '2009-10-30', '2009-11-03', '21', 'PG');
insert into rosterPlayer (id, playerId, teamId, fromDate, toDate, number, position) VALUES(3, 2, 4, '2009-10-30', '2009-11-04', '12', 'C');
insert into rosterPlayer (id, playerId, teamId, fromDate, toDate, number, position) VALUES(4, 3, 5, '2009-10-30', '9999-12-31', '9', 'PG');
insert into rosterPlayer (id, playerId, teamId, fromDate, toDate, number, position) VALUES(10, 10, 10, '2015-11-15', '9999-12-31', '21', 'SG');
insert into rosterPlayer (id, playerId, teamId, fromDate, toDate, number, position) VALUES(11, 11, 10, '2015-10-30', '9999-12-31', '28', 'PG');
insert into rosterPlayer (id, playerId, teamId, fromDate, toDate, number, position) VALUES(12, 12, 10, '2015-10-30', '2015-12-01', '12', 'C');
insert into rosterPlayer (id, playerId, teamId, fromDate, toDate, number, position) VALUES(13, 13, 10, '2015-10-30', '9999-12-31', '9', 'PG');

insert into game VALUES(1, '2015-10-27 20:00:00.0', 'Regular', 'Completed');
insert into gameOfficial (id, gameId, officialId) VALUES(1, 1, 2);
insert into gameOfficial (id, gameId, officialId) VALUES(2, 1, 3);
insert into gameOfficial (id, gameId, officialId) VALUES(3, 1, 4);
insert into boxScore (id, gameId, teamId, location, result, points) VALUES(1, 1, 1, 'Home', 'Win', 114);
insert into boxScorePlayer(id, boxScoreId, rosterPlayerId, position, starter, points) VALUES(1, 1, 1, 'SG', true, 12);
insert into boxScore (id, gameId, teamId, location, result, points) VALUES(2, 1, 2, 'Away', 'Loss', 98);
insert into boxScorePlayer(id, boxScoreId, rosterPlayerId, position, starter, points) VALUES(2, 2, 2, 'PG', false, 0);
insert into boxScorePlayer(id, boxScoreId, rosterPlayerId, position, starter, points) VALUES(3, 2, 3, 'C', true, 5);

insert into game VALUES(2, '2015-10-27 21:00:00.0', 'Regular', 'Scheduled');
insert into boxScore (id, gameId, teamId, location) VALUES(3, 2, 4, 'Home');
insert into boxScore (id, gameId, teamId, location) VALUES(4, 2, 5, 'Away');

insert into game VALUES(3, '2015-10-27 20:30:00.0', 'Regular', 'Scheduled');
insert into boxScore (id, gameId, teamId, location) VALUES(5, 3, 5, 'Home');
insert into boxScore (id, gameId, teamId, location) VALUES(6, 3, 6, 'Away');

insert into game VALUES(4, '2015-10-28 20:00:00.0', 'Regular', 'Scheduled');
insert into boxScore (id, gameId, teamId, location) VALUES(7, 4, 1, 'Home');
insert into boxScore (id, gameId, teamId, location) VALUES(8, 4, 3, 'Away');

insert into game VALUES(5, '2015-10-29 20:00:00.0', 'Regular', 'Postponed');
insert into boxScore (id, gameId, teamId, location) VALUES(9, 5, 5, 'Home');
insert into boxScore (id, gameId, teamId, location) VALUES(10, 5, 3, 'Away');

insert into game VALUES(6, '2015-10-30 10:00:00.0', 'Regular', 'Scheduled');
insert into boxScore (id, gameId, teamId, location) VALUES(11, 6, 5, 'Home');
insert into boxScore (id, gameId, teamId, location) VALUES(12, 6, 3, 'Away');

insert into game VALUES(7, '2015-10-15 10:00:00.0', 'Regular', 'Scheduled');
insert into boxScore (id, gameId, teamId, location) VALUES(13, 7, 6, 'Home');
insert into boxScore (id, gameId, teamId, location) VALUES(14, 7, 5, 'Away');

insert into standing (id, teamId, standingDate, rank, ordinalRank, gamesWon, gamesLost, streak, streakType, streakTotal, gamesBack, pointsFor, pointsAgainst, homeWins, homeLosses, 
awayWins, awayLosses, conferenceWins, conferenceLosses, lastFive, lastTen, gamesPlayed, pointsScoredPerGame, PointsAllowedPerGame, WinPercentage, PointDifferential, pointDifferentialPerGame) 
VALUES(1, 1, '2015-10-30', 1, '1st', 1, 0, 1, 'win', 1, 0, 114, 98, 1, 0, 0, 0, 0, 0, '1-0', '1-0', 1, 114, 98, 1.00, 16, 16);

insert into standing (id, teamId, standingDate, rank, ordinalRank, gamesWon, gamesLost, streak, streakType, streakTotal, gamesBack, pointsFor, pointsAgainst, homeWins, homeLosses, 
awayWins, awayLosses, conferenceWins, conferenceLosses, lastFive, lastTen, gamesPlayed, pointsScoredPerGame, PointsAllowedPerGame, WinPercentage, PointDifferential, pointDifferentialPerGame) 
VALUES(2, 3, '2015-10-30', 3, '3rd', 1, 0, 1, 'win', 1, 0, 114, 98, 1, 0, 0, 0, 0, 0, '1-0', '1-0', 1, 114, 98, 1.00, 16, 16);

insert into standing (id, teamId, standingDate, rank, ordinalRank, gamesWon, gamesLost, streak, streakType, streakTotal, gamesBack, pointsFor, pointsAgainst, homeWins, homeLosses, 
awayWins, awayLosses, conferenceWins, conferenceLosses, lastFive, lastTen, gamesPlayed, pointsScoredPerGame, PointsAllowedPerGame, WinPercentage, PointDifferential, pointDifferentialPerGame) 
VALUES(3, 3, '2015-10-31', 5, '5th', 1, 0, 1, 'win', 1, 0, 114, 98, 1, 0, 0, 0, 0, 0, '1-0', '1-0', 1, 114, 98, 1.00, 16, 16);

insert into standing (id, teamId, standingDate, rank, ordinalRank, gamesWon, gamesLost, streak, streakType, streakTotal, gamesBack, pointsFor, pointsAgainst, homeWins, homeLosses, 
awayWins, awayLosses, conferenceWins, conferenceLosses, lastFive, lastTen, gamesPlayed, pointsScoredPerGame, PointsAllowedPerGame, WinPercentage, PointDifferential, pointDifferentialPerGame) 
VALUES(4, 4, '2015-10-31', 17, '17th', 1, 0, 1, 'win', 1, 0, 114, 98, 1, 0, 0, 0, 0, 0, '1-0', '1-0', 1, 114, 98, 1.00, 16, 16);
