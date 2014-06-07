/*
 * Copyright (C) 2011 Wglxy.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gunnarro.android.bandy.view.dashboard;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.domain.SearchResult;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;

/**
 * This is the Search activity in the dashboard application. It displays some
 * text and provides a way to get back to the home activity.
 * 
 */

public class SearchActivity extends DashboardActivity {

	private BandyService bandyService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_layout);
		setTitleFromActivityLabel(R.id.title_text);
		this.bandyService = new BandyServiceImpl(getApplicationContext());
		String sqlQuery = this.bandyService.getSqlQuery("", "");
		TextView input = ((TextView) findViewById(R.id.search_input_txt));
		input.setText(sqlQuery.replace(",", ",\n"));
		setupEventHandlers();
	}

	private void setupEventHandlers() {
		findViewById(R.id.search_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				searchDB();
			}
		});

		findViewById(R.id.clear_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((TextView) findViewById(R.id.search_input_txt)).setText("");
				((TextView) findViewById(R.id.search_result_txt)).setText("");
			}
		});
	}

	private void searchDB() {
		String sqlQuery = ((TextView) findViewById(R.id.search_input_txt)).getText().toString();
		SearchResult search = bandyService.search(sqlQuery);
		((TextView) findViewById(R.id.search_result_txt)).setText(search.getResult());
		CustomLog.d(this.getClass(), search.getResult());
	}
} // end class
