
-- Table: clubs
CREATE TABLE clubs(_id               INTEGER  PRIMARY KEY AUTOINCREMENT,created_date_time DATETIME DEFAULT CURRENT_TIMESTAMP,club_name         TEXT     NOT NULL
                           UNIQUE);

INSERT INTO [clubs] ([_id], [created_date_time], [club_name]) VALUES (1, '2014-03-20 22:02:21', 'Ullevål IL - Bandy');

-- Table: trainings
CREATE TABLE trainings(_id               INTEGER  PRIMARY KEY AUTOINCREMENT,created_date_time DATETIME DEFAULT CURRENT_TIMESTAMP,fk_season_id      INTEGER  NOT NULL
                           DEFAULT 1,fk_team_id        INTEGER  NOT NULL,start_date        INTEGER,end_time          INTEGER,place             TEXT     NOT NULL,UNIQUE(fk_team_id,start_date) ON CONFLICT ABORT);

INSERT INTO [trainings] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [end_time], [place]) VALUES (1, '2014-03-20 22:02:42', 1, 1, 1381941000, 64800, 'Bergbanen');
INSERT INTO [trainings] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [end_time], [place]) VALUES (2, '2014-03-20 22:02:42', 1, 1, 1382027400, 64800, 'Bergbanen');
INSERT INTO [trainings] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [end_time], [place]) VALUES (3, '2014-03-20 22:02:42', 1, 1, 1382459400, 64800, 'Bergbanen');
INSERT INTO [trainings] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [end_time], [place]) VALUES (4, '2014-03-20 22:02:42', 1, 1, 1382632200, 64800, 'Bergbanen');
INSERT INTO [trainings] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [end_time], [place]) VALUES (5, '2014-03-20 22:02:42', 1, 1, 1383064200, 64800, 'Bergbanen');
INSERT INTO [trainings] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [end_time], [place]) VALUES (6, '2014-03-20 22:02:43', 1, 1, 1383150600, 64800, 'Bergbanen');
INSERT INTO [trainings] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [end_time], [place]) VALUES (7, '2014-03-20 22:02:43', 1, 1, 1383237000, 64800, 'Bergbanen');
INSERT INTO [trainings] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [end_time], [place]) VALUES (8, '2014-03-20 22:02:43', 1, 1, 1383582600, 64800, 'Bergbanen');
INSERT INTO [trainings] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [end_time], [place]) VALUES (9, '2014-03-20 22:02:43', 1, 1, 1383755400, 64800, 'Bergbanen');
INSERT INTO [trainings] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [end_time], [place]) VALUES (10, '2014-03-20 22:02:43', 1, 1, 1384360200, 64800, 'Bergbanen');
INSERT INTO [trainings] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [end_time], [place]) VALUES (11, '2014-03-20 22:02:43', 1, 1, 1384792200, 64800, 'Bergbanen');
INSERT INTO [trainings] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [end_time], [place]) VALUES (12, '2014-03-20 22:02:43', 1, 1, 1384965000, 64800, 'Bergbanen');
INSERT INTO [trainings] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [end_time], [place]) VALUES (13, '2014-03-20 22:02:43', 1, 1, 1385397000, 64800, 'Bergbanen');
INSERT INTO [trainings] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [end_time], [place]) VALUES (14, '2014-03-20 22:02:43', 1, 1, 1385569800, 64800, 'Bergbanen');
INSERT INTO [trainings] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [end_time], [place]) VALUES (15, '2014-03-20 22:02:44', 1, 1, 1385821800, 57600, 'Bergbanen');
INSERT INTO [trainings] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [end_time], [place]) VALUES (16, '2014-03-20 22:02:44', 1, 1, 1386001800, 64800, 'Bergbanen');
INSERT INTO [trainings] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [end_time], [place]) VALUES (17, '2014-03-20 22:02:44', 1, 1, 1386174600, 64800, 'Bergbanen');
INSERT INTO [trainings] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [end_time], [place]) VALUES (18, '2014-03-20 22:02:44', 1, 1, 1386606600, 64800, 'Bergbanen');
INSERT INTO [trainings] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [end_time], [place]) VALUES (19, '2014-03-20 22:02:44', 1, 1, 1386779400, 64800, 'Bergbanen');
INSERT INTO [trainings] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [end_time], [place]) VALUES (20, '2014-03-20 22:02:44', 1, 1, 1387211400, 64800, 'Bergbanen');
INSERT INTO [trainings] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [end_time], [place]) VALUES (21, '2014-03-20 22:02:45', 1, 1, 1387384200, 64800, 'Bergbanen');
INSERT INTO [trainings] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [end_time], [place]) VALUES (22, '2014-03-20 22:02:45', 1, 1, 1389025800, 64800, 'Bergbanen');
INSERT INTO [trainings] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [end_time], [place]) VALUES (23, '2014-03-20 22:02:45', 1, 1, 1389198600, 64800, 'Bergbanen');
INSERT INTO [trainings] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [end_time], [place]) VALUES (24, '2014-03-20 22:02:46', 1, 1, 1392136200, 64800, 'Bergbanen');
INSERT INTO [trainings] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [end_time], [place]) VALUES (25, '2014-03-20 22:02:46', 1, 1, 1389630600, 64800, 'Bergbanen');
INSERT INTO [trainings] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [end_time], [place]) VALUES (26, '2014-03-20 22:02:46', 1, 1, 1389803400, 64800, 'Bergbanen');
INSERT INTO [trainings] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [end_time], [place]) VALUES (27, '2014-03-20 22:02:46', 1, 1, 1390062600, 64800, 'Bergbanen');
INSERT INTO [trainings] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [end_time], [place]) VALUES (28, '2014-03-20 22:02:46', 1, 1, 1390235400, 64800, 'Bergbanen');
INSERT INTO [trainings] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [end_time], [place]) VALUES (29, '2014-03-20 22:02:46', 1, 1, 1390408200, 64800, 'Bergbanen');
INSERT INTO [trainings] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [end_time], [place]) VALUES (30, '2014-03-20 22:02:47', 1, 1, 1390753800, 64800, 'Bergbanen');
INSERT INTO [trainings] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [end_time], [place]) VALUES (31, '2014-03-20 22:02:47', 1, 1, 1391617800, 64800, 'Bergbanen');
INSERT INTO [trainings] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [end_time], [place]) VALUES (32, '2014-03-20 22:02:47', 1, 1, 1391869800, 57600, 'Bergbanen');
INSERT INTO [trainings] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [end_time], [place]) VALUES (33, '2014-03-20 22:02:47', 1, 1, 1392049800, 64800, 'Bergbanen');
INSERT INTO [trainings] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [end_time], [place]) VALUES (34, '2014-03-20 22:02:47', 1, 1, 1392222600, 64800, 'Bergbanen');
INSERT INTO [trainings] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [end_time], [place]) VALUES (35, '2014-03-20 22:02:48', 1, 1, 1392474600, 57600, 'Bergbanen');
INSERT INTO [trainings] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [end_time], [place]) VALUES (36, '2014-03-20 22:02:48', 1, 1, 1393259400, 64800, 'Bergbanen');

