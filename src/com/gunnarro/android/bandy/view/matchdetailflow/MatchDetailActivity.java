package com.gunnarro.android.bandy.view.matchdetailflow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.view.dashboard.DashboardActivity;

/**
 * An activity representing a single Item detail screen. This activity is only
 * used on handset devices. On tablet-size devices, item details are presented
 * side-by-side with a list of items in a {@link MatchListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link MatchDetailFragment}.
 */
public class MatchDetailActivity extends FragmentActivity {

	public static final String ARG_MATCH_ID = "match_id";
	private String teamName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_detail);
		setTitle("Match Details");
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// savedInstanceState is non-null when there is fragment state
		// saved from previous configurations of this activity
		// (e.g. when rotating the screen from portrait to landscape).
		// In this case, the fragment will automatically be re-added
		// to its container so we don't need to manually add it.
		// For more information, see the Fragments API guide at:
		//
		// http://developer.android.com/guide/components/fragments.html
		//
		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			teamName = getIntent().getStringExtra(DashboardActivity.ARG_TEAM_NAME);
			Bundle arguments = new Bundle();
			arguments.putInt(ARG_MATCH_ID, getIntent().getIntExtra(ARG_MATCH_ID, -1));
			MatchDetailFragment fragment = new MatchDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction().add(R.id.item_detail_container, fragment).commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			Intent playerListIntent = new Intent(getApplicationContext(), MatchListActivity.class);
			playerListIntent.putExtra(DashboardActivity.ARG_TEAM_NAME, teamName);
			NavUtils.navigateUpTo(this, playerListIntent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
