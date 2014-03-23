Database: sportsteam3.db (C:/code/git/bandy-master/database/sportsteam3.db, SQLite 3)


Table: clubs

Table: trainings

Table: teams

Table: matches

Table: players

Table: android_metadata

Table: addresses

Table: contacts

Table: cups

Table: cup_match_lnk

Table: leagues

Table: league_match_lnk

Table: league_team_lnk

Table: match_types

Table: player_contact_lnk

Table: player_cup_lnk

Table: player_match_lnk

Table: position_types

Table: player_training_lnk

Table: roles

Table: seasons

Table: settings

Table: statuses

Table: notifications

View: match_result_view
-----------------------------------------
CREATE VIEW match_result_view AS
SELECT m.fk_season_id AS season_id,m.fk_team_id AS team_id,m.match_type_id AS match_type_id,m.home_team AS team_name,count(m.fk_team_id)AS numberOfPlayedMatches,sum(goals_home_team>goals_away_team)AS numberOfWonMatches,sum(goals_home_team=goals_away_team)AS numberOfDrawMatches,sum(goals_home_team<goals_away_team)AS numberOfLossMatches,sum(m.goals_home_team)AS scored,sum(m.goals_away_team)AS against
       FROM matches m,teams t
       WHERE m.home_team LIKE t.team_name
       GROUP BY m.fk_season_id,m.fk_team_id,m.match_type_id,m.home_team
       UNION
       SELECT m.fk_season_id AS season_id,m.fk_team_id AS team_id,m.match_type_id AS match_type_id,m.away_team AS team_name,count(m.fk_team_id)AS numberOfPlayedMatches,sum(goals_away_team>goals_home_team)AS numberOfWonMatches,sum(goals_away_team=goals_home_team)AS numberOfDrawMatches,sum(goals_away_team<goals_home_team)AS numberOfLossMatches,sum(m.goals_away_team)AS scored,sum(m.goals_home_team)AS against
       FROM matches m,teams t
       WHERE m.away_team LIKE t.team_name
       GROUP BY m.fk_season_id,m.fk_team_id,m.match_type_id,m.away_team;

