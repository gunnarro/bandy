package com.gunnarro.android.bandy.view;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.domain.SearchResult;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;

public class SearchFragment extends Fragment {

	private BandyService bandyService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.search_layout, container, false);
		this.bandyService = new BandyServiceImpl(view.getContext());
		setupEventHandlers(view);
		return view;
	}

	private void setupEventHandlers(final View view) {
		view.findViewById(R.id.search_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// SearchDBTask task = new SearchDBTask();
				// task.execute((Void[]) null);
				searchDB();
			}
		});
	}

	private void searchDB() {
		((TextView) getActivity().findViewById(R.id.search_result_txt)).setText("");
		String sqlQuery = ((TextView) getActivity().findViewById(R.id.search_input_txt)).getText().toString();
		SearchResult search = bandyService.search(sqlQuery);
		((TextView) getActivity().findViewById(R.id.search_result_txt)).setText("RESULT:\n" + search.getResult());
		CustomLog.d(this.getClass(), search.getResult());
		System.out.println(search.getResult());
	}

	/**
	 * Asynch task for DB search
	 * 
	 * @author admin
	 * 
	 */
	class SearchDBTask extends AsyncTask<Void, Void, Void> {
		private ProgressDialog pdialog;

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pdialog = ProgressDialog.show(getActivity(), "", "Searching database...", true);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected Void doInBackground(Void... agrs) {
			try {
				searchDB();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void onPostExecute(Void result) {
			if (pdialog != null) {
				pdialog.dismiss();
			}
		}
	}

}