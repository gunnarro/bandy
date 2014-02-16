-- tables
CREATE TABLE android_metadata (locale TEXT)	
CREATE TABLE sqlite_sequence(name,seq)	
CREATE TABLE playermatchlnk(_id INTEGER PRIMARY KEY AUTOINCREMENT,fk_player_id INTEGER NOT NULL,fk_match_id INTEGER NOT NULL)	
CREATE TABLE clubs(_id INTEGER PRIMARY KEY AUTOINCREMENT,club_name TEXT NOT NULL UNIQUE)	
CREATE TABLE contacts(_id INTEGER PRIMARY KEY AUTOINCREMENT,fk_team_id INTEGER,first_name TEXT NOT NULL,middle_name TEXT NOT NULL,last_name TEXT NOT NULL,mobile TEXT NOT NULL,email TEXT NOT NULL,UNIQUE (first_name,last_name) ON CONFLICT REPLACE)	
CREATE TABLE cups(_id INTEGER PRIMARY KEY AUTOINCREMENT,start_date INTEGER,cup_name TEXT NOT NULL UNIQUE,club_name TEXT NOT NULL,venue TEXT NOT NULL,deadline_date INTEGER)	
CREATE TABLE matches(_id INTEGER PRIMARY KEY AUTOINCREMENT,fk_team_id INTEGER NOT NULL,start_date INTEGER NOT NULL,home_team TEXT NOT NULL,away_team TEXT NOT NULL,venue TEXT NOT NULL,referee TEXT, FOREIGN KEY(fk_team_id) REFERENCES teams(_id),UNIQUE (home_team,start_date) ON CONFLICT REPLACE)	
CREATE TABLE players(_id INTEGER PRIMARY KEY AUTOINCREMENT,fk_team_id INTEGER,status TEXT NOT NULL,first_name TEXT NOT NULL,middle_name TEXT,last_name TEXT NOT NULL,date_of_birth INTEGER,email TEXT,mobile TEXT,UNIQUE (first_name,last_name) ON CONFLICT REPLACE)	
CREATE TABLE player_contact_lnk(_id INTEGER PRIMARY KEY AUTOINCREMENT,fk_player_id INTEGER NOT NULL,fk_contact_id INTEGER NOT NULL,UNIQUE (fk_player_id,fk_contact_id) ON CONFLICT REPLACE)	
CREATE TABLE player_cup_lnk(_id INTEGER PRIMARY KEY AUTOINCREMENT,fk_player_id INTEGER NOT NULL,fk_cup_id INTEGER NOT NULL,UNIQUE (fk_player_id,fk_cup_id) ON CONFLICT REPLACE)	
CREATE TABLE player_match_lnk(_id INTEGER PRIMARY KEY AUTOINCREMENT,fk_player_id INTEGER NOT NULL,fk_match_id INTEGER NOT NULL,UNIQUE (fk_player_id,fk_match_id) ON CONFLICT REPLACE)	
CREATE TABLE player_training_lnk(_id INTEGER PRIMARY KEY AUTOINCREMENT,fk_player_id INTEGER NOT NULL,fk_training_id INTEGER NOT NULL,UNIQUE (fk_player_id,fk_training_id) ON CONFLICT REPLACE)	
CREATE TABLE roles(_id INTEGER PRIMARY KEY AUTOINCREMENT,role TEXT NOT NULL UNIQUE,fk_contact_id INTEGER NOT NULL)	
CREATE TABLE settings(_id INTEGER PRIMARY KEY AUTOINCREMENT,key TEXT NOT NULL UNIQUE,value TEXT NOT NULL)	
CREATE TABLE teams(_id INTEGER PRIMARY KEY AUTOINCREMENT,fk_club_id INTEGER NOT NULL,team_name TEXT NOT NULL UNIQUE, FOREIGN KEY(fk_club_id) REFERENCES clubs(_id))	
CREATE TABLE trainings(_id INTEGER PRIMARY KEY AUTOINCREMENT,fk_team_id INTEGER NOT NULL,start_date INTEGER,end_time INTEGER,place TEXT NOT NULL,UNIQUE (fk_team_id,start_date) ON CONFLICT REPLACE)	
CREATE TABLE notifications(_id INTEGER PRIMARY KEY AUTOINCREMENT,created_date TEXT NOT NULL,sent_date TEXT NOT NULL,recipients TEXT NOT NULL,subject TEXT NOT NULL,message TEXT NOT NULL,status INTEGER)	
 
-- indexes
 index	sqlite_autoindex_clubs_1				clubs				12	null	
 index	sqlite_autoindex_contacts_1				contacts			14	null	
 index	sqlite_autoindex_cups_1					cups				16	null	
 index	sqlite_autoindex_matches_1				matches				27	null	
 index	sqlite_autoindex_players_1				players				20	null	
 index	sqlite_autoindex_player_contact_lnk_1	player_contact_lnk	29	null	
 index	sqlite_autoindex_player_cup_lnk_1		player_cup_lnk		30	null	
 index	sqlite_autoindex_player_match_lnk_1		player_match_lnk	23	null	
 index	sqlite_autoindex_player_training_lnk_1	player_training_lnk	25	null	
 index	sqlite_autoindex_roles_1				roles				26	null	
 index	sqlite_autoindex_settings_1				settings			7	null	
 index	sqlite_autoindex_teams_1				teams				9	null	
 index	sqlite_autoindex_trainings_1			trainings			10	null	
