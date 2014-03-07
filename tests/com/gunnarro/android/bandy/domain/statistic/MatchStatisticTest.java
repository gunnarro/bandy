package com.gunnarro.android.bandy.domain.statistic;

import com.gunnarro.android.bandy.domain.statistic.MatchStatistic.MatchTypesEnum;

import junit.framework.TestCase;

public class MatchStatisticTest extends TestCase {

	// @Test
	public void testConstructor() {
		MatchStatistic matchStatistic = new MatchStatistic(MatchTypesEnum.LEAGUE.getCode(), 1, 2, 3, 4, 5, 6);
		assertEquals(MatchTypesEnum.LEAGUE.name(), matchStatistic.getName());
		assertEquals(MatchTypesEnum.LEAGUE.getCode(), matchStatistic.getMatchTypeId());
		assertEquals(1, matchStatistic.getPlayed().intValue());
		assertEquals(2, matchStatistic.getWon().intValue());
		assertEquals(3, matchStatistic.getDraw().intValue());
		assertEquals(4, matchStatistic.getLoss().intValue());
		assertEquals(5, matchStatistic.getGoalsScored().intValue());
		assertEquals(6, matchStatistic.getGoalsAgainst().intValue());

		matchStatistic.incrementPlayed(1);
		matchStatistic.incrementWon(2);
		matchStatistic.incrementDraw(3);
		matchStatistic.incrementLoss(4);
		matchStatistic.incrementGoalsScored(5);
		matchStatistic.incrementGoalsAgainst(6);

		assertEquals(2, matchStatistic.getPlayed().intValue());
		assertEquals(4, matchStatistic.getWon().intValue());
		assertEquals(6, matchStatistic.getDraw().intValue());
		assertEquals(8, matchStatistic.getLoss().intValue());
		assertEquals(10, matchStatistic.getGoalsScored().intValue());
		assertEquals(12, matchStatistic.getGoalsAgainst().intValue());
	}

}