insert into team VALUES(1, 'CHI', 'Chicago', 'East', 'Central', 'Chicago', '2009-07-01', 'Chicago Zephyrs', 'Zephyrs', 'Chicago Coliseum', 'IL', 'chicago-zephyrs', '9999-12-31');
insert into team VALUES(2, 'HAR', 'Harlem', 'East', 'Atlantic', 'Harlem', '2009-07-01', 'Harlem Globetrotters', 'Globetrotters', 'Rucker Park', 'NY', 'harlem-globetrotters', '2010-06-30');
insert into team VALUES(3, 'STL', 'St. Louis', 'East', 'Southwest', 'St. Louis', '2009-07-01', 'St. Louis Bombers', 'Bombers', 'St. Louis Arena', 'MO', 'st-louis-bombers', '2010-06-30');
insert into team VALUES(4, 'SAL', 'Salinas', 'West', 'Pacific', 'Salinas', '2010-07-01', 'Salinas Cowboys', 'Cowboys', 'Salinas High School', 'CA', 'salinas-cowboys', '9999-12-31');
insert into team VALUES(5, 'STL', 'Baltimore', 'East', 'Southeast', 'Baltimore', '2005-07-01', 'Baltimore Bullets', 'Bullets', 'Baltimore Coliseum', 'MD', 'baltimore-bullets', '2006-06-30');
insert into team VALUES(6, 'CLE', 'Relels', 'East', 'Central', 'Cleveland', '2010-07-01', 'Cleveland Rebels', 'Rebels', 'Cleveland Arena', 'OH', 'cleveland-rebels', '9999-12-31');
insert into team VALUES(7, 'ROC', 'Rochester', 'East', 'Atlantic', 'Rochester', '2008-07-01', 'Rochester Royals', 'Royals', 'Edgerton Park Arena', 'NY', 'rochester-royals', '2009-06-30');
insert into team VALUES(8, 'STL', 'St. Louis', 'East', 'Southwest', 'St. Louis', '2010-07-01', 'St. Louis Bombers', 'Bombers', 'St. Louis Multiplex', 'MO', 'st-louis-bombers', '2011-06-30');

insert into official VALUES(1, 'Joe', '2009-07-01', 'LateCall', '96', '2010-06-30');
insert into official VALUES(2, 'Mike', '2009-07-01', 'MissedCall', '97', '2010-06-30');
insert into official VALUES(3, 'Mike', '2010-07-01', 'MissedCall', '98', '2011-06-30');
insert into official VALUES(4, 'Hefe', '2005-07-01', 'QuestionableCall', '99', '2006-06-30');
insert into official VALUES(5, 'Limo', '2005-07-01', 'TerribleCall', '100', '2006-06-30');

insert into player VALUES(1, '2002-02-20', 'Sacramento, CA, USA', 'Luke Puzdrakiewicz', 'Luke', 78, 'Puzdrakiewicz', 175);
insert into player VALUES(2, '1966-06-02', 'Sacramento, CA, USA', 'Thad Puzdrakiewicz', 'Thad', 81, 'Puzdrakiewicz', 210);
insert into player VALUES(3, '2000-03-13', 'Sacramento, CA, USA', 'Thad Puzdrakiewicz', 'Thad', 79, 'Puzdrakiewicz', 180);
insert into player VALUES(4, '1969-09-08', 'Sacramento, CA, USA', 'Michelle Puzdrakiewicz', 'Michelle', 75, 'Puzdrakiewicz', 170);
insert into player VALUES(5, '1966-06-10', 'Sacramento, CA, USA', 'Junior Puzdrakiewicz', 'Junior', 80, 'Puzdrakiewicz', 240);

insert into rosterPlayer VALUES(1, '2009-11-30', '21', 'SG', '9999-12-31', 1, 1);
insert into rosterPlayer VALUES(2, '2009-10-30', '21', 'PG', '2009-11-03', 1, 1);
insert into rosterPlayer VALUES(3, '2009-10-30', '12', 'C', '2009-11-04', 2, 1);

insert into game VALUES(1, '2015-10-27 20:00:00.0', 'Regular', 'Completed');
insert into boxScore (id, location, result, gameId, teamId) VALUES(1, 'Home', 'Win', 1, 1);
insert into boxScore (id, location, result, gameId, teamId) VALUES(2, 'Away', 'Loss', 1, 2);

insert into game VALUES(2, '2015-10-27 21:00:00.0', 'Regular', 'Completed');
insert into boxScore (id, location, gameId, teamId) VALUES(3, 'Home', 2, 3);
insert into boxScore (id, location, gameId, teamId) VALUES(4, 'Away', 2, 4);

insert into game VALUES(3, '2015-10-27 20:30:00.0', 'Regular', 'Scheduled');
insert into boxScore (id, location, gameId, teamId) VALUES(5, 'Home', 3, 5);
insert into boxScore (id, location, gameId, teamId) VALUES(6, 'Away', 3, 6);

insert into game VALUES(4, '2015-10-28 20:00:00.0', 'Regular', 'Scheduled');
insert into boxScore (id, location, gameId, teamId) VALUES(7, 'Home', 4, 1);
insert into boxScore (id, location, gameId, teamId) VALUES(8, 'Away', 4, 3);

insert into game VALUES(5, '2015-10-29 20:00:00.0', 'Regular', 'Postponed');
insert into boxScore (id, location, gameId, teamId) VALUES(9, 'Home', 5, 5);
insert into boxScore (id, location, gameId, teamId) VALUES(10, 'Away', 5, 3);

insert into game VALUES(6, '2015-10-30 10:00:00.0', 'Regular', 'Scheduled');
insert into boxScore (id, location, gameId, teamId) VALUES(11, 'Home', 6, 5);
insert into boxScore (id, location, gameId, teamId) VALUES(12, 'Away', 6, 3);