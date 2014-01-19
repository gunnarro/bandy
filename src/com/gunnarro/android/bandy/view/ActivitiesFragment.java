package com.gunnarro.android.bandy.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.gunnarro.android.bandy.R;
import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.domain.Activity;
import com.gunnarro.android.bandy.domain.Activity.ActivityTypeEnum;
import com.gunnarro.android.bandy.domain.Contact;
import com.gunnarro.android.bandy.domain.Role;
import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.mail.MailSender;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.exception.ApplicationException;
import com.gunnarro.android.bandy.service.impl.BandyServiceImpl;
import com.gunnarro.android.bandy.utility.Utility;

public class ActivitiesFragment extends Fragment {

	protected BandyService bandyService;
	private String viewBy = "Week";
	private String activityFilter = ActivityTypeEnum.MATCH.name();
	private String selectedTeamName = "%2003";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		if (container == null) {
			return null;
		}

		View view = inflater.inflate(R.layout.team_activites_layout, container, false);

		Spinner teamsSpinner = (Spinner) view.findViewById(R.id.teams_spinner);
		ArrayAdapter<CharSequence> teamsAdapter = ArrayAdapter
				.createFromResource(view.getContext(), R.array.team_options, android.R.layout.simple_spinner_item);
		teamsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		teamsSpinner.setAdapter(teamsAdapter);
		teamsSpinner.setOnItemSelectedListener(new TeamOnItemSelectedListener(view));

		Spinner viewBySpinner = (Spinner) view.findViewById(R.id.view_by_spinner);
		ArrayAdapter<CharSequence> viewByAdapter = ArrayAdapter.createFromResource(view.getContext(), R.array.activities_view_by_options,
				android.R.layout.simple_spinner_item);
		viewByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		viewBySpinner.setAdapter(viewByAdapter);
		viewBySpinner.setOnItemSelectedListener(new ViewByOnItemSelectedListener(view));

		Spinner activityFilterSpinner = (Spinner) view.findViewById(R.id.activity_filter_spinner);
		ArrayAdapter<CharSequence> activityFilterAdapter = ArrayAdapter.createFromResource(view.getContext(), R.array.activitiy_filter_options,
				android.R.layout.simple_spinner_item);
		activityFilterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		activityFilterSpinner.setAdapter(activityFilterAdapter);
		activityFilterSpinner.setOnItemSelectedListener(new ActivityFilterOnItemSelectedListener(view));

