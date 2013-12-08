package com.gunnarro.android.bandy.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;

import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.domain.Activity;
import com.gunnarro.android.bandy.domain.Activity.ActivityTypeEnum;
import com.gunnarro.android.bandy.domain.Club;
import com.gunnarro.android.bandy.domain.Contact;
import com.gunnarro.android.bandy.domain.Cup;
import com.gunnarro.android.bandy.domain.Match;
import com.gunnarro.android.bandy.domain.Player;
import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.Training;
import com.gunnarro.android.bandy.repository.BandyRepository;
import com.gunnarro.android.bandy.repository.impl.BandyRepositoryImpl;
import com.gunnarro.android.bandy.repository.table.SettingsTable;
import com.gunnarro.android.bandy.service.BandyService;

public class BandyServiceImpl implements BandyService {

	private Context context;
	private BandyRepository bandyRepository;
	private XmlDocumentParser xmlParser;

	/**
	 * default constructor, used for unit testing only.
	 */
	public BandyServiceImpl() {
	}

	/**
	 * 
	 * @param context
	 */
	public BandyServiceImpl(Context context) {
		this.context = context;
		this.bandyRepository = new BandyRepositoryImpl(this.context);
		this.bandyRepository.open();
		this.xmlParser = new XmlDocumentParser();
	}

	public Context getContext() {
		return context;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void loadData(String filePath) {
		try {
			this.xmlParser.testParseByXpath(filePath, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createClub(Club club) {
		this.bandyRepository.createClub(club);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createTeam(Team team) {
		this.bandyRepository.createTeam(team);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createMatch(Match match) {
		this.bandyRepository.createMatch(match);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createCup(Cup cup) {
		this.bandyRepository.createCup(cup);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createTraining(Training training) {
		this.bandyRepository.createTraining(training);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createPlayer(Player player) {
		this.bandyRepository.createPlayer(player);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createContact(Contact contact) {
		this.bandyRepository.createContact(contact);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Team getTeam(String name) {
		return this.bandyRepository.getTeam(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Team getTeam(Integer id) {
		return this.bandyRepository.getTeam(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Club getClub(String name) {
		return this.bandyRepository.getClub(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Club getClub(Integer id) {
		return this.bandyRepository.getClub(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Activity> getActivityList(String teamName, String periode, String filterBy) {
		Team team = getTeam(teamName);
		List<Activity> list = new ArrayList<Activity>();
		if (team == null) {
			CustomLog.e(this.getClass(), "No team found for: " + teamName);
			return list;
		}
		if (filterBy.equalsIgnoreCase("All") || filterBy.equalsIgnoreCase(Activity.ActivityTypeEnum.MATCH.name())) {
			for (Match match : getMatchList(team.getId(), periode)) {
				list.add(new Activity(ActivityTypeEnum.MATCH, match.getStartDate(), match.getVenue(), match.getTeamVersus()));
			}
		}
		if (filterBy.equalsIgnoreCase("All") || filterBy.equalsIgnoreCase(Activity.ActivityTypeEnum.TRAINING.name())) {
			for (Training training : getTrainingList(team.getId(), periode)) {
				list.add(new Activity(ActivityTypeEnum.TRAINING, training.getStartDate(), training.getVenue(), training.getTeam().getName()));
			}
		}
		if (filterBy.equalsIgnoreCase("All") || filterBy.equalsIgnoreCase(Activity.ActivityTypeEnum.CUP.name())) {
			for (Cup cup : getCupList(team.getId(), periode)) {
				list.add(new Activity(ActivityTypeEnum.CUP, cup.getStartDate(), cup.getVenue(), cup.getCupName()));
			}
		}
		Collections.sort(list);
		return list;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param teamId
	 * 
	 * @param periode
	 */
	@Override
	public List<Match> getMatchList(Integer teamId, String periode) {
		return this.bandyRepository.getMatchList(teamId, periode);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param periode
	 */
	@Override
	public List<Training> getTrainingList(Integer teamId, String periode) {
		return this.bandyRepository.getTrainingList(teamId, periode);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param teamId
	 */
	@Override
	public List<Cup> getCupList(Integer teamId, String periode) {
		return this.bandyRepository.getCupList(teamId, periode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Player> getPlayerList(String teamName) {
		return this.bandyRepository.getPlayerList(teamName);
	}

	// ---------------------------------------------------------------------------
	// Settings table operations
	// ---------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateDataFileUrl(String url) {
		this.bandyRepository.updateSetting(SettingsTable.DATA_FILE_URL, url);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDataFileUrl() {
		return this.bandyRepository.getSetting(SettingsTable.DATA_FILE_URL);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateDataFileVersion(String version) {
		this.bandyRepository.updateSetting(SettingsTable.DATA_FILE_VERSION, version);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDataFileVersion() {
		return this.bandyRepository.getSetting(SettingsTable.DATA_FILE_VERSION);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateDataFileLastUpdated(long lastUpdatedTime) {
		this.bandyRepository.updateSetting(SettingsTable.DATA_FILE_LAST_UPDATED, Long.toString(lastUpdatedTime));
		;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getDataFileLastUpdated() {
		return Long.parseLong(this.bandyRepository.getSetting(SettingsTable.DATA_FILE_LAST_UPDATED));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getEmailAccount() {
		return this.bandyRepository.getSetting(SettingsTable.MAIL_ACCOUNT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getEmailAccountPwd() {
		return this.bandyRepository.getSetting(SettingsTable.MAIL_ACCOUNT_PWD);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateEmailAccount(String mailAccount) {
		this.bandyRepository.updateSetting(SettingsTable.MAIL_ACCOUNT, mailAccount);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateEmailAccountPwd(String mailAccountPwd) {
		this.bandyRepository.updateSetting(SettingsTable.MAIL_ACCOUNT_PWD, mailAccountPwd);
	}
}