-- Table: teams
CREATE TABLE teams(_id                INTEGER  PRIMARY KEY AUTOINCREMENT,created_date_time  DATETIME DEFAULT CURRENT_TIMESTAMP,fk_club_id         INTEGER  NOT NULL,team_name          TEXT     NOT NULL
                            UNIQUE,team_year_of_birth INTEGER,team_gender        TEXT     NOT NULL
                            UNIQUE,FOREIGN KEY(fk_club_id)REFERENCES clubs(_id));

INSERT INTO [teams] ([_id], [created_date_time], [fk_club_id], [team_name], [team_year_of_birth], [team_gender]) VALUES (1, '2014-03-20 22:02:22', 1, 'UIL Knøtt 2003', 0, 'MALE');

-- Table: matches
CREATE TABLE matches(_id               INTEGER  PRIMARY KEY AUTOINCREMENT,created_date_time DATETIME DEFAULT CURRENT_TIMESTAMP,fk_season_id      INTEGER  NOT NULL
                           DEFAULT 1,fk_team_id        INTEGER  NOT NULL,start_date        INTEGER  NOT NULL,home_team         TEXT     NOT NULL,away_team         TEXT     NOT NULL,goals_home_team   INTEGER  DEFAULT -1,goals_away_team   INETGER  DEFAULT -1,venue             TEXT     NOT NULL,referee           TEXT,match_type_id     INTEGER,FOREIGN KEY(fk_team_id)REFERENCES teams(_id),UNIQUE(home_team,start_date) ON CONFLICT ABORT);

INSERT INTO [matches] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [home_team], [away_team], [goals_home_team], [goals_away_team], [venue], [referee], [match_type_id]) VALUES (1, '2014-03-20 22:02:40', 1, 1, 1385485200, 'UIL Knøtt 2003', 'Røa 03 2', 4, 4, 'Bergbanen', 'dommer ukjent dommer ukjent', 1);
INSERT INTO [matches] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [home_team], [away_team], [goals_home_team], [goals_away_team], [venue], [referee], [match_type_id]) VALUES (2, '2014-03-20 22:02:40', 1, 1, 1386694800, 'UIL Knøtt 2003', 'Ready 01 4', 5, 2, 'Bergbanen', 'dommer ukjent dommer ukjent', 1);
INSERT INTO [matches] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [home_team], [away_team], [goals_home_team], [goals_away_team], [venue], [referee], [match_type_id]) VALUES (3, '2014-03-20 22:02:40', 1, 1, 1387299600, 'Ready 01 3 Jenter', 'UIL Knøtt 2003', 3, 5, 'Gressbanen', 'dommer ukjent dommer ukjent', 1);
INSERT INTO [matches] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [home_team], [away_team], [goals_home_team], [goals_away_team], [venue], [referee], [match_type_id]) VALUES (4, '2014-03-20 22:02:41', 1, 1, 1389117600, 'UIL Knøtt 2003', 'Sarpsborg', 5, 9, 'Bergbanen', 'dommer ukjent dommer ukjent', 1);
INSERT INTO [matches] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [home_team], [away_team], [goals_home_team], [goals_away_team], [venue], [referee], [match_type_id]) VALUES (5, '2014-03-20 22:02:41', 1, 1, 1389722400, 'Ullern', 'UIL Knøtt 2003', 0, 9, 'Ullernbanen', 'dommer ukjent dommer ukjent', 1);
INSERT INTO [matches] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [home_team], [away_team], [goals_home_team], [goals_away_team], [venue], [referee], [match_type_id]) VALUES (6, '2014-03-20 22:02:41', 1, 1, 1390323600, 'UIL Knøtt 2003', 'Røa 03 1', 7, 7, 'Bergbanen', 'dommer ukjent dommer ukjent', 1);
INSERT INTO [matches] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [home_team], [away_team], [goals_home_team], [goals_away_team], [venue], [referee], [match_type_id]) VALUES (7, '2014-03-20 22:02:41', 1, 1, 1391533200, 'Ready 01 2', 'UIL Knøtt 2003', 1, 10, 'Gressbanen', 'dommer ukjent dommer ukjent', 1);
INSERT INTO [matches] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [home_team], [away_team], [goals_home_team], [goals_away_team], [venue], [referee], [match_type_id]) VALUES (8, '2014-03-20 22:02:41', 1, 1, 1392138000, 'UIL Knøtt 2003', 'Ready 01', 5, 3, 'Bergbanen', 'dommer ukjent dommer ukjent', 1);
INSERT INTO [matches] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [home_team], [away_team], [goals_home_team], [goals_away_team], [venue], [referee], [match_type_id]) VALUES (9, '2014-03-20 22:02:41', 1, 1, 1393437600, 'Sagene 03', 'UIL Knøtt 2003', 3, 11, 'Voldsløkka', 'dommer ukjent dommer ukjent', 1);
INSERT INTO [matches] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [home_team], [away_team], [goals_home_team], [goals_away_team], [venue], [referee], [match_type_id]) VALUES (10, '2014-03-20 22:02:41', 1, 1, 1392224400, 'UIL Knøtt 2003', 'Ullevål 2004', 11, 2, 'Bergbanen', 'dommer ukjent dommer ukjent', 2);
INSERT INTO [matches] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [home_team], [away_team], [goals_home_team], [goals_away_team], [venue], [referee], [match_type_id]) VALUES (11, '2014-03-20 22:02:41', 1, 1, 1394038800, 'UIL Knøtt 2003', 'Ullevål 2002', 3, 6, 'Bergbanen', 'Mats Håkansson Mats Håkansson', 2);
INSERT INTO [matches] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [home_team], [away_team], [goals_home_team], [goals_away_team], [venue], [referee], [match_type_id]) VALUES (12, '2014-03-20 22:02:49', 1, 1, 1384074000, 'Konnerud', 'UIL Knøtt 2003', 5, 2, '', 'dommer ukjent dommer ukjent', 3);
INSERT INTO [matches] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [home_team], [away_team], [goals_home_team], [goals_away_team], [venue], [referee], [match_type_id]) VALUES (13, '2014-03-20 22:02:49', 1, 1, 1384077600, 'UIL Knøtt 2003', 'Snarøya 2', 6, 3, '', 'dommer ukjent dommer ukjent', 3);
INSERT INTO [matches] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [home_team], [away_team], [goals_home_team], [goals_away_team], [venue], [referee], [match_type_id]) VALUES (14, '2014-03-20 22:02:49', 1, 1, 1384081200, 'Stabekk 1', 'UIL Knøtt 2003', 4, 3, '', 'dommer ukjent dommer ukjent', 3);
INSERT INTO [matches] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [home_team], [away_team], [goals_home_team], [goals_away_team], [venue], [referee], [match_type_id]) VALUES (15, '2014-03-20 22:02:50', 1, 1, 1386493200, 'UIL Knøtt 2003', 'Ready Tigers', 3, 5, '', 'dommer ukjent dommer ukjent', 3);
INSERT INTO [matches] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [home_team], [away_team], [goals_home_team], [goals_away_team], [venue], [referee], [match_type_id]) VALUES (16, '2014-03-20 22:02:50', 1, 1, 1386496800, 'Stabekk 2', 'UIL Knøtt 2003', 6, 3, '', 'dommer ukjent dommer ukjent', 3);
INSERT INTO [matches] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [home_team], [away_team], [goals_home_team], [goals_away_team], [venue], [referee], [match_type_id]) VALUES (17, '2014-03-20 22:02:50', 1, 1, 1386500400, 'Øv/Holse løvene', 'UIL Knøtt 2003', 4, 4, '', 'dommer ukjent dommer ukjent', 3);
INSERT INTO [matches] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [home_team], [away_team], [goals_home_team], [goals_away_team], [venue], [referee], [match_type_id]) VALUES (18, '2014-03-20 22:02:50', 1, 1, 1391331600, 'Ready Champions', 'UIL Knøtt 2003', 4, 0, '', 'dommer ukjent dommer ukjent', 3);
INSERT INTO [matches] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [home_team], [away_team], [goals_home_team], [goals_away_team], [venue], [referee], [match_type_id]) VALUES (19, '2014-03-20 22:02:50', 1, 1, 1391335200, 'UIL Knøtt 2003', 'Stabekk 2', 0, 2, '', 'dommer ukjent dommer ukjent', 3);
INSERT INTO [matches] ([_id], [created_date_time], [fk_season_id], [fk_team_id], [start_date], [home_team], [away_team], [goals_home_team], [goals_away_team], [venue], [referee], [match_type_id]) VALUES (20, '2014-03-20 22:02:51', 1, 1, 1391338800, 'Snarøya 2', 'UIL Knøtt 2003', 2, 5, '', 'dommer ukjent dommer ukjent', 3);

