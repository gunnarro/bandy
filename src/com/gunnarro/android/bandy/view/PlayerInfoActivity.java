package com.gunnarro.android.bandy.view;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.domain.Player;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;

public class PlayerInfoActivity extends Activity {

	protected BandyService bandyService;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_info_layout);
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setIcon(R.drawable.bandy);
		actionBar.show();
		// setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

		this.bandyService = new BandyServiceImpl(getApplicationContext());
		setupEventHandlers();
		Bundle bundle = getIntent().getExtras();
		CustomLog.d(this.getClass(), "playerId" + bundle.getInt("playerId"));
		Player player = this.bandyService.getPlayer(bundle.getInt("playerId"));
		updatePlayerDetails(player);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_back:
			Intent intent = new Intent(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	private void setupEventHandlers() {

	}

	private void updatePlayerDetails(Player player) {
		((TextView) findViewById(R.id.nameTxt)).setText(player.getFullName());
		((TextView) findViewById(R.id.addressTxt)).setText("");
		((TextView) findViewById(R.id.mobileTxt)).setText(player.getMobileNumber());
		((TextView) findViewById(R.id.emailTxt)).setText(player.getEmailAddress());
	}

}
