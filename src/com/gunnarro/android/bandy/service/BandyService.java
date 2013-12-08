package com.gunnarro.android.bandy.service;

import java.util.List;

import com.gunnarro.android.bandy.domain.Activity;
import com.gunnarro.android.bandy.domain.Club;
import com.gunnarro.android.bandy.domain.Contact;
import com.gunnarro.android.bandy.domain.Cup;
import com.gunnarro.android.bandy.domain.Match;
import com.gunnarro.android.bandy.domain.Player;
import com.gunnarro.android.bandy.domain.Setting;
import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.Training;

public interface BandyService {

	public void loadData(String filePath);

	public void createClub(Club club);

	public void createTeam(Team team);

	public void createMatch(Match match);

	public void createCup(Cup cup);

	public void createTraining(Training training);

	public void createPlayer(Player player);

	public void createContact(Contact contact);

	// -------------------------------------------------------------------

	public Team getTeam(String name);

	public Team getTeam(Integer id);

	public Club getClub(String name);

	public Club getClub(Integer id);

	public List<Match> getMatchList(Integer teamId, String periode);

	public List<Training> getTrainingList(Integer teamId, String periode);

	public List<Cup> getCupList(Integer teamId, String periode);

	public List<Activity> getActivityList(String teamName, String viewBy, String filterBy);

	public List<Player> getPlayerList(String teamName);

	// ---------------------------------------------------------------------------
	// Settings table operations
	// ---------------------------------------------------------------------------
	public void updateDataFileUrl(String url);

	public String getDataFileUrl();

	public void updateDataFileVersion(String version);

	public String getDataFileVersion();

	public void updateDataFileLastUpdated(long lastUpdatedTime);

	public long getDataFileLastUpdated();

	public String getEmailAccount();

	public String getEmailAccountPwd();

	public void updateEmailAccount(String mailAccount);

	public void updateEmailAccountPwd(String mailAccountPwd);

}