-- Table: players
CREATE TABLE players(_id               INTEGER  PRIMARY KEY AUTOINCREMENT,created_date_time DATETIME DEFAULT CURRENT_TIMESTAMP,fk_team_id        INTEGER,fk_address_id     INTEGER,status            TEXT     NOT NULL,first_name        TEXT     NOT NULL,middle_name       TEXT,last_name         TEXT     NOT NULL,date_of_birth     INTEGER,email             TEXT,mobile            TEXT,UNIQUE(first_name,last_name) ON CONFLICT ABORT,FOREIGN KEY(fk_team_id)REFERENCES teams(_id));

INSERT INTO [players] ([_id], [created_date_time], [fk_team_id], [fk_address_id], [status], [first_name], [middle_name], [last_name], [date_of_birth], [email], [mobile]) VALUES (1, '2014-03-20 22:02:52', 1, 2, 'ACTIVE', 'Emilie', '', 'Paluen', 1011657600, null, null);
INSERT INTO [players] ([_id], [created_date_time], [fk_team_id], [fk_address_id], [status], [first_name], [middle_name], [last_name], [date_of_birth], [email], [mobile]) VALUES (2, '2014-03-20 22:02:53', 1, 3, 'ACTIVE', 'Andreas', '', 'Paluen', 1069286400, null, null);
INSERT INTO [players] ([_id], [created_date_time], [fk_team_id], [fk_address_id], [status], [first_name], [middle_name], [last_name], [date_of_birth], [email], [mobile]) VALUES (3, '2014-03-20 22:02:54', 1, 4, 'ACTIVE', 'Marius', '', 'Kiplesund', 1069027200, null, null);
INSERT INTO [players] ([_id], [created_date_time], [fk_team_id], [fk_address_id], [status], [first_name], [middle_name], [last_name], [date_of_birth], [email], [mobile]) VALUES (4, '2014-03-20 22:02:55', 1, 5, 'ACTIVE', 'August', '', 'Hartberg', 0, null, null);
INSERT INTO [players] ([_id], [created_date_time], [fk_team_id], [fk_address_id], [status], [first_name], [middle_name], [last_name], [date_of_birth], [email], [mobile]) VALUES (5, '2014-03-20 22:02:56', 1, 6, 'ACTIVE', 'Anton', '', 'Braanen', 0, null, null);
INSERT INTO [players] ([_id], [created_date_time], [fk_team_id], [fk_address_id], [status], [first_name], [middle_name], [last_name], [date_of_birth], [email], [mobile]) VALUES (6, '2014-03-20 22:02:57', 1, 7, 'ACTIVE', 'Magnus', '', 'Halse', 1384732800, null, null);
INSERT INTO [players] ([_id], [created_date_time], [fk_team_id], [fk_address_id], [status], [first_name], [middle_name], [last_name], [date_of_birth], [email], [mobile]) VALUES (7, '2014-03-20 22:02:59', 1, 8, 'ACTIVE', 'Thomas', '', 'Bergan Håkansson', 0, null, null);
INSERT INTO [players] ([_id], [created_date_time], [fk_team_id], [fk_address_id], [status], [first_name], [middle_name], [last_name], [date_of_birth], [email], [mobile]) VALUES (8, '2014-03-20 22:03:01', 1, 9, 'ACTIVE', 'Linus', '', 'Jensen', 0, null, null);
INSERT INTO [players] ([_id], [created_date_time], [fk_team_id], [fk_address_id], [status], [first_name], [middle_name], [last_name], [date_of_birth], [email], [mobile]) VALUES (9, '2014-03-20 22:03:02', 1, 10, 'ACTIVE', 'Oscar', '', 'Dunlop', 0, null, null);
INSERT INTO [players] ([_id], [created_date_time], [fk_team_id], [fk_address_id], [status], [first_name], [middle_name], [last_name], [date_of_birth], [email], [mobile]) VALUES (10, '2014-03-20 22:03:03', 1, 11, 'ACTIVE', 'Mathias', '', 'Harildstad', 0, null, null);
INSERT INTO [players] ([_id], [created_date_time], [fk_team_id], [fk_address_id], [status], [first_name], [middle_name], [last_name], [date_of_birth], [email], [mobile]) VALUES (11, '2014-03-20 22:03:04', 1, 12, 'ACTIVE', 'Martin André', '', 'Karal', 1067212800, null, null);
INSERT INTO [players] ([_id], [created_date_time], [fk_team_id], [fk_address_id], [status], [first_name], [middle_name], [last_name], [date_of_birth], [email], [mobile]) VALUES (12, '2014-03-20 22:03:05', 1, 13, 'ACTIVE', 'Trygve', '', 'Scharff', 1059955200, null, null);
INSERT INTO [players] ([_id], [created_date_time], [fk_team_id], [fk_address_id], [status], [first_name], [middle_name], [last_name], [date_of_birth], [email], [mobile]) VALUES (13, '2014-03-20 22:03:06', 1, 14, 'ACTIVE', 'Andreas', '', 'Bjelland', 0, null, null);
INSERT INTO [players] ([_id], [created_date_time], [fk_team_id], [fk_address_id], [status], [first_name], [middle_name], [last_name], [date_of_birth], [email], [mobile]) VALUES (14, '2014-03-20 22:03:07', 1, 15, 'ACTIVE', 'Paul', 'Ring', 'Sørensen', 1069891200, null, null);
INSERT INTO [players] ([_id], [created_date_time], [fk_team_id], [fk_address_id], [status], [first_name], [middle_name], [last_name], [date_of_birth], [email], [mobile]) VALUES (15, '2014-03-20 22:03:09', 1, 16, 'ACTIVE', 'Brage', '', 'Frivik', 0, null, null);
INSERT INTO [players] ([_id], [created_date_time], [fk_team_id], [fk_address_id], [status], [first_name], [middle_name], [last_name], [date_of_birth], [email], [mobile]) VALUES (16, '2014-03-20 22:03:10', 1, 17, 'ACTIVE', 'Ole Martin', '', 'Skedsmo', 1052179200, null, null);

