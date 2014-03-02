package com.gunnarro.android.bandy.view.dashboard;

import java.net.URI;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.service.impl.DataLoader;
import com.gunnarro.android.bandy.service.impl.DownloadService;
import com.gunnarro.android.bandy.utility.DownloadFileTask;
import com.gunnarro.android.bandy.utility.Utility;

public class SetupActivity extends DashboardActivity {

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
					// updateBandyDatabase(file);
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup_layout);
		getActionBar().setTitle(R.string.settings);
		this.bandyService = new BandyServiceImpl(getApplicationContext());
		setupEventHandlers();
		init();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onResume() {
		super.onResume();
		registerReceiver(downloadReceiver, new IntentFilter(DownloadService.NOTIFICATION));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(downloadReceiver);
	}

	private void setupEventHandlers() {
		findViewById(R.id.load_data_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Intent intent = new Intent(getApplicationContext(),
				// DownloadService.class);
				// intent.putExtra(DownloadService.FILENAME, "team.xml");
				// intent.putExtra(DownloadService.URL,
				// DataLoader.TEAM_XML_URL);
				// startService(intent);

				UpdateDataTask task = new UpdateDataTask(SetupActivity.this);
				task.execute(new String[] { DataLoader.TEAM_XML_URL });

				// downloadFile();
			}
		});

		findViewById(R.id.save_mail_account_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText mailAccount = (EditText) findViewById(R.id.gmail_account_txt);
				updateMailAccount(mailAccount.getText().toString());
				EditText mailAccountPwd = (EditText) findViewById(R.id.gmail_account_pwd_txt);
				updateMailAccountPwd(mailAccountPwd.getText().toString());
			}
		});
	}

	private void downloadFile() {
		final DownloadFileTask task = new DownloadFileTask(this);
		try {
			task.execute(new URI(DataLoader.TEAM_XML_URL), null, null);
		} catch (Exception e) {
		}
	}

	protected void updateMailAccount(String mailAccount) {
		this.bandyService.updateEmailAccount(mailAccount);
	}

	protected void updateMailAccountPwd(String mailAccountPwd) {
		this.bandyService.updateEmailAccountPwd(mailAccountPwd);
	}

	private void init() {
		TextView updatedDateTxtView = (TextView) findViewById(R.id.data_file_last_updated_date_txt);
		updatedDateTxtView.setText(Utility.getDateFormatter().format(bandyService.getDataFileLastUpdated()));
		TextView versionTxtView = (TextView) findViewById(R.id.data_file_version_txt);
		versionTxtView.setText(bandyService.getDataFileVersion());
		// Read mail account settings
		EditText mailAccount = (EditText) findViewById(R.id.gmail_account_txt);
		mailAccount.setText(this.bandyService.getEmailAccount());
		EditText mailAccountPwd = (EditText) findViewById(R.id.gmail_account_pwd_txt);
		mailAccountPwd.setText(this.bandyService.getEmailAccountPwd());
	}

	/**
	 * Asynch task for updating data
	 * 
	 * @author admin
	 * 
	 */
	class UpdateDataTask extends AsyncTask<String, Void, String> {
		private ProgressDialog dialog;

		public UpdateDataTask(Activity activity) {
			dialog = new ProgressDialog(activity);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void onPreExecute() {
			// super.onPreExecute();
			dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			dialog.setTitle("Loading data...");
			dialog.setMessage("Please wait.");
			// dialog.setCancelable(true);
			// Show spinner in dialog
			// dialog.setIndeterminate(false);
			dialog.show();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void onProgressUpdate(Void... values) {
			dialog.setProgress(0);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected String doInBackground(String... args) {
			try {
				publishProgress();
				bandyService.loadData(args[0]);
				bandyService.updateDataFileLastUpdated(System.currentTimeMillis());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void onPostExecute(String result) {
			if (dialog != null) {
				dialog.dismiss();
			}
		}
	}

}
