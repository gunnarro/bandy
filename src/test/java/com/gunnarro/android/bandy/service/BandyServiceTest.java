package com.gunnarro.android.bandy.service;

import org.junit.Before;

import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import org.junit.Test;

public class BandyServiceTest {

	private BandyService bandyService;

	@Before
	public void setUp() throws Exception {
		bandyService = new BandyServiceImpl();
	}

	@Test
	public void loadData() {
		bandyService.loadData("");
	}
}