-- Table: android_metadata
CREATE TABLE android_metadata(locale TEXT);

INSERT INTO [android_metadata] ([locale]) VALUES ('en_US');

-- Table: addresses
CREATE TABLE addresses(_id                   INTEGER  PRIMARY KEY AUTOINCREMENT,created_date_time     DATETIME DEFAULT CURRENT_TIMESTAMP,street_name           TEXT     NOT NULL,street_number         TEXT     NOT NULL,street_number_postfix TEXT,zip_code              TEXT     NOT NULL,city                  TEXT     NOT NULL,post_code             TEXT,post_box              TEXT,country               TEXT     NOT NULL,UNIQUE(street_name,street_number,street_number_postfix,zip_code) ON CONFLICT ABORT);

INSERT INTO [addresses] ([_id], [created_date_time], [street_name], [street_number], [street_number_postfix], [zip_code], [city], [post_code], [post_box], [country]) VALUES (1, '2014-03-20 22:02:23', 'na', 'na', 'na', 'na', 'na', null, null, 'na');
INSERT INTO [addresses] ([_id], [created_date_time], [street_name], [street_number], [street_number_postfix], [zip_code], [city], [post_code], [post_box], [country]) VALUES (2, '2014-03-20 22:02:52', 'Stavangergata', 35, null, 'Oslo', 0467, null, null, 'Norway');
INSERT INTO [addresses] ([_id], [created_date_time], [street_name], [street_number], [street_number_postfix], [zip_code], [city], [post_code], [post_box], [country]) VALUES (3, '2014-03-20 22:02:53', 'Stavangergata', 35, null, 'Oslo', 0467, null, null, 'Norway');
INSERT INTO [addresses] ([_id], [created_date_time], [street_name], [street_number], [street_number_postfix], [zip_code], [city], [post_code], [post_box], [country]) VALUES (4, '2014-03-20 22:02:54', '', '', null, '', '', null, null, '');
INSERT INTO [addresses] ([_id], [created_date_time], [street_name], [street_number], [street_number_postfix], [zip_code], [city], [post_code], [post_box], [country]) VALUES (5, '2014-03-20 22:02:55', '', '', null, '', '', null, null, '');
INSERT INTO [addresses] ([_id], [created_date_time], [street_name], [street_number], [street_number_postfix], [zip_code], [city], [post_code], [post_box], [country]) VALUES (6, '2014-03-20 22:02:56', '', '', null, '', '', null, null, '');
INSERT INTO [addresses] ([_id], [created_date_time], [street_name], [street_number], [street_number_postfix], [zip_code], [city], [post_code], [post_box], [country]) VALUES (7, '2014-03-20 22:02:57', '', '', null, '', '', null, null, '');
INSERT INTO [addresses] ([_id], [created_date_time], [street_name], [street_number], [street_number_postfix], [zip_code], [city], [post_code], [post_box], [country]) VALUES (8, '2014-03-20 22:02:59', '', '', null, '', '', null, null, '');
INSERT INTO [addresses] ([_id], [created_date_time], [street_name], [street_number], [street_number_postfix], [zip_code], [city], [post_code], [post_box], [country]) VALUES (9, '2014-03-20 22:03:01', '', '', null, '', '', null, null, '');
INSERT INTO [addresses] ([_id], [created_date_time], [street_name], [street_number], [street_number_postfix], [zip_code], [city], [post_code], [post_box], [country]) VALUES (10, '2014-03-20 22:03:02', '', '', null, '', '', null, null, '');
INSERT INTO [addresses] ([_id], [created_date_time], [street_name], [street_number], [street_number_postfix], [zip_code], [city], [post_code], [post_box], [country]) VALUES (11, '2014-03-20 22:03:03', '', '', null, '', '', null, null, '');
INSERT INTO [addresses] ([_id], [created_date_time], [street_name], [street_number], [street_number_postfix], [zip_code], [city], [post_code], [post_box], [country]) VALUES (12, '2014-03-20 22:03:04', '', '', null, '', '', null, null, '');
INSERT INTO [addresses] ([_id], [created_date_time], [street_name], [street_number], [street_number_postfix], [zip_code], [city], [post_code], [post_box], [country]) VALUES (13, '2014-03-20 22:03:05', '', '', null, '', '', null, null, '');
INSERT INTO [addresses] ([_id], [created_date_time], [street_name], [street_number], [street_number_postfix], [zip_code], [city], [post_code], [post_box], [country]) VALUES (14, '2014-03-20 22:03:06', '', '', null, '', '', null, null, '');
INSERT INTO [addresses] ([_id], [created_date_time], [street_name], [street_number], [street_number_postfix], [zip_code], [city], [post_code], [post_box], [country]) VALUES (15, '2014-03-20 22:03:07', '', '', null, '', '', null, null, '');
INSERT INTO [addresses] ([_id], [created_date_time], [street_name], [street_number], [street_number_postfix], [zip_code], [city], [post_code], [post_box], [country]) VALUES (16, '2014-03-20 22:03:09', '', '', null, '', '', null, null, '');
INSERT INTO [addresses] ([_id], [created_date_time], [street_name], [street_number], [street_number_postfix], [zip_code], [city], [post_code], [post_box], [country]) VALUES (17, '2014-03-20 22:03:10', '', '', null, '', '', null, null, '');

-- Table: contacts
CREATE TABLE contacts(_id               INTEGER  PRIMARY KEY AUTOINCREMENT,created_date_time DATETIME DEFAULT CURRENT_TIMESTAMP,fk_address_id     INTEGER,fk_team_id        INTEGER,first_name        TEXT     NOT NULL,middle_name       TEXT     NOT NULL,last_name         TEXT     NOT NULL,mobile            TEXT     NOT NULL,email             TEXT     NOT NULL,UNIQUE(first_name,last_name) ON CONFLICT ABORT);

