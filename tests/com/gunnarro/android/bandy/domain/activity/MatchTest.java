package com.gunnarro.android.bandy.domain.activity;

import junit.framework.TestCase;

import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.activity.Match.MatchStatus;
import com.gunnarro.android.bandy.domain.party.Referee;

public class MatchTest extends TestCase {

	// @Test
	public void testConstructor() {
		Match match = new Match(new Season("2013/2014", 0, 0), System.currentTimeMillis() - 60000, new Team(""), new Team("homeTeam"), new Team("awayTeam"),
				"venue", new Referee("firstname", "lastname"));
		assertTrue(match.isFinished());
		assertNull(match.getNumberOfGoalsHome());
		assertNull(match.getNumberOfGoalsAway());
		assertNull(match.getResult());
		assertFalse(match.isPlayed());
	}

	// @Test
	public void testConstructorPlayed() {
		Match match = new Match(1, new Season("2013/2014", 0, 0), System.currentTimeMillis() - 60000, new Team(""), new Team("homeTeam"), new Team("awayTeam"),
				4, 5, "venue", new Referee("firstname", "lastname"), 33, MatchStatus.PLAYED.name());
		assertEquals(1, match.getId().intValue());
		// assertEquals(1, match.ge);
		assertEquals("homeTeam", match.getHomeTeam().getName());
		assertEquals("awayTeam", match.getAwayTeam().getName());
		assertEquals("homeTeam - awayTeam", match.getTeamVersus());
		assertEquals(4, match.getNumberOfGoalsHome().intValue());
		assertEquals(5, match.getNumberOfGoalsAway().intValue());
		assertEquals("4 - 5", match.getResult());
		assertEquals(33, match.getMatchType().getCode());
		assertEquals("firstname lastname", match.getReferee().getFullName());
		assertTrue(match.isFinished());
		assertTrue(match.isPlayed());
	}

	public void testConstructorNotPlayedDateBefore() {
		Match match = new Match(1, new Season("2013/2014", 0, 0), System.currentTimeMillis() - 60000, new Team(""), new Team("homeTeam"), new Team("awayTeam"),
				null, null, "venue", new Referee("firstname", "lastname"), 33, MatchStatus.PLAYED.name());
		assertTrue(match.isFinished());
		assertNull(match.getNumberOfGoalsHome());
		assertNull(match.getNumberOfGoalsAway());
		assertNull(match.getResult());
		assertFalse(match.isPlayed());
	}

	public void testConstructorNotPlayedDateAfter() {
		Match match = new Match(1, new Season("2013/2014", 0, 0), System.currentTimeMillis() + 60000, new Team(""), new Team("homeTeam"), new Team("awayTeam"),
				null, null, "venue", new Referee("firstname", "lastname"), 33, MatchStatus.PLAYED.name());
		assertFalse(match.isFinished());
		assertNull(match.getNumberOfGoalsHome());
		assertNull(match.getNumberOfGoalsAway());
		assertNull(match.getResult());
		assertFalse(match.isPlayed());
	}
}
