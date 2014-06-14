package com.gunnarro.android.bandy.view.dashboard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.repository.impl.BandyRepositoryImpl;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;

@RunWith(RobolectricTestRunner.class)
public class SettingsActivityTest {

	// Cantare
	// MsCris
	SettingsActivity activity;

	@Mock
	BandyServiceImpl bandyServiceMock;

	@Mock
	BandyRepositoryImpl BandyRepositoryMock;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		// when(BandyRepositoryMock.open()).thenReturn(true);
		activity = Robolectric.buildActivity(SettingsActivity.class).create().get();
		// BandyService bandyService = mock(BandyServiceImpl.class);
		Bundle bundle = new Bundle();
	}

	@Ignore
	@Test
	public void checkActivityNotNull() throws Exception {
		assertNotNull(activity);
		// when(bandyServiceMock.deleteFile("foo")).thenReturn(true);
		// assertNotNull(
		// homeActivity.getSupportFragmentManager().findFragmentById( R.id. ) );
	}

	@Ignore
	@Test
	public void checkView() throws Exception {
		String title = new HomeActivity().getResources().getString(R.string.settings);
		assertEquals(title, activity.getTitle());
	}

	@Ignore
	@Test
	public void loadDataButtonClick() throws Exception {
		Button view = (Button) activity.findViewById(R.id.load_data_btn);
		assertNotNull(view);
		view.performClick();
	}

	@Ignore
	@Test
	public void saveEmailAccountButtonClick() throws Exception {
		Button view = (Button) activity.findViewById(R.id.save_mail_account_btn);
		assertNotNull(view);
		view.performClick();
	}
}