INSERT INTO [contacts] ([_id], [created_date_time], [fk_address_id], [fk_team_id], [first_name], [middle_name], [last_name], [mobile], [email]) VALUES (1, '2014-03-20 22:02:23', 1, 1, 'Gunnar', '', 'Rønneberg', +4745465500, 'gunnar_ronneberg@yahoo.no');
INSERT INTO [contacts] ([_id], [created_date_time], [fk_address_id], [fk_team_id], [first_name], [middle_name], [last_name], [mobile], [email]) VALUES (2, '2014-03-20 22:02:24', 1, 1, 'Siri', 'Birgitte', 'Paulen', +4792671308, 'spaulen@ymail.com');
INSERT INTO [contacts] ([_id], [created_date_time], [fk_address_id], [fk_team_id], [first_name], [middle_name], [last_name], [mobile], [email]) VALUES (3, '2014-03-20 22:02:25', 1, 1, 'Trond', '', 'Kiplesund', +4792045699, 'trond.kiplesund@sparebank1.no');
INSERT INTO [contacts] ([_id], [created_date_time], [fk_address_id], [fk_team_id], [first_name], [middle_name], [last_name], [mobile], [email]) VALUES (4, '2014-03-20 22:02:25', 1, 1, 'Heidi', '', 'Kiplesund', '', '');
INSERT INTO [contacts] ([_id], [created_date_time], [fk_address_id], [fk_team_id], [first_name], [middle_name], [last_name], [mobile], [email]) VALUES (5, '2014-03-20 22:02:26', 1, 1, 'Vemund', '', 'Hartberg', +4792668817, 'vem-har@online.no');
INSERT INTO [contacts] ([_id], [created_date_time], [fk_address_id], [fk_team_id], [first_name], [middle_name], [last_name], [mobile], [email]) VALUES (6, '2014-03-20 22:02:28', 1, 1, 'Cecilie', '', 'Hartberg', +4792666231, 'c.b.hartberg@medisin.uio.no');
INSERT INTO [contacts] ([_id], [created_date_time], [fk_address_id], [fk_team_id], [first_name], [middle_name], [last_name], [mobile], [email]) VALUES (7, '2014-03-20 22:02:29', 1, 1, 'Johan', '', 'Braanen', '', 'Johan@Braanen.com');
INSERT INTO [contacts] ([_id], [created_date_time], [fk_address_id], [fk_team_id], [first_name], [middle_name], [last_name], [mobile], [email]) VALUES (8, '2014-03-20 22:02:30', 1, 1, 'Frank', '', 'Halse', '', 'fhalse@gmail.com');
INSERT INTO [contacts] ([_id], [created_date_time], [fk_address_id], [fk_team_id], [first_name], [middle_name], [last_name], [mobile], [email]) VALUES (9, '2014-03-20 22:02:30', 1, 1, 'Anita', '', 'Halse', +4798616194, 'halseanita@gmail.com');
INSERT INTO [contacts] ([_id], [created_date_time], [fk_address_id], [fk_team_id], [first_name], [middle_name], [last_name], [mobile], [email]) VALUES (10, '2014-03-20 22:02:31', 1, 1, 'Mats', '', 'Håkansson', +4792031707, 'mats.hakansson@hotmail.no');
INSERT INTO [contacts] ([_id], [created_date_time], [fk_address_id], [fk_team_id], [first_name], [middle_name], [last_name], [mobile], [email]) VALUES (11, '2014-03-20 22:02:32', 1, 1, 'Atle', '', 'Jensen', '', 'atlej@math.uio.no');
INSERT INTO [contacts] ([_id], [created_date_time], [fk_address_id], [fk_team_id], [first_name], [middle_name], [last_name], [mobile], [email]) VALUES (12, '2014-03-20 22:02:32', 1, 1, 'Kristin', '', 'Halvorsen', '', 'kristinprivat@hotmail.com');
INSERT INTO [contacts] ([_id], [created_date_time], [fk_address_id], [fk_team_id], [first_name], [middle_name], [last_name], [mobile], [email]) VALUES (13, '2014-03-20 22:02:33', 1, 1, 'David', '', 'Dunlop', +4799106806, 'david.dunlop@ude.oslo.kommune.no');
INSERT INTO [contacts] ([_id], [created_date_time], [fk_address_id], [fk_team_id], [first_name], [middle_name], [last_name], [mobile], [email]) VALUES (14, '2014-03-20 22:02:33', 1, 1, 'Torstein', '', 'Harildstad', '', 'torstein.harildstad@software-innovation.com');
INSERT INTO [contacts] ([_id], [created_date_time], [fk_address_id], [fk_team_id], [first_name], [middle_name], [last_name], [mobile], [email]) VALUES (15, '2014-03-20 22:02:34', 1, 1, 'Petter', '', 'Karal', '', 'petter@karal.no');
INSERT INTO [contacts] ([_id], [created_date_time], [fk_address_id], [fk_team_id], [first_name], [middle_name], [last_name], [mobile], [email]) VALUES (16, '2014-03-20 22:02:35', 1, 1, 'Heidi', '', 'Karal', '', 'heidi.karal@gmail.com');
INSERT INTO [contacts] ([_id], [created_date_time], [fk_address_id], [fk_team_id], [first_name], [middle_name], [last_name], [mobile], [email]) VALUES (17, '2014-03-20 22:02:35', 1, 1, 'Otto', '', 'Scharff', '', 'otto@scharff.net');
INSERT INTO [contacts] ([_id], [created_date_time], [fk_address_id], [fk_team_id], [first_name], [middle_name], [last_name], [mobile], [email]) VALUES (18, '2014-03-20 22:02:37', 1, 1, 'Kristin', '', 'Bjelland', '', 'kbjellan@gmail.com');
INSERT INTO [contacts] ([_id], [created_date_time], [fk_address_id], [fk_team_id], [first_name], [middle_name], [last_name], [mobile], [email]) VALUES (19, '2014-03-20 22:02:37', 1, 1, 'Anne Jorun', '', 'Aas', '', 'annejorunaas@gmail.com');
INSERT INTO [contacts] ([_id], [created_date_time], [fk_address_id], [fk_team_id], [first_name], [middle_name], [last_name], [mobile], [email]) VALUES (20, '2014-03-20 22:02:39', 1, 1, 'Karen', '', 'Frivik', '', 'karen@dyrevern.no');
INSERT INTO [contacts] ([_id], [created_date_time], [fk_address_id], [fk_team_id], [first_name], [middle_name], [last_name], [mobile], [email]) VALUES (21, '2014-03-20 22:02:40', 1, 1, 'Nini', '', 'Ring', '', 'nini.ring@domstol.no');

-- Table: cups
CREATE TABLE cups(_id               INTEGER  PRIMARY KEY AUTOINCREMENT,created_date_time DATETIME DEFAULT CURRENT_TIMESTAMP,fk_season_id      INTEGER  NOT NULL
                           DEFAULT 1,start_date        INTEGER,cup_name          TEXT     NOT NULL
                           UNIQUE,club_name         TEXT     NOT NULL,venue             TEXT     NOT NULL,deadline_date     INTEGER);

INSERT INTO [cups] ([_id], [created_date_time], [fk_season_id], [start_date], [cup_name], [club_name], [venue], [deadline_date]) VALUES (1, '2014-03-20 22:02:48', 1, 1384074000, 'Stabekk', 'Kosa Cup', 'Hauger kunstis', 1384041600);
INSERT INTO [cups] ([_id], [created_date_time], [fk_season_id], [start_date], [cup_name], [club_name], [venue], [deadline_date]) VALUES (2, '2014-03-20 22:02:49', 1, 1386493200, 'Frigg', 'Frigg Cup', 'Frogner Stadion', 1386460800);
INSERT INTO [cups] ([_id], [created_date_time], [fk_season_id], [start_date], [cup_name], [club_name], [venue], [deadline_date]) VALUES (3, '2014-03-20 22:02:50', 1, 1391331600, 'Snarøya', 'Snarøya Cup', 'Snarøya', 1389398400);

-- Table: cup_match_lnk
CREATE TABLE cup_match_lnk(_id               INTEGER  PRIMARY KEY AUTOINCREMENT,created_date_time DATETIME DEFAULT CURRENT_TIMESTAMP,fk_cup_id         INTEGER  NOT NULL,fk_match_id       INTEGER  NOT NULL,UNIQUE(fk_cup_id,fk_match_id) ON CONFLICT ABORT,FOREIGN KEY(fk_cup_id)REFERENCES players(_id),FOREIGN KEY(fk_match_id)REFERENCES matches(_id));

INSERT INTO [cup_match_lnk] ([_id], [created_date_time], [fk_cup_id], [fk_match_id]) VALUES (1, '2014-03-20 22:02:49', 1, 12);
INSERT INTO [cup_match_lnk] ([_id], [created_date_time], [fk_cup_id], [fk_match_id]) VALUES (2, '2014-03-20 22:02:49', 1, 13);
INSERT INTO [cup_match_lnk] ([_id], [created_date_time], [fk_cup_id], [fk_match_id]) VALUES (3, '2014-03-20 22:02:49', 1, 14);
INSERT INTO [cup_match_lnk] ([_id], [created_date_time], [fk_cup_id], [fk_match_id]) VALUES (4, '2014-03-20 22:02:50', 2, 15);
INSERT INTO [cup_match_lnk] ([_id], [created_date_time], [fk_cup_id], [fk_match_id]) VALUES (5, '2014-03-20 22:02:50', 2, 16);
INSERT INTO [cup_match_lnk] ([_id], [created_date_time], [fk_cup_id], [fk_match_id]) VALUES (6, '2014-03-20 22:02:50', 2, 17);
INSERT INTO [cup_match_lnk] ([_id], [created_date_time], [fk_cup_id], [fk_match_id]) VALUES (7, '2014-03-20 22:02:50', 3, 18);
INSERT INTO [cup_match_lnk] ([_id], [created_date_time], [fk_cup_id], [fk_match_id]) VALUES (8, '2014-03-20 22:02:51', 3, 19);
INSERT INTO [cup_match_lnk] ([_id], [created_date_time], [fk_cup_id], [fk_match_id]) VALUES (9, '2014-03-20 22:02:51', 3, 20);

-- Table: leagues
CREATE TABLE leagues(_id                                    INTEGER  PRIMARY KEY AUTOINCREMENT,created_date_time                      DATETIME DEFAULT CURRENT_TIMESTAMP,league_name                            TEXT,league_player_age_min                  INTEGER,league_player_age_max                  INTEGER,league_gender                          TEXT,league_match_period_time_minutes       INTEGER,league_match_extra_period_time_minutes INTEGER,league_number_of_players               INTEGER,league_description                     TEXT);

INSERT INTO [leagues] ([_id], [created_date_time], [league_name], [league_player_age_min], [league_player_age_max], [league_gender], [league_match_period_time_minutes], [league_match_extra_period_time_minutes], [league_number_of_players], [league_description]) VALUES (1, 'datetime()', 'Knøtt', '', 11, 'male', 25, '', 7, 'Spillerne må ikke ha fylt 11 år ved årsskiftet i inneværende sesong');
INSERT INTO [leagues] ([_id], [created_date_time], [league_name], [league_player_age_min], [league_player_age_max], [league_gender], [league_match_period_time_minutes], [league_match_extra_period_time_minutes], [league_number_of_players], [league_description]) VALUES (2, 'datetime()', 'Lillegutt', '', 13, 'male', 25, '', 7, 'Spillerne må ikke ha fylt 13 år ved årsskiftet i inneværende sesong');
INSERT INTO [leagues] ([_id], [created_date_time], [league_name], [league_player_age_min], [league_player_age_max], [league_gender], [league_match_period_time_minutes], [league_match_extra_period_time_minutes], [league_number_of_players], [league_description]) VALUES (3, 'datetime()', 'Smågutt', '', 15, 'male', 30, 5, 11, 'Spillerne må ikke ha fylt 15 år ved årsskiftet i inneværende sesong');
INSERT INTO [leagues] ([_id], [created_date_time], [league_name], [league_player_age_min], [league_player_age_max], [league_gender], [league_match_period_time_minutes], [league_match_extra_period_time_minutes], [league_number_of_players], [league_description]) VALUES (4, 'datetime()', 'Gutt', '', 17, 'male', 40, 10, 11, 'Spillerne må ikke ha fylt 17 år ved årsskiftet i inneværende sesong');
INSERT INTO [leagues] ([_id], [created_date_time], [league_name], [league_player_age_min], [league_player_age_max], [league_gender], [league_match_period_time_minutes], [league_match_extra_period_time_minutes], [league_number_of_players], [league_description]) VALUES (5, 'datetime()', 'Junior', '', 20, 'male', 45, 10, 11, 'Spillerne må ikke ha fylt 20 år ved årsskiftet i inneværende sesong');
INSERT INTO [leagues] ([_id], [created_date_time], [league_name], [league_player_age_min], [league_player_age_max], [league_gender], [league_match_period_time_minutes], [league_match_extra_period_time_minutes], [league_number_of_players], [league_description]) VALUES (6, 'datetime()', 'Old boys', 35, 50, 'male', 30, 5, 11, 'info');
INSERT INTO [leagues] ([_id], [created_date_time], [league_name], [league_player_age_min], [league_player_age_max], [league_gender], [league_match_period_time_minutes], [league_match_extra_period_time_minutes], [league_number_of_players], [league_description]) VALUES (7, 'datetime()', 'Veteran', 50, '', 'male', 30, 5, 7, 'info');

