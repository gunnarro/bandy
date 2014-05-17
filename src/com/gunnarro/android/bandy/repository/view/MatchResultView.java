package com.gunnarro.android.bandy.repository.view;

import android.database.sqlite.SQLiteDatabase;

import com.gunnarro.android.bandy.custom.CustomLog;

public class MatchResultView {

	private static final String VIEW_NAME = "match_result_view";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(createMatchResultViewQuery());
		CustomLog.d(MatchResultView.class, createMatchResultViewQuery().replace("\n", "").replace("\t", " "));
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		CustomLog.i(MatchResultView.class, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		database.execSQL(dropViewQuery());
		onCreate(database);
	}

	public static String dropViewQuery() {
		return "DROP VIEW IF EXISTS " + VIEW_NAME;
	}

	public static String createMatchResultViewQuery() {
		StringBuffer query = new StringBuffer();
		query.append("CREATE VIEW match_result_view AS");
		query.append(" SELECT m.fk_season_id AS season_id,");
		query.append("			m.fk_team_id AS team_id,");
		query.append("			m.fk_match_type_id AS match_type_id,");
		query.append("			m.home_team AS team_name,");
		query.append("			count( m.fk_team_id ) AS numberOfPlayedMatches,");
		query.append("			sum( goals_home_team > goals_away_team ) AS numberOfWonMatches,");
		query.append("			sum( goals_home_team = goals_away_team ) AS numberOfDrawMatches,");
		query.append("			sum( goals_home_team < goals_away_team ) AS numberOfLossMatches,");
		query.append("			sum( m.goals_home_team ) AS scored,");
		query.append("			sum( m.goals_away_team ) AS against");
		query.append(" FROM matches m,");
		query.append("      teams t");
		query.append(" WHERE m.home_team LIKE t.team_name");
		query.append(" GROUP BY m.fk_season_id,");
		query.append("			m.fk_team_id,");
		query.append("			m.fk_match_type_id,");
		query.append("			m.home_team");
		query.append(" UNION");
		query.append(" SELECT m.fk_season_id AS season_id,");
		query.append("			m.fk_team_id AS team_id,");
		query.append("			m.fk_match_type_id AS match_type_id,");
		query.append("			m.away_team AS team_name,");
		query.append("			count( m.fk_team_id ) AS numberOfPlayedMatches,");
		query.append("			sum( goals_away_team > goals_home_team ) AS numberOfWonMatches,");
		query.append("			sum( goals_away_team = goals_home_team ) AS numberOfDrawMatches,");
		query.append("			sum( goals_away_team < goals_home_team ) AS numberOfLossMatches,");
		query.append("			sum( m.goals_away_team ) AS scored,");
		query.append("			sum( m.goals_home_team ) AS against");
		query.append("  FROM matches m,");
		query.append("		 teams t");
		query.append(" WHERE m.away_team LIKE t.team_name");
		query.append(" GROUP BY m.fk_season_id,");
		query.append("			m.fk_team_id,");
		query.append("			m.fk_match_type_id,");
		query.append("			m.away_team;");
		return query.toString();
	}

}
