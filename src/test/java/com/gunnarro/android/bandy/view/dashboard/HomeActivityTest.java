package com.gunnarro.android.bandy.view.dashboard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowView;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.repository.impl.BandyRepositoryImpl;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.view.dialog.SelectDialogOnClickListener;

@RunWith(RobolectricTestRunner.class)
public class HomeActivityTest {

	HomeActivity activity;

	@Mock
	BandyServiceImpl bandyServiceMock;

	@Mock
	BandyRepositoryImpl BandyRepositoryMock;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
//		when(BandyRepositoryMock.open()).thenReturn(true);
		activity = Robolectric.buildActivity(HomeActivity.class).create().get();
//		activity.setBandyService(bandyServiceMock);
		// BandyService bandyService = mock(BandyServiceImpl.class);
//		activity = new HomeActivity();
	}

	 @Ignore
	@Test
	public void checkActivityNotNull() throws Exception {
		assertNotNull(activity);
		ShadowView button = Robolectric.shadowOf(activity.findViewById(R.id.selectClubBtnId));
	    assertEquals(SelectDialogOnClickListener.class, button.getOnClickListener());
//		when(bandyServiceMock.getSeletionList(1, SelectionListType.CLUB_NAMES)).thenReturn(Arrays.asList(new Item(1,"club1")));
		// assertNotNull(
		// homeActivity.getSupportFragmentManager().findFragmentById( R.id. ) );
	}
//
//	// @Ignore
//	@Test
//	public void checkView() throws Exception {
//		String title = new HomeActivity().getResources().getString(R.string.home);
//		assertEquals(title, homeActivity.getTitle());
//	}
//
//	@Ignore
//	@Test
//	public void buttonClickShouldStartNewActivity() throws Exception {
//		Button button = (Button) homeActivity.findViewById(R.id.about_btn);
//		button.performClick();
//		Intent intent = Robolectric.shadowOf(homeActivity).peekNextStartedActivity();
//		// assertEquals(SecondActivity.class.getCanonicalName(),
//		// intent.getComponent().getClassName());
//	}
//
//	@Ignore
//	@Test
//	public void testButtonClick() throws Exception {
//		Button view = (Button) homeActivity.findViewById(R.id.about_btn);
//		assertNotNull(view);
//		view.performClick();
//	}
}