-- Table: league_match_lnk
CREATE TABLE league_match_lnk(_id               INTEGER  PRIMARY KEY AUTOINCREMENT,created_date_time DATETIME DEFAULT CURRENT_TIMESTAMP,fk_league_id      INTEGER  NOT NULL,fk_match_id       INTEGER  NOT NULL,UNIQUE(fk_league_id,fk_match_id) ON CONFLICT ABORT,FOREIGN KEY(fk_league_id)REFERENCES players(_id),FOREIGN KEY(fk_match_id)REFERENCES cups(_id));


-- Table: league_team_lnk
CREATE TABLE league_team_lnk(_id               INTEGER  PRIMARY KEY AUTOINCREMENT,created_date_time DATETIME DEFAULT CURRENT_TIMESTAMP,fk_league_id      INTEGER  NOT NULL,fk_team_id        INTEGER  NOT NULL,UNIQUE(fk_league_id,fk_team_id) ON CONFLICT ABORT,FOREIGN KEY(fk_league_id)REFERENCES players(_id),FOREIGN KEY(fk_team_id)REFERENCES cups(_id));


-- Table: match_types
CREATE TABLE match_types(_id               INTEGER  PRIMARY KEY AUTOINCREMENT,created_date_time DATETIME DEFAULT CURRENT_TIMESTAMP,match_type_id     INTEGER,match_type_name   TEXT     NOT NULL);

INSERT INTO [match_types] ([_id], [created_date_time], [match_type_id], [match_type_name]) VALUES (1, 'datetime()', 1, 'LEAGUE');
INSERT INTO [match_types] ([_id], [created_date_time], [match_type_id], [match_type_name]) VALUES (2, 'datetime()', 2, 'TRAINING');
INSERT INTO [match_types] ([_id], [created_date_time], [match_type_id], [match_type_name]) VALUES (3, 'datetime()', 3, 'CUP');
INSERT INTO [match_types] ([_id], [created_date_time], [match_type_id], [match_type_name]) VALUES (4, 'datetime()', 4, 'TOURNAMENT');

-- Table: player_contact_lnk
CREATE TABLE player_contact_lnk(_id               INTEGER  PRIMARY KEY AUTOINCREMENT,created_date_time DATETIME DEFAULT CURRENT_TIMESTAMP,fk_player_id      INTEGER  NOT NULL,fk_contact_id     INTEGER  NOT NULL,UNIQUE(fk_player_id,fk_contact_id) ON CONFLICT ABORT,FOREIGN KEY(fk_player_id)REFERENCES players(_id),FOREIGN KEY(fk_contact_id)REFERENCES contacts(_id));

INSERT INTO [player_contact_lnk] ([_id], [created_date_time], [fk_player_id], [fk_contact_id]) VALUES (1, '2014-03-20 22:02:52', 1, 1);
INSERT INTO [player_contact_lnk] ([_id], [created_date_time], [fk_player_id], [fk_contact_id]) VALUES (2, '2014-03-20 22:02:52', 1, 2);
INSERT INTO [player_contact_lnk] ([_id], [created_date_time], [fk_player_id], [fk_contact_id]) VALUES (3, '2014-03-20 22:02:53', 2, 1);
INSERT INTO [player_contact_lnk] ([_id], [created_date_time], [fk_player_id], [fk_contact_id]) VALUES (4, '2014-03-20 22:02:53', 2, 2);
INSERT INTO [player_contact_lnk] ([_id], [created_date_time], [fk_player_id], [fk_contact_id]) VALUES (5, '2014-03-20 22:02:54', 3, 3);
INSERT INTO [player_contact_lnk] ([_id], [created_date_time], [fk_player_id], [fk_contact_id]) VALUES (6, '2014-03-20 22:02:54', 3, 4);
INSERT INTO [player_contact_lnk] ([_id], [created_date_time], [fk_player_id], [fk_contact_id]) VALUES (7, '2014-03-20 22:02:55', 4, 5);
INSERT INTO [player_contact_lnk] ([_id], [created_date_time], [fk_player_id], [fk_contact_id]) VALUES (8, '2014-03-20 22:02:55', 4, 6);
INSERT INTO [player_contact_lnk] ([_id], [created_date_time], [fk_player_id], [fk_contact_id]) VALUES (9, '2014-03-20 22:02:56', 5, 7);
INSERT INTO [player_contact_lnk] ([_id], [created_date_time], [fk_player_id], [fk_contact_id]) VALUES (10, '2014-03-20 22:02:58', 6, 8);
INSERT INTO [player_contact_lnk] ([_id], [created_date_time], [fk_player_id], [fk_contact_id]) VALUES (11, '2014-03-20 22:02:59', 6, 9);
INSERT INTO [player_contact_lnk] ([_id], [created_date_time], [fk_player_id], [fk_contact_id]) VALUES (12, '2014-03-20 22:03:00', 7, 10);
INSERT INTO [player_contact_lnk] ([_id], [created_date_time], [fk_player_id], [fk_contact_id]) VALUES (13, '2014-03-20 22:03:01', 8, 11);
INSERT INTO [player_contact_lnk] ([_id], [created_date_time], [fk_player_id], [fk_contact_id]) VALUES (14, '2014-03-20 22:03:01', 8, 12);
INSERT INTO [player_contact_lnk] ([_id], [created_date_time], [fk_player_id], [fk_contact_id]) VALUES (15, '2014-03-20 22:03:02', 9, 13);
INSERT INTO [player_contact_lnk] ([_id], [created_date_time], [fk_player_id], [fk_contact_id]) VALUES (16, '2014-03-20 22:03:03', 10, 14);
INSERT INTO [player_contact_lnk] ([_id], [created_date_time], [fk_player_id], [fk_contact_id]) VALUES (17, '2014-03-20 22:03:04', 11, 15);
INSERT INTO [player_contact_lnk] ([_id], [created_date_time], [fk_player_id], [fk_contact_id]) VALUES (18, '2014-03-20 22:03:04', 11, 16);
INSERT INTO [player_contact_lnk] ([_id], [created_date_time], [fk_player_id], [fk_contact_id]) VALUES (19, '2014-03-20 22:03:05', 12, 17);
INSERT INTO [player_contact_lnk] ([_id], [created_date_time], [fk_player_id], [fk_contact_id]) VALUES (20, '2014-03-20 22:03:06', 13, 18);
INSERT INTO [player_contact_lnk] ([_id], [created_date_time], [fk_player_id], [fk_contact_id]) VALUES (21, '2014-03-20 22:03:08', 14, 21);
INSERT INTO [player_contact_lnk] ([_id], [created_date_time], [fk_player_id], [fk_contact_id]) VALUES (22, '2014-03-20 22:03:09', 15, 20);
INSERT INTO [player_contact_lnk] ([_id], [created_date_time], [fk_player_id], [fk_contact_id]) VALUES (23, '2014-03-20 22:03:10', 16, 19);

