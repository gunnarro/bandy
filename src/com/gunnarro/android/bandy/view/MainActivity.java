package com.gunnarro.android.bandy.view;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.view.playerdetailflow.PlayerListActivity;

public class MainActivity extends Activity {

	public static Context appContext;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		appContext = getApplicationContext();

		// ActionBar
		ActionBar actionbar = getActionBar();
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		actionbar.addTab(actionbar.newTab().setText(R.string.tab_setup)
				.setTabListener(new TabListener<SetupFragment>(this, SetupFragment.class.getSimpleName(), SetupFragment.class)));
		actionbar.addTab(actionbar.newTab().setText(R.string.tab_activities)
				.setTabListener(new TabListener<ActivitiesFragment>(this, ActivitiesFragment.class.getSimpleName(), ActivitiesFragment.class)));
		actionbar.addTab(actionbar.newTab().setText(R.string.tab_players)
				.setTabListener(new TabListener<PlayersFragment>(this, PlayersFragment.class.getSimpleName(), PlayersFragment.class)));
		actionbar.addTab(actionbar.newTab().setText(R.string.tab_search)
				.setTabListener(new TabListener<SearchFragment>(this, SearchFragment.class.getSimpleName(), SearchFragment.class)));

		if (savedInstanceState != null) {
			actionbar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main_actions, menu);
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_about:
			Toast.makeText(appContext, R.string.app_about, Toast.LENGTH_SHORT).show();
			return true;
		case R.id.menu_item_quit:
			finish();
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
	}

	public Fragment getCurrentFragment(Fragment fragment) {
		return null;
	}

	/**
	 * Custom tab listener class
	 * 
	 * @author admin
	 * 
	 * @param <T>
	 */
	public static class TabListener<T extends Fragment> implements ActionBar.TabListener {
		private final Activity activity;
		private final String tag;
		private final Class<T> clazz;
		private final Bundle args;
		private Fragment fragment;

		public TabListener(Activity activity, String tag, Class<T> clz) {
			this(activity, tag, clz, null);
		}

		public TabListener(Activity activity, String tag, Class<T> clz, Bundle args) {
			this.activity = activity;
			this.tag = tag;
			this.clazz = clz;
			this.args = args;

			// Check to see if we already have a fragment for this tab, probably
			// from a previously saved state. If so, deactivate it, because our
			// initial state is that a tab isn't shown.
			this.fragment = this.activity.getFragmentManager().findFragmentByTag(this.tag);
			if (this.fragment != null && !this.fragment.isDetached()) {
				FragmentTransaction ft = this.activity.getFragmentManager().beginTransaction();
				ft.detach(this.fragment);
				ft.commit();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			if (this.fragment == null) {
				this.fragment = Fragment.instantiate(this.activity, this.clazz.getName(), this.args);
				ft.add(android.R.id.content, this.fragment, this.tag);
			} else {
				ft.attach(this.fragment);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			if (this.fragment != null) {
				ft.detach(this.fragment);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
		}

	} // end class TabListener

} // end class MainActivity