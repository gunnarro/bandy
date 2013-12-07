package com.gunnarro.android.bandy.service.impl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.gunnarro.android.bandy.service.BandyService;

public class DataLoader extends Service {
	public final static String TEAM_XML_URL = "https://raw.github.com/gunnarro/smsfilter/master/team.xml";
	private BandyService bandyService;
	private XmlDocumentParser xmlParser;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void onCreate() {
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// For time consuming an long tasks you can launch a new thread here...
		Toast.makeText(this, " Service Started", Toast.LENGTH_LONG).show();

	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();

	}

	public void loadData() {
		try {
			xmlParser.testParseByXpath(TEAM_XML_URL, bandyService);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