-- Table: player_cup_lnk
CREATE TABLE player_cup_lnk(_id               INTEGER  PRIMARY KEY AUTOINCREMENT,created_date_time DATETIME DEFAULT CURRENT_TIMESTAMP,fk_player_id      INTEGER  NOT NULL,fk_cup_id         INTEGER  NOT NULL,UNIQUE(fk_player_id,fk_cup_id) ON CONFLICT ABORT,FOREIGN KEY(fk_player_id)REFERENCES players(_id),FOREIGN KEY(fk_cup_id)REFERENCES cups(_id));


-- Table: player_match_lnk
CREATE TABLE player_match_lnk(_id               INTEGER  PRIMARY KEY AUTOINCREMENT,created_date_time DATETIME DEFAULT CURRENT_TIMESTAMP,fk_player_id      INTEGER  NOT NULL,fk_match_id       INTEGER  NOT NULL,UNIQUE(fk_player_id,fk_match_id) ON CONFLICT ABORT,FOREIGN KEY(fk_player_id)REFERENCES players(_id),FOREIGN KEY(fk_match_id)REFERENCES matches(_id));


-- Table: position_types
CREATE TABLE position_types(_id                INTEGER  PRIMARY KEY AUTOINCREMENT,created_date_time  DATETIME DEFAULT CURRENT_TIMESTAMP,position_type_id   INTEGER,position_type_name TEXT     NOT NULL);

INSERT INTO [position_types] ([_id], [created_date_time], [position_type_id], [position_type_name]) VALUES (1, 'datetime()', 1, 'GOALKEEPER');
INSERT INTO [position_types] ([_id], [created_date_time], [position_type_id], [position_type_name]) VALUES (2, 'datetime()', 2, 'DEFENDER');
INSERT INTO [position_types] ([_id], [created_date_time], [position_type_id], [position_type_name]) VALUES (3, 'datetime()', 3, 'MIDFIELDER');
INSERT INTO [position_types] ([_id], [created_date_time], [position_type_id], [position_type_name]) VALUES (4, 'datetime()', 4, 'FORWARD');

-- Table: player_training_lnk
CREATE TABLE player_training_lnk(_id               INTEGER  PRIMARY KEY AUTOINCREMENT,created_date_time DATETIME DEFAULT CURRENT_TIMESTAMP,fk_player_id      INTEGER  NOT NULL,fk_training_id    INTEGER  NOT NULL,UNIQUE(fk_player_id,fk_training_id) ON CONFLICT ABORT,FOREIGN KEY(fk_player_id)REFERENCES players(_id),FOREIGN KEY(fk_training_id)REFERENCES trainings(_id));


-- Table: roles
CREATE TABLE roles(_id               INTEGER  PRIMARY KEY AUTOINCREMENT,created_date_time DATETIME DEFAULT CURRENT_TIMESTAMP,role              TEXT     NOT NULL
                           UNIQUE,fk_contact_id     INTEGER  NOT NULL);

INSERT INTO [roles] ([_id], [created_date_time], [role], [fk_contact_id]) VALUES (1, '2014-03-20 22:02:23', 'TEAMLEAD', 1);
INSERT INTO [roles] ([_id], [created_date_time], [role], [fk_contact_id]) VALUES (2, '2014-03-20 22:02:23', 'COACH', 1);

-- Table: seasons
CREATE TABLE seasons(_id               INTEGER  PRIMARY KEY AUTOINCREMENT,created_date_time DATETIME DEFAULT CURRENT_TIMESTAMP,period            STRING   NOT NULL,start_date        INTEGER  NOT NULL,end_date          INTEGER  NOT NULL,UNIQUE(period,start_date) ON CONFLICT ABORT);

INSERT INTO [seasons] ([_id], [created_date_time], [period], [start_date], [end_date]) VALUES (1, 'datetime()', '2013/2014', 1, 1);
INSERT INTO [seasons] ([_id], [created_date_time], [period], [start_date], [end_date]) VALUES (2, 'datetime()', '2014/2015', 1, 1);
INSERT INTO [seasons] ([_id], [created_date_time], [period], [start_date], [end_date]) VALUES (3, 'datetime()', '2015/2016', 1, 1);
INSERT INTO [seasons] ([_id], [created_date_time], [period], [start_date], [end_date]) VALUES (4, 'datetime()', '2016/2017', 1, 1);

-- Table: settings
CREATE TABLE settings(_id               INTEGER  PRIMARY KEY AUTOINCREMENT,created_date_time DATETIME DEFAULT CURRENT_TIMESTAMP,[KEY]             TEXT     NOT NULL
                           UNIQUE,value             TEXT     NOT NULL);

INSERT INTO [settings] ([_id], [created_date_time], [key], [value]) VALUES (1, 'datetime()', 'data_file_url', 'https://raw.github.com/gunnarro/bandy/master/assets/team.xml');
INSERT INTO [settings] ([_id], [created_date_time], [key], [value]) VALUES (2, '2014-03-20 22:03:10', 'data_file_last_updated', 1395352990875);
INSERT INTO [settings] ([_id], [created_date_time], [key], [value]) VALUES (3, '2014-03-20 22:02:22', 'data_file_version', 0.1);
INSERT INTO [settings] ([_id], [created_date_time], [key], [value]) VALUES (4, 'datetime()', 'mail_account', 'na');
INSERT INTO [settings] ([_id], [created_date_time], [key], [value]) VALUES (5, 'datetime()', 'mail_account_pwd', 'na');

-- Table: statuses
CREATE TABLE statuses(_id               INTEGER  PRIMARY KEY AUTOINCREMENT,created_date_time DATETIME DEFAULT CURRENT_TIMESTAMP,status_id         INTEGER  NOT NULL
                           UNIQUE,status_name       TEXT     NOT NULL);

INSERT INTO [statuses] ([_id], [created_date_time], [status_id], [status_name]) VALUES (1, 'datetime()', 1, 'ACTIVE');
INSERT INTO [statuses] ([_id], [created_date_time], [status_id], [status_name]) VALUES (2, 'datetime()', 2, 'PASSIVE');
INSERT INTO [statuses] ([_id], [created_date_time], [status_id], [status_name]) VALUES (3, 'datetime()', 3, 'INJURED');
INSERT INTO [statuses] ([_id], [created_date_time], [status_id], [status_name]) VALUES (4, 'datetime()', 4, 'QUIT');

-- Table: notifications
CREATE TABLE notifications(_id               INTEGER  PRIMARY KEY AUTOINCREMENT,created_date_time DATETIME DEFAULT CURRENT_TIMESTAMP,sent_date         TEXT     NOT NULL,recipients        TEXT     NOT NULL,subject           TEXT     NOT NULL,message           TEXT     NOT NULL,status            INTEGER);


-- View: match_result_view
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
;

