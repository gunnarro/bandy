INSERT INTO [leagues] ([_id], [created_date_time], [league_name], [league_player_age_min], [league_player_age_max], [league_gender], [league_match_period_time_minutes], [league_match_extra_period_time_minutes], [league_number_of_players], [league_description]) VALUES (1, 'datetime()', 'Knøtt', '', 11, 'male', 25, '', 7, 'Spillerne må ikke ha fylt 11 år ved årsskiftet i inneværende sesong');
INSERT INTO [leagues] ([_id], [created_date_time], [league_name], [league_player_age_min], [league_player_age_max], [league_gender], [league_match_period_time_minutes], [league_match_extra_period_time_minutes], [league_number_of_players], [league_description]) VALUES (2, 'datetime()', 'Lillegutt', '', 13, 'male', 25, '', 7, 'Spillerne må ikke ha fylt 13 år ved årsskiftet i inneværende sesong');
INSERT INTO [leagues] ([_id], [created_date_time], [league_name], [league_player_age_min], [league_player_age_max], [league_gender], [league_match_period_time_minutes], [league_match_extra_period_time_minutes], [league_number_of_players], [league_description]) VALUES (3, 'datetime()', 'Smågutt', '', 15, 'male', 30, 5, 11, 'Spillerne må ikke ha fylt 15 år ved årsskiftet i inneværende sesong');
INSERT INTO [leagues] ([_id], [created_date_time], [league_name], [league_player_age_min], [league_player_age_max], [league_gender], [league_match_period_time_minutes], [league_match_extra_period_time_minutes], [league_number_of_players], [league_description]) VALUES (4, 'datetime()', 'Gutt', '', 17, 'male', 40, 10, 11, 'Spillerne må ikke ha fylt 17 år ved årsskiftet i inneværende sesong');
INSERT INTO [leagues] ([_id], [created_date_time], [league_name], [league_player_age_min], [league_player_age_max], [league_gender], [league_match_period_time_minutes], [league_match_extra_period_time_minutes], [league_number_of_players], [league_description]) VALUES (5, 'datetime()', 'Junior', '', 20, 'male', 45, 10, 11, 'Spillerne må ikke ha fylt 20 år ved årsskiftet i inneværende sesong');
INSERT INTO [leagues] ([_id], [created_date_time], [league_name], [league_player_age_min], [league_player_age_max], [league_gender], [league_match_period_time_minutes], [league_match_extra_period_time_minutes], [league_number_of_players], [league_description]) VALUES (6, 'datetime()', 'Old boys', 35, 50, 'male', 30, 5, 11, 'info');
INSERT INTO [leagues] ([_id], [created_date_time], [league_name], [league_player_age_min], [league_player_age_max], [league_gender], [league_match_period_time_minutes], [league_match_extra_period_time_minutes], [league_number_of_players], [league_description]) VALUES (7, 'datetime()', 'Veteran', 50, '', 'male', 30, 5, 7, 'info');

INSERT INTO [match_types] ([_id], [created_date_time], [match_type_id], [match_type_name]) VALUES (1, 'datetime()', 1, 'LEAGUE');
INSERT INTO [match_types] ([_id], [created_date_time], [match_type_id], [match_type_name]) VALUES (2, 'datetime()', 2, 'TRAINING');
INSERT INTO [match_types] ([_id], [created_date_time], [match_type_id], [match_type_name]) VALUES (3, 'datetime()', 3, 'CUP');
INSERT INTO [match_types] ([_id], [created_date_time], [match_type_id], [match_type_name]) VALUES (4, 'datetime()', 4, 'TOURNAMENT');

INSERT INTO [position_types] ([_id], [created_date_time], [position_type_id], [position_type_name]) VALUES (1, 'datetime()', 1, 'GOALKEEPER');
INSERT INTO [position_types] ([_id], [created_date_time], [position_type_id], [position_type_name]) VALUES (2, 'datetime()', 2, 'DEFENDER');
INSERT INTO [position_types] ([_id], [created_date_time], [position_type_id], [position_type_name]) VALUES (3, 'datetime()', 3, 'MIDFIELDER');
INSERT INTO [position_types] ([_id], [created_date_time], [position_type_id], [position_type_name]) VALUES (4, 'datetime()', 4, 'FORWARD');

INSERT INTO [settings] ([_id], [created_date_time], [key], [value]) VALUES (1, 'datetime()', 'data_file_url', 'https://raw.github.com/gunnarro/bandy/master/assets/team.xml');
INSERT INTO [settings] ([_id], [created_date_time], [key], [value]) VALUES (2, '2014-03-20 22:03:10', 'data_file_last_updated', 1395352990875);
INSERT INTO [settings] ([_id], [created_date_time], [key], [value]) VALUES (3, '2014-03-20 22:02:22', 'data_file_version', 0.1);
INSERT INTO [settings] ([_id], [created_date_time], [key], [value]) VALUES (4, 'datetime()', 'mail_account', 'na');
INSERT INTO [settings] ([_id], [created_date_time], [key], [value]) VALUES (5, 'datetime()', 'mail_account_pwd', 'na');

INSERT INTO [statuses] ([_id], [created_date_time], [status_id], [status_name]) VALUES (1, 'datetime()', 1, 'ACTIVE');
INSERT INTO [statuses] ([_id], [created_date_time], [status_id], [status_name]) VALUES (2, 'datetime()', 2, 'PASSIVE');
INSERT INTO [statuses] ([_id], [created_date_time], [status_id], [status_name]) VALUES (3, 'datetime()', 3, 'INJURED');
INSERT INTO [statuses] ([_id], [created_date_time], [status_id], [status_name]) VALUES (4, 'datetime()', 4, 'QUIT');

INSERT INTO [seasons] ([_id], [created_date_time], [period], [start_date], [end_date]) VALUES (1, 'datetime()', '2013/2014', 1, 1);
INSERT INTO [seasons] ([_id], [created_date_time], [period], [start_date], [end_date]) VALUES (2, 'datetime()', '2014/2015', 1, 1);
INSERT INTO [seasons] ([_id], [created_date_time], [period], [start_date], [end_date]) VALUES (3, 'datetime()', '2015/2016', 1, 1);
INSERT INTO [seasons] ([_id], [created_date_time], [period], [start_date], [end_date]) VALUES (4, 'datetime()', '2016/2017', 1, 1);