untry TEXT NOT NULL,UNIQUE(street_name,street_number,street_number_postfix,zip_code) ON conflict ABORT);
CREATE TABLE contacts(_id INTEGER PRIMARY KEY autoincrement,created_date_time datetime DEFAULT CURRENT_TIMESTAMP,fk_address_id INTEGER,fk_team_id INTEGER,first_name TEXT NOT NULL,middle_name TEXT NOT NULL,last_name TEXT NOT NULL,mobile TEXT NOT NULL,email TEXT NOT NULL,UNIQUE(first_name,last_name) ON conflict ABORT);
CREATE TABLE cups(_id INTEGER PRIMARY KEY autoincrement,created_date_time datetime DEFAULT CURRENT_TIMESTAMP,fk_season_id INTEGER NOT NULL DEFAULT 1,start_date INTEGER,cup_name TEXT NOT NULL UNIQUE,club_name TEXT NOT NULL,venue TEXT NOT NULL,deadline_date INTEGER);
CREATE TABLE cup_match_lnk(_id INTEGER PRIMARY KEY autoincrement,created_date_time datetime DEFAULT CURRENT_TIMESTAMP,fk_cup_id INTEGER NOT NULL,fk_match_id INTEGER NOT NULL,UNIQUE(fk_cup_id,fk_match_id) ON conflict ABORT,FOREIGN KEY(fk_cup_id)REFERENCES players(_id),FOREIGN KEY(fk_match_id)REFERENCES matches(_id));
CREATE TABLE leagues(_id INTEGER PRIMARY KEY autoincrement,created_date_time datetime DEFAULT CURRENT_TIMESTAMP,league_name TEXT,league_player_age_min INTEGER,league_player_age_max INTEGER,league_gender TEXT,league_match_period_time_minutes INTEGER,league_match_extra_period_time_minutes INTEGER,league_number_of_players INTEGER,league_description TEXT);
CREATE TABLE league_match_lnk(_id INTEGER PRIMARY KEY autoincrement,created_date_time datetime DEFAULT CURRENT_TIMESTAMP,fk_league_id INTEGER NOT NULL,fk_match_id INTEGER NOT NULL,UNIQUE(fk_league_id,fk_match_id) ON conflict ABORT, FOREIGN KEY(fk_league_id)REFERENCES players(_id),FOREIGN KEY(fk_match_id)REFERENCES cups(_id));
CREATE TABLE league_team_lnk(_id INTEGER PRIMARY KEY autoincrement,created_date_time datetime DEFAULT CURRENT_TIMESTAMP,fk_league_id INTEGER NOT NULL,fk_team_id INTEGER NOT NULL,UNIQUE(fk_league_id,fk_team_id) ON conflict ABORT, FOREIGN KEY(fk_league_id)REFERENCES players(_id),FOREIGN KEY(fk_team_id)REFERENCES cups(_id));
CREATE TABLE match_types(_id INTEGER PRIMARY KEY autoincrement,created_date_time datetime DEFAULT CURRENT_TIMESTAMP,match_type_id INTEGER,match_type_name TEXT NOT NULL);
CREATE TABLE player_contact_lnk(_id INTEGER PRIMARY KEY autoincrement,created_date_time datetime DEFAULT CURRENT_TIMESTAMP,fk_player_id INTEGER NOT NULL,fk_contact_id INTEGER NOT NULL,UNIQUE(fk_player_id,fk_contact_id) ON conflict ABORT,FOREIGN KEY(fk_player_id)REFERENCES players(_id),FOREIGN KEY(fk_contact_id)REFERENCES contacts(_id));
CREATE TABLE player_cup_lnk(_id INTEGER PRIMARY KEY autoincrement,created_date_time datetime DEFAULT CURRENT_TIMESTAMP,fk_player_id INTEGER NOT NULL,fk_cup_id INTEGER NOT NULL,UNIQUE(fk_player_id,fk_cup_id) ON conflict ABORT,FOREIGN KEY(fk_player_id)REFERENCES players(_id),FOREIGN KEY(fk_cup_id)REFERENCES cups(_id));
CREATE TABLE player_match_lnk(_id INTEGER PRIMARY KEY autoincrement,created_date_time datetime DEFAULT CURRENT_TIMESTAMP,fk_player_id INTEGER NOT NULL,fk_match_id INTEGER NOT NULL,UNIQUE(fk_player_id,fk_match_id) ON conflict ABORT,FOREIGN KEY(fk_player_id)REFERENCES players(_id),FOREIGN KEY(fk_match_id)REFERENCES matches(_id));
CREATE TABLE position_types(_id INTEGER PRIMARY KEY autoincrement,created_date_time datetime DEFAULT CURRENT_TIMESTAMP,position_type_id INTEGER,position_type_name TEXT NOT NULL);
CREATE TABLE player_training_lnk(_id INTEGER PRIMARY KEY autoincrement,created_date_time datetime DEFAULT CURRENT_TIMESTAMP,fk_player_id INTEGER NOT NULL,fk_training_id INTEGER NOT NULL,UNIQUE(fk_player_id,fk_training_id) ON conflict ABORT,FOREIGN KEY(fk_player_id)REFERENCES players(_id),FOREIGN KEY(fk_training_id)REFERENCES trainings(_id));
CREATE TABLE roles(_id INTEGER PRIMARY KEY autoincrement,created_date_time datetime DEFAULT CURRENT_TIMESTAMP,ROLE TEXT NOT NULL UNIQUE,fk_contact_id INTEGER NOT NULL);
CREATE TABLE seasons(_id INTEGER PRIMARY KEY autoincrement,created_date_time datetime DEFAULT CURRENT_TIMESTAMP,period string NOT NULL,start_date INTEGER NOT NULL,end_date INTEGER NOT NULL,UNIQUE(period,start_date) ON conflict ABORT);
CREATE TABLE settings(_id INTEGER PRIMARY KEY autoincrement,created_date_time datetime DEFAULT CURRENT_TIMESTAMP,[key] TEXT NOT NULL UNIQUE,value TEXT NOT NULL);
CREATE TABLE statuses(_id INTEGER PRIMARY KEY autoincrement,created_date_time datetime DEFAULT CURRENT_TIMESTAMP,status_id INTEGER NOT NULL UNIQUE,status_name TEXT NOT NULL);
CREATE TABLE notifications(_id INTEGER PRIMARY KEY autoincrement,created_date_time datetime DEFAULT CURRENT_TIMESTAMP,sent_date TEXT NOT NULL,recipients TEXT NOT NULL,subject TEXT NOT NULL,message TEXT NOT NULL,status INTEGER);

