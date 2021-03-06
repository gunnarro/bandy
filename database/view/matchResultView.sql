CREATE VIEW match_result_view AS
SELECT m.fk_season_id AS season_id,
					m.fk_team_id AS team_id,
					m.fk_match_type_id AS match_type_id,
					m.home_team AS team_name,
					count( m.fk_team_id ) AS numberOfPlayedMatches,
					sum( goals_home_team > goals_away_team ) AS numberOfWonMatches,
					sum( goals_home_team = goals_away_team ) AS numberOfDrawMatches,
					sum( goals_home_team < goals_away_team ) AS numberOfLossMatches,
					sum( m.goals_home_team ) AS scored,
					sum( m.goals_away_team ) AS against
 FROM matches m,
      teams t
 WHERE m.home_team LIKE t.team_name
 GROUP BY m.fk_season_id,
					m.fk_team_id,
					m.fk_match_type_id,
					m.home_team
 UNION
 SELECT m.fk_season_id AS season_id,
					m.fk_team_id AS team_id,
					m.fk_match_type_id AS match_type_id,
					m.away_team AS team_name,
					count( m.fk_team_id ) AS numberOfPlayedMatches,
					sum( goals_away_team > goals_home_team ) AS numberOfWonMatches,
					sum( goals_away_team = goals_home_team ) AS numberOfDrawMatches,
					sum( goals_away_team < goals_home_team ) AS numberOfLossMatches,
					sum( m.goals_away_team ) AS scored,
					sum( m.goals_home_team ) AS against
  FROM matches m,
	   teams t
 WHERE m.away_team LIKE t.team_name
 GROUP BY m.fk_season_id,
		  m.fk_team_id,
  		  m.fk_match_type_id,
		  m.away_team;