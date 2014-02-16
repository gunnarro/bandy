package com.gunnarro.android.bandy.domain.activity;

import junit.framework.TestCase;

import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.activity.Activity.ActivityStatusEnum;
import com.gunnarro.android.bandy.domain.activity.Activity.ActivityTypesEnum;

public class TrainingTest extends TestCase {

	// @Test
	public void testConstructor() {
		Training training = new Training(System.currentTimeMillis(), 120, new Team("team name"), "venue");
		assertEquals(ActivityTypesEnum.Training.name(), training.getName());
		assertEquals(ActivityTypesEnum.Training.name(), training.getType());
		assertEquals(ActivityStatusEnum.BEGIN, training.getStatus());
		assertEquals("team name", training.getTeam().getName());
		assertEquals("venue", training.getVenue());
		assertFalse(training.isFinished());
	}
}
