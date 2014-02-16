package com.gunnarro.android.bandy.domain.activity;

import junit.framework.TestCase;

import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.party.Referee;

public class MatchTest extends TestCase {

	// @Test
	public void testConstructor() {
		Match match = new Match(1, System.currentTimeMillis(), new Team(""), new Team("homeTeam"), new Team("awayTeam"), 4, 5, "venue", new Referee(
				"firstname", "lastname"), 33);
		assertEquals("homeTeam", match.getHomeTeam().getName());
		assertEquals("awayTeam", match.getAwayTeam().getName());
		assertEquals("homeTeam - awayTeam", match.getTeamVersus());
		assertEquals(4, match.getNumberOfGoalsHome().intValue());
		assertEquals(5, match.getNumberOfGoalsAway().intValue());
		assertEquals("4 - 5", match.getResult());
		assertEquals(33, match.getMatchTypeId().intValue());
		assertEquals("firstname lastname", match.getReferee().getFullName());
		assertTrue(match.isPlayed());
	}
}
