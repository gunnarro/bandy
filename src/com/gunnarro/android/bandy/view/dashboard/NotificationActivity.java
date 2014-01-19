package com.gunnarro.android.bandy.view.dashboard;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.mail.MailSender;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;

public class NotificationActivity extends Activity {

	private BandyService bandyService;
	private Bundle bundle;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification_layout);
		this.bandyService = new BandyServiceImpl(getApplicationContext());
		setupEventHandlers();
		bundle = getIntent().getExtras();
		init();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onResume() {
		super.onResume();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onPause() {
		super.onPause();
	}

	private void setupEventHandlers() {
		findViewById(R.id.send_email_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				NotifyTask task = new NotifyTask();
				task.execute((Void[]) null);
			}
		});

		findViewById(R.id.send_sms_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				NotifyTask task = new NotifyTask();
				task.execute((Void[]) null);
			}
		});

	}

	private void init() {
		TextView msgTxtView = (TextView) findViewById(R.id.notification_msg_txt);
		StringBuffer msg = new StringBuffer();
		msg.append("To     : ").append(bundle.getStringArrayList("toEmail")).append(bundle.getStringArrayList("toMobile")).append("\n");
		msg.append("From   : ").append(bundle.getString("fromEmail")).append("(").append(bundle.getString("fromMobile")).append(")\n");
		msg.append("Message:").append(bundle.getString("message"));
		msgTxtView.setText(msg.toString());
	}

	private void sendEmail() {
		try {
			MailSender mailSender = new MailSender(this.bandyService.getEmailAccount(), this.bandyService.getEmailAccountPwd());
			String subject = bundle.getString("teamName") + " activites for this " + bundle.getString("periode");
			List<String> toList = Arrays.asList(bundle.getString("to").split(";"));
			mailSender.sendMail(subject, bundle.getString("message"), bundle.getString("from"), toList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendSMS() {

	}

	/**
	 * Asynch task for updating data
	 * 
	 * @author admin
	 * 
	 */
	class NotifyTask extends AsyncTask<Void, Void, Void> {
		private ProgressDialog pdialog;

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pdialog = ProgressDialog.show(getApplicationContext(), "", "Sends notification...", true);
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
				if (true) {
					sendEmail();
				} else {
					sendSMS();
				}
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
