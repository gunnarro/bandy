package com.gunnarro.android.bandy.view.matchdetailflow;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.activity.Match;
import com.gunnarro.android.bandy.domain.view.list.MultiLineItem;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.exception.ApplicationException;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.utility.Utility;
import com.gunnarro.android.bandy.view.adapter.MultiLineArrayAdapter;
import com.gunnarro.android.bandy.view.dashboard.DashboardActivity;

/**
 * A list fragment representing a list of Items. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link MatchDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class MatchListFragment extends ListFragment {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * activated item position. Only used on tablets.
	 */
	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	private BandyService bandyService;
	private List<MultiLineItem> itemList;

	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = sDummyCallbacks;

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = ListView.INVALID_POSITION;

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(int id);
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onItemSelected(int id) {
		}
	};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public MatchListFragment() {
		CustomLog.d(this.getClass(), "default constructor");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CustomLog.d(this.getClass(), "onCreate state: " + savedInstanceState);
		if (this.bandyService == null) {
			this.bandyService = new BandyServiceImpl(getActivity());
		}
		String teamName = null;
		if (savedInstanceState != null) {
			teamName = savedInstanceState.getString(DashboardActivity.ARG_TEAM_NAME, null);
		} else if (getArguments() != null && getArguments().containsKey(DashboardActivity.ARG_TEAM_NAME)) {
			teamName = getArguments().getString(DashboardActivity.ARG_TEAM_NAME, null);
		} else {
			throw new ApplicationException(this.getClass().getSimpleName() + ": Missing team name attribute!");
		}
		this.itemList = getMatchItemList(teamName);
		CustomLog.d(this.getClass(), "items:" + this.itemList.size());
		setListAdapter(new MultiLineArrayAdapter(getActivity(), this.itemList));
		// finally, update the action bar sub title with number of matches for
		// selected team
		getActivity().getActionBar().setSubtitle(teamName + ", Matches: " + itemList.size());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		CustomLog.d(this.getClass(), "onViewCreated state: " + savedInstanceState);
		// Restore the previously serialized activated item position.
		if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException("Activity must implement fragment's callbacks.");
		}
		mCallbacks = (Callbacks) activity;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDetach() {
		super.onDetach();
		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);
		// Notify the active callbacks interface (the activity, if the
		// fragment is attached to one) that an item has been selected.
		if (this.itemList.get(position).getId() != null) {
			mCallbacks.onItemSelected(this.itemList.get(position).getId().intValue());
		} else {
			CustomLog.e(this.getClass(), "Item: id not set! " + this.itemList.get(position).toString());
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		getListView().setChoiceMode(activateOnItemClick ? ListView.CHOICE_MODE_SINGLE : ListView.CHOICE_MODE_NONE);
	}

	private List<MultiLineItem> getMatchItemList(String teamName) {
		List<MultiLineItem> matches = new ArrayList<MultiLineItem>();
		try {
			Team team = bandyService.getTeam(teamName, true);
			List<Match> matchList = this.bandyService.getMatchList(team.getId(), Calendar.YEAR);
			for (Match match : matchList) {
				String matchInfo = match.getResult();
				if (!match.isPlayed()) {
					matchInfo = match.getVenue() + " (" + match.getNumberOfSignedPlayers() + "/" + team.getPlayerItemList().size() + ")";
				}
				MultiLineItem item = new MultiLineItem(match.getId(), Utility.formatTime(match.getStartTime(), Utility.DATE_TIME_PATTERN),
						match.getTeamVersus(), matchInfo, match.isPlayed());
				matches.add(item);
			}
		} catch (Exception e) {
			CustomLog.e(this.getClass(), e.getMessage());
		}
		return matches;
	}

	private void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}
		mActivatedPosition = position;
	}

}