		this.bandyService = new BandyServiceImpl(view.getContext());
		setupEventHandlers(view);
		addTestData();
		return view;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private void setupEventHandlers(final View statView) {
		ImageButton sendMailButton = (ImageButton) statView.findViewById(R.id.activity_send_mail);
		sendMailButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// SendEmailTask task = new SendEmailTask();
				// task.execute((Void[]) null);
				Intent intent = new Intent(getActivity().getApplicationContext(), NotificationActivity.class);
				Team team = bandyService.getTeam(selectedTeamName, true);
				intent.putExtra("teamName", team.getName());
				intent.putStringArrayListExtra("toEmail", new ArrayList<String>(team.getEmailAddresseForContacts()));
				intent.putStringArrayListExtra("toMobile", new ArrayList<String>(team.getMobileNrForContacts()));
				intent.putExtra("fromEmail", team.getTeamLead().getEmailAddress());
				intent.putExtra("fromMobile", team.getTeamLead().getMobileNumber());
				intent.putExtra("message", composeMessage(team));
				intent.putExtra("periode", viewBy);
				getActivity().startService(intent);
				startActivity(intent);
			}
		});
	}

	private void updateActivitesData(View statView) {
		TableLayout table = (TableLayout) statView.findViewById(R.id.tableLayout);
		if (table == null) {
			return;
		}
		// Remove all rows before updating the table, except for the table
		// header rows.
		clearTableData(statView);
		List<Activity> activityList = this.bandyService.getActivityList(selectedTeamName, viewBy, activityFilter);

		Date startDate = activityList.isEmpty() ? new Date() : new Date(activityList.get(0).getStartDate());
		Date endDate = activityList.isEmpty() ? new Date() : new Date(activityList.get(activityList.size() - 1).getStartDate());
		for (Activity activity : activityList) {
			table.addView(createTableRow(statView, activity, table.getChildCount()));
		}
		Utility.getDateFormatter().applyPattern("dd.MM.yyyy");
		String periode = Utility.getDateFormatter().format(startDate) + " - " + Utility.getDateFormatter().format(endDate);
		TextView tableHeaderTxt = (TextView) statView.findViewById(R.id.tableHeaderPeriod);
		tableHeaderTxt.setText(getResources().getString(R.string.activities_period) + ": " + periode);
	}

	private TableRow createTableRow(View statView, Activity activity, int rowNumber) {
		TableRow row = new TableRow(statView.getContext());
		int rowBgColor = getResources().getColor(R.color.white);
		int txtColor = getResources().getColor(R.color.black);
		int numberColor = getActivityTypeColor(activity.getType());
		Utility.getDateFormatter().applyPattern("dd.MM.yyyy");
		row.addView(createTextView(statView, Utility.getDateFormatter().format(new Date(activity.getStartDate())), rowBgColor, txtColor, Gravity.CENTER));
		Utility.getDateFormatter().applyPattern("HH:mm");
		row.addView(createTextView(statView, Utility.getDateFormatter().format(new Date(activity.getStartDate())), rowBgColor, txtColor, Gravity.CENTER));
		row.addView(createTextView(statView, activity.getDescription(), rowBgColor, numberColor, Gravity.LEFT));
		row.addView(createTextView(statView, activity.getPlace(), rowBgColor, numberColor, Gravity.CENTER));

		row.setBackgroundColor(rowBgColor);
		row.setPadding(1, 1, 1, 1);
		return row;
	}

	private TextView createTextView(View statView, String value, int bgColor, int txtColor, int gravity) {
		TextView txtView = new TextView(statView.getContext());
		txtView.setText(value);
		txtView.setGravity(gravity);
		txtView.setBackgroundColor(bgColor);
		txtView.setTextColor(txtColor);
		txtView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
		return txtView;
	}

	private void clearTableData(View statView) {
		TableLayout table = (TableLayout) statView.findViewById(R.id.tableLayout);
		if (table == null) {
			return;
		}
		CustomLog.i(this.getClass(), "...child: " + table.getChildCount());
		if (table.getChildCount() > 3) {
			table.removeViews(3, table.getChildCount() - 3);
			CustomLog.i(this.getClass(), "Removed rows from view: " + (table.getChildCount() - 3));
		}
	}

	private int getActivityTypeColor(ActivityTypeEnum type) {
		switch (type) {
		case CUP:
			return getResources().getColor(R.color.dark_green);
		case MATCH:
			return getResources().getColor(R.color.dark_blue);
		case TRAINING:
			return getResources().getColor(R.color.black);
		default:
			return getResources().getColor(R.color.black);
		}
	}

	/**
	 * for unit testing only
	 */
	@Deprecated
	private void addTestData() {
		int nextInt = new Random().nextInt(100);
		// this.bandyService.createMatch(new Match(100,
		// System.currentTimeMillis(), new Team("Ullev�l"), new Team("Ullev�l"),
		// new Team("R�a 03"), "Bergbanen",
		// new Referee("Ukjent")));
		// this.bandyService.createMatch(new Match(101,
		// System.currentTimeMillis(), new Team("Ullev�l"), new Team("Ullev�l"),
		// new Team("Ready 03"), "Bergbanen",
		// new Referee("Ukjent")));
		// this.bandyService.createMatch(new Match(102,
		// System.currentTimeMillis(), new Team("Ullev�l"), new Team("Ready 2"),
		// new Team("Ullev�l"), "Gressbanen",
		// new Referee("Ukjent")));
		//
		// this.bandyService.createCup(new Cup(System.currentTimeMillis(),
		// "Frigg", "Frigg Cup", "Frogner Stadium",
		// System.currentTimeMillis()));

		// this.bandyService.createTraining(new Training(102,
		// System.currentTimeMillis(), new Team("Ready 2"), new Team("Ullev�l"),
		// "Gressbanen", new Referee(
		// "Ukjent")));

	}

	/**
	 * 
	 * @author gunnarro
	 * 
	 */
	public class ViewByOnItemSelectedListener implements OnItemSelectedListener {
		final View activitesView;

		public ViewByOnItemSelectedListener(final View activitesView) {
			this.activitesView = activitesView;
		}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			viewBy = parent.getItemAtPosition(pos).toString();
			// update the table upon item selection
			updateActivitesData(activitesView);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// Do nothing.
		}
	}

	private String composeMessage(Team team) {
		List<Activity> activityList = this.bandyService.getActivityList(selectedTeamName, viewBy, activityFilter);
		String msg = Utility.createActivitiesHtmlTable(team, activityList);
		StringBuffer regards = new StringBuffer();
		regards.append(msg);
		regards.append("\n\n\n");
		regards.append("Vennlig hilsen").append("\n");
		regards.append(team.getTeamLead().getFullName()).append("\n");
		regards.append(team.getClub().getName()).append("\n");
		regards.append("Trener/Lagleder\n");
		regards.append(team.getName()).append("\n");
		regards.append("Mobile: ").append(team.getTeamLead().getMobileNumber()).append("\n");
		regards.append("Homepage: ").append(team.getClub().getHomepage()).append("\n");
		return regards.toString();
	}

	private void sendMail() {
		List<Activity> activityList = this.bandyService.getActivityList(selectedTeamName, viewBy, activityFilter);
		MailSender mailSender = new MailSender(this.bandyService.getEmailAccount(), this.bandyService.getEmailAccountPwd());
		Team team = this.bandyService.getTeam(selectedTeamName, true);
		String msg = Utility.createActivitiesHtmlTable(team, activityList);
		String subject = team.getName() + " aktiviteter for " + viewBy;
		StringBuffer regards = new StringBuffer();
		List<String> recipients = new ArrayList<String>();
		List<Contact> contactList = this.bandyService.getContactList(team.getId());
		for (Contact c : contactList) {
			CustomLog.e(this.getClass(), c.toString());
			recipients.add(c.getEmailAddress());
		}
		List<Role> roleList = this.bandyService.getRoleList();
		for (Role r : roleList) {
			CustomLog.e(this.getClass(), r.toString());
		}
		CustomLog.e(this.getClass(), recipients.toString());
		Contact teamLead = this.bandyService.getTeamLead(team.getId());
		regards.append("\n\n\n");
		regards.append("Vennlig hilsen").append("\n");
		regards.append(teamLead.getFullName()).append("\n");
		regards.append(team.getClub().getName()).append("\n");
		regards.append("Trener/Lagleder\n");
		regards.append(team.getName()).append("\n");
		regards.append("Mobile: ").append(teamLead.getMobileNumber()).append("\n");
		regards.append("Homepage: ").append(team.getClub().getHomepage()).append("\n");
		try {
			CustomLog.d(this.getClass(), "email msg=" + msg);
			CustomLog.d(this.getClass(), "email regards=" + regards.toString());
			// mailSender.sendMail(subject, msg + regards.toString(),
			// "gunnar.ronneberg@gmail.com", recipients);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @author gunnarro
	 * 
	 */
	public class ActivityFilterOnItemSelectedListener implements OnItemSelectedListener {
		final View activitesView;

		public ActivityFilterOnItemSelectedListener(final View activitesView) {
			this.activitesView = activitesView;
		}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			activityFilter = parent.getItemAtPosition(pos).toString();
			// update the table upon item selection
			updateActivitesData(activitesView);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// Do nothing.
		}
	}

	/**
	 * 
	 * @author gunnarro
	 * 
	 */
	public class TeamOnItemSelectedListener implements OnItemSelectedListener {
		final View activitesView;

		public TeamOnItemSelectedListener(final View activitesView) {
			this.activitesView = activitesView;
		}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			selectedTeamName = parent.getItemAtPosition(pos).toString();
			// update the table upon item selection
			updateActivitesData(activitesView);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// Do nothing.
		}
	}

	/**
	 * Asynch task for sending email
	 * 
	 * @author admin
	 * 
	 */
	class SendEmailTask extends AsyncTask<Void, Void, Void> {
		private ProgressDialog pdialog;

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pdialog = ProgressDialog.show(getActivity(), "", "Sending Email...", true);
		}

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
				sendMail();
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
