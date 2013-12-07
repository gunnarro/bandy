package com.gunnarro.android.bandy.view;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.service.impl.DataLoader;
import com.gunnarro.android.bandy.service.impl.DownloadService;
import com.gunnarro.android.bandy.utility.Utility;

public class SetupFragment extends Fragment {

	private BandyService bandyService;

	private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				String file = bundle.getString(DownloadService.FILEPATH);
				int resultCode = bundle.getInt(DownloadService.RESULT);
				if (resultCode == Activity.RESULT_OK) {
					CustomLog.d(this.getClass(), "Downloaded file:" + file);
					bandyService.loadData(file);
					bandyService.updateDataFileLastUpdated(System.currentTimeMillis());
					TextView updatedDateTxtView = (TextView) getActivity().findViewById(R.id.data_file_last_updated_date_txt);
					updatedDateTxtView.setText(Utility.getDateFormatter().format(bandyService.getDataFileLastUpdated()));
					TextView versionTxtView = (TextView) getActivity().findViewById(R.id.data_file_version_txt);
					versionTxtView.setText(bandyService.getDataFileVersion());
				} else {
					CustomLog.d(this.getClass(), "Problem Downloading data file...");
				}
			}
		}
	};

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.setup_layout, container, false);
		this.bandyService = new BandyServiceImpl(view.getContext());
		setupEventHandlers(view);
//		init();
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		getActivity().registerReceiver(downloadReceiver, new IntentFilter(DownloadService.NOTIFICATION));
	}

	@Override
	public void onPause() {
		super.onPause();
		getActivity().unregisterReceiver(downloadReceiver);
	}

	private void setupEventHandlers(final View view) {
		view.findViewById(R.id.load_data_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), DownloadService.class);
				intent.putExtra(DownloadService.FILENAME, "team.xml");
				intent.putExtra(DownloadService.URL, DataLoader.TEAM_XML_URL);
				getActivity().startService(intent);
			}
		});
	}

	private void init() {
		TextView updatedDateTxtView = (TextView) getActivity().findViewById(R.id.data_file_last_updated_date_txt);
		updatedDateTxtView.setText(Utility.getDateFormatter().format(bandyService.getDataFileLastUpdated()));
		TextView versionTxtView = (TextView) getActivity().findViewById(R.id.data_file_version_txt);
		versionTxtView.setText(bandyService.getDataFileVersion());
	}
}
