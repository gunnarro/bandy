package com.gunnarro.android.bandy.view;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
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
					updateBandyDatabase(file);
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
		init();
		return view;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onResume() {
		super.onResume();
		getActivity().registerReceiver(downloadReceiver, new IntentFilter(DownloadService.NOTIFICATION));
	}

	/**
	 * {@inheritDoc}
	 */
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

		view.findViewById(R.id.save_mail_account_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText mailAccount = (EditText) view.findViewById(R.id.gmail_account_txt);
				updateMailAccount(mailAccount.getText().toString());
				EditText mailAccountPwd = (EditText) view.findViewById(R.id.gmail_account_pwd_txt);
				updateMailAccountPwd(mailAccountPwd.getText().toString());
			}
		});
	}

	protected void updateMailAccount(String mailAccount) {
		this.bandyService.updateEmailAccount(mailAccount);
	}

	protected void updateMailAccountPwd(String mailAccountPwd) {
		this.bandyService.updateEmailAccountPwd(mailAccountPwd);
	}

	private void init() {
		TextView updatedDateTxtView = (TextView) getActivity().findViewById(R.id.data_file_last_updated_date_txt);
		updatedDateTxtView.setText(Utility.getDateFormatter().format(bandyService.getDataFileLastUpdated()));
		TextView versionTxtView = (TextView) getActivity().findViewById(R.id.data_file_version_txt);
		versionTxtView.setText(bandyService.getDataFileVersion());
		// Read mail account settings
		EditText mailAccount = (EditText) getActivity().findViewById(R.id.gmail_account_txt);
		mailAccount.setText(this.bandyService.getEmailAccount());
		EditText mailAccountPwd = (EditText) getActivity().findViewById(R.id.gmail_account_pwd_txt);
		mailAccountPwd.setText(this.bandyService.getEmailAccountPwd());

	}

	private void updateBandyDatabase(String dataFile) {
		bandyService.loadData(dataFile);
		bandyService.updateDataFileLastUpdated(System.currentTimeMillis());
		TextView updatedDateTxtView = (TextView) getActivity().findViewById(R.id.data_file_last_updated_date_txt);
		updatedDateTxtView.setText(Utility.getDateFormatter().format(bandyService.getDataFileLastUpdated()));
		TextView versionTxtView = (TextView) getActivity().findViewById(R.id.data_file_version_txt);
		versionTxtView.setText(bandyService.getDataFileVersion());
	}

	/**
	 * Asynch task for sending email
	 * 
	 * @author admin
	 * 
	 */
	class DownloadDataFileTask extends AsyncTask<Void, Void, Void> {
		private ProgressDialog pdialog;

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pdialog = ProgressDialog.show(getActivity(), "", "Sending Email...", true);
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
