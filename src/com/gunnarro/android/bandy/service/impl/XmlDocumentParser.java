package com.gunnarro.android.bandy.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.net.http.AndroidHttpClient;

import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.domain.Club;
import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.activity.Cup;
import com.gunnarro.android.bandy.domain.activity.Match;
import com.gunnarro.android.bandy.domain.activity.Match.MatchTypesEnum;
import com.gunnarro.android.bandy.domain.activity.Season;
import com.gunnarro.android.bandy.domain.activity.Training;
import com.gunnarro.android.bandy.domain.party.Address;
import com.gunnarro.android.bandy.domain.party.Contact;
import com.gunnarro.android.bandy.domain.party.Player;
import com.gunnarro.android.bandy.domain.party.Player.PlayerStatusEnum;
import com.gunnarro.android.bandy.domain.party.Referee;
import com.gunnarro.android.bandy.domain.party.Role.RoleTypesEnum;
import com.gunnarro.android.bandy.repository.table.party.ContactsTable.GenderEnum;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.service.exception.ApplicationException;
import com.gunnarro.android.bandy.service.exception.ValidationException;
import com.gunnarro.android.bandy.utility.Utility;

public class XmlDocumentParser {

	private final static String ATTR_DATE = "date";
	private final static String ATTR_START_TIME = "startTime";
	private final static String ATTR_BIRTH_DATE = "birthDate";

	private final static String ATTR_FIRST_NAME = "firstName";
	private final static String ATTR_MIDDLE_NAME = "middleName";
	private final static String ATTR_LAST_NAME = "lastName";
	private final static String ATTR_GENDER = "gender";

	private final static String ATTR_CUP_NAME = "cupName";

	private AndroidHttpClient mHttpClient;

	private InputStream getHttpsInput(String httpsUrl) {
		try {
			HostnameVerifier hostnameVerifier = new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
					return hv.verify("raw.github.com", session);
				}
			};
			URL url = new URL(httpsUrl);
			HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
			urlConnection.setHostnameVerifier(hostnameVerifier);
			return urlConnection.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private InputStream getInputStreamFromHttp(String url) {
		InputStream inputStream = null;
		try {
			final HttpGet get = new HttpGet(url);
			mHttpClient = AndroidHttpClient.newInstance("Android");
			HttpResponse response = mHttpClient.execute(get);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				final HttpEntity entity = response.getEntity();
				if (entity != null) {
					inputStream = entity.getContent();
				}
			}
		} catch (IOException e) {
			CustomLog.e(this.getClass(), "Error while retrieving XML file " + url + ", ERROR: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return inputStream;
	}

	public void downloadAndUpdateDB(Context context, String filePath, BandyService bandyService) throws Exception {
		Document doc = loadDocument(context, filePath);
		CustomLog.i(this.getClass(), "Downloaded data file..." + filePath);
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();

		String expression = "/club";
		NodeList nodeList = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
		Club club = mapAndSaveClubNode(xpath, doc, nodeList, bandyService);
		if (club == null) {
			throw new ApplicationException("Invalid xml document, Club node is missing!");
		}

		// FIXME must relate to
		// expression = "/club/contacts/contact";
		// nodeList = (NodeList) xpath.evaluate(expression, doc,
		// XPathConstants.NODESET);
		// mapAndSaveContacts(xpath, doc, null, nodeList, bandyService);

		expression = "/club/teams/team";
		nodeList = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			loadAndSaveTeam(context, getAttributeValue(nodeList.item(i), "file"), club, bandyService, xpath);
		}
		// Node mapping to domain objects and saved to repository must be done
		// in strict order
		// due to database table references. We use foreing key as references
		// between tables.
		// String expression = "/team/club";
		// NodeList nodeList = (NodeList) xpath.evaluate(expression, doc,
		// XPathConstants.NODESET);
		// Club club = mapAndSaveClubNode(nodeList, bandyService);
	}

	private Document loadDocument(Context context, String filePath) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		DocumentBuilder db = dbf.newDocumentBuilder();
		InputStream inputStream = null;
		CustomLog.d(this.getClass(), "Start loading: " + filePath);
		if (filePath.startsWith("http")) {
			inputStream = getHttpsInput(filePath);
			return db.parse(inputStream);
		} else {
			InputStream fStream = context.getAssets().open(filePath);
			return db.parse(fStream);
		}
	}

	private void loadAndSaveTeam(Context context, String teamFilePath, Club club, BandyService bandyService, XPath xpath) throws Exception {
		Document doc = loadDocument(context, teamFilePath);
		String expression = "/team";
		NodeList nodeList = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
		Team team = mapAndSaveTeamNode(club, nodeList, bandyService);
		if (team == null) {
			throw new ApplicationException("Invalid xml document, Tema node is missing!");
		}

		expression = "/team/contacts/contact";
		// expression "/team/contacts/contact[@firstName='Gunnar']/roles/role"
		nodeList = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
		mapAndSaveContacts(xpath, doc, team, nodeList, bandyService);

		// get seasons
		expression = "/team/seasons/season";
		nodeList = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i).getNodeName().equalsIgnoreCase("season")) {
				Season season = mapAndSaveSeasonNode(nodeList.item(i), bandyService);
				expression = "/team/seasons/season[@period='" + season.getPeriod() + "']/matches/match";
				nodeList = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
				mapAndSaveMatchNodes(team, season, nodeList, bandyService);

				expression = "/team/seasons/season[@period='" + season.getPeriod() + "']/trainings/training";
				nodeList = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
				mapAndSaveTrainingNodes(team, season, nodeList, bandyService);

				expression = "/team/seasons/season[@period='" + season.getPeriod() + "']/cups/cup";
				nodeList = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
				mapAndSaveCupNodes(xpath, doc, team, season, nodeList, bandyService);
			} else {
				CustomLog.e(this.getClass(), "Error parsing XML, Not a season node! nodeName=" + nodeList.item(i).getNodeName() + ", i=" + i
						+ ", node list size=" + nodeList.getLength());
				CustomLog.e(this.getClass(), "Error parsing XML, Not a season node! node=" + nodeList.item(i) + ", i=" + i);
			}
		}
		expression = "/team/players/player";
		nodeList = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
		mapAndSavePlayerNodes(xpath, doc, team, nodeList, bandyService);
	}

	private Season mapAndSaveSeasonNode(Node node, BandyService bandyService) {
		CustomLog.d(this.getClass(), "node=" + node.getNodeName());
		String period = getAttributeValue(node, "period");
		if (period == null) {
			return null;
		}
		Season existingSeason = bandyService.getSeason(period);
		if (existingSeason == null) {
			Season newSeason = new Season(period, 0, 0);
			CustomLog.d(this.getClass(), newSeason.toString());
			int seasonId = bandyService.createSeason(newSeason);
			return bandyService.getSeason(seasonId);
		}
		return existingSeason;
	}

	private Club mapAndSaveClubNode(XPath xpath, Document doc, NodeList nodeList, BandyService bandyService) throws XPathExpressionException, DOMException {
		CustomLog.d(this.getClass(), "node=" + nodeList.item(0).getNodeName());
		Club club = new Club(getAttributeValue(nodeList.item(0), "name"), getAttributeValue(nodeList.item(0), "department"));
		String xpathExprAddress = "/club/address";
		Address address = mapAddress(xpath, doc, xpathExprAddress);
		club.setAddress(address);
		CustomLog.d(this.getClass(), club.toString());
		if (bandyService.getClub(club.getName(), club.getDepartmentName()) == null) {
			bandyService.createClub(club);
		}
		return bandyService.getClub(club.getName(), club.getDepartmentName());
	}

	private Team mapAndSaveTeamNode(Club club, NodeList nodeList, BandyService bandyService) {
		Team team = new Team(getAttributeValue(nodeList.item(0), "name"), club, 0, GenderEnum.MALE.name());
		CustomLog.d(this.getClass(), team.toString());
		try {
			bandyService.getTeam(team.getName(), false);
		} catch (ValidationException ae) {
			bandyService.createTeam(team);
		}
		bandyService.updateDataFileVersion(getAttributeValue(nodeList.item(0), "version"));
		return bandyService.getTeam(team.getName(), false);
	}

	private void mapAndSavePlayerNodes(XPath xpath, Document doc, Team team, NodeList nodeList, BandyService bandyService) throws XPathExpressionException,
			DOMException {
		for (int i = 0; i < nodeList.getLength(); i++) {
			String firstName = getAttributeValue(nodeList.item(i), ATTR_FIRST_NAME);
			String lastName = getAttributeValue(nodeList.item(i), ATTR_LAST_NAME);
			String xpathExprAddress = "/team/players/player[@" + ATTR_FIRST_NAME + "='" + firstName + "' and @" + ATTR_LAST_NAME + "='" + lastName
					+ "']/address";
			Address address = mapAddress(xpath, doc, xpathExprAddress);
			List<Contact> parentList = getParentList(xpath, doc, getAttributeValue(nodeList.item(i), ATTR_FIRST_NAME),
					getAttributeValue(nodeList.item(i), ATTR_LAST_NAME));
			String status = getAttributeValue(nodeList.item(i), "status");
			Player player = new Player(-1, team, getAttributeValue(nodeList.item(i), ATTR_FIRST_NAME), getAttributeValue(nodeList.item(i), ATTR_MIDDLE_NAME),
					getAttributeValue(nodeList.item(i), ATTR_LAST_NAME), getAttributeValue(nodeList.item(i), ATTR_GENDER), PlayerStatusEnum.valueOf(status
							.toUpperCase()), parentList, Utility.timeToDate(getAttributeValue(nodeList.item(i), ATTR_BIRTH_DATE), Utility.DATE_PATTERN)
							.getTime(), address);
			CustomLog.d(this.getClass(), player.toString());
			bandyService.createPlayer(player);
		}
	}

	private void mapAndSaveCupNodes(XPath xpath, Document doc, Team team, Season season, NodeList nodeList, BandyService bandyService)
			throws XPathExpressionException {
		for (int i = 0; i < nodeList.getLength(); i++) {
			String cupName = getAttributeValue(nodeList.item(i), "cupName");
			String dateTimeStr = getAttributeValue(nodeList.item(i), ATTR_DATE) + " " + getAttributeValue(nodeList.item(i), ATTR_START_TIME);
			Cup cup = new Cup(season, Utility.timeToDate(dateTimeStr, "dd.MM.yyyy HH:mm").getTime(), getAttributeValue(nodeList.item(i), "clubName"), cupName,
					getAttributeValue(nodeList.item(i), "venue"), Utility.timeToDate(getAttributeValue(nodeList.item(i), "deadlineDate"), "dd.MM.yyyy")
							.getTime());
			CustomLog.d(this.getClass(), cup.toString());
			int cupId = bandyService.createCup(cup);
			// Add matches for this cup, if any...
			List<Match> cupMatchList = this.getCupMatchList(team, xpath, doc, season, cupName);
			for (Match match : cupMatchList) {
				bandyService.createMatchForCup(match, cupId);
			}
		}
	}

	private void mapAndSaveTrainingNodes(Team team, Season season, NodeList nodeList, BandyService bandyService) {
		for (int i = 0; i < nodeList.getLength(); i++) {
			String dateTimeStr = getAttributeValue(nodeList.item(i), ATTR_DATE) + " " + getAttributeValue(nodeList.item(i), "fromTime");
			String toTimeStr = getAttributeValue(nodeList.item(i), "toTime");
			Training training = new Training(season, Utility.timeToDate(dateTimeStr, "dd.MM.yyyy HH:mm").getTime(), Utility.timeToDate(toTimeStr, "HH:mm")
					.getTime(), new Team(team.getId(), team.getName()), getAttributeValue(nodeList.item(i), "place"));
			CustomLog.d(this.getClass(), training.toString());
			bandyService.createTraining(training);
		}
	}

	private void mapAndSaveMatchNodes(Team team, Season season, NodeList nodeList, BandyService bandyService) {
		for (int i = 0; i < nodeList.getLength(); i++) {
			Match match = mapNodeToMatch(team, season, nodeList.item(i));
			CustomLog.d(this.getClass(), match.toString());
			bandyService.createMatch(match);
		}
	}

	private void mapAndSaveContacts(XPath xpath, Document doc, Team team, NodeList nodeList, BandyService bandyService) throws XPathExpressionException,
			DOMException {
		for (int i = 0; i < nodeList.getLength(); i++) {
			// Get the contact node roles child node
			List<RoleTypesEnum> roleList = getRoleList(xpath, doc, getAttributeValue(nodeList.item(i), ATTR_FIRST_NAME),
					getAttributeValue(nodeList.item(i), ATTR_LAST_NAME));
			Address address = Address.createEmptyAddress();
			if (nodeList.item(i).hasChildNodes()) {
				String firstName = getAttributeValue(nodeList.item(i), ATTR_FIRST_NAME);
				String lastName = getAttributeValue(nodeList.item(i), ATTR_LAST_NAME);
				String xpathExprAddress = "/team/players/player[@" + ATTR_FIRST_NAME + "='" + firstName + "' and @" + ATTR_LAST_NAME + "='" + lastName
						+ "']/address";
				address = mapAddress(xpath, doc, xpathExprAddress);
			}
			Contact contact = new Contact(new Team(team.getId(), team.getName()), roleList, getAttributeValue(nodeList.item(i), ATTR_FIRST_NAME),
					getAttributeValue(nodeList.item(i), ATTR_MIDDLE_NAME), getAttributeValue(nodeList.item(i), ATTR_LAST_NAME), getAttributeValue(
							nodeList.item(i), ATTR_GENDER), getAttributeValue(nodeList.item(i), "mobile"), getAttributeValue(nodeList.item(i), "email"),
					address);
			CustomLog.d(this.getClass(), contact.toString());
			bandyService.createContact(contact);
		}
	}

	private Address mapAddress(XPath xpath, Document doc, String xpathExprAddress) throws XPathExpressionException, DOMException {
		Node addressNode = (Node) xpath.evaluate(xpathExprAddress, doc, XPathConstants.NODE);
		if (addressNode != null) {
			Address address = new Address(getAttributeValue(addressNode, "streetName"), getAttributeValue(addressNode, "streetNumber"), getAttributeValue(
					addressNode, "streetNumberPrefix"), getAttributeValue(addressNode, "zipCode"), getAttributeValue(addressNode, "city"), getAttributeValue(
					addressNode, "country"));
			CustomLog.d(this.getClass(), "mappedXmlAddress=" + address.toString());
			return address;
		}
		return Address.createEmptyAddress();
	}

	private List<RoleTypesEnum> getRoleList(XPath xpath, Document doc, String firstName, String lastName) throws XPathExpressionException {
		String xpathExprRoles = "/team/contacts/contact[@" + ATTR_FIRST_NAME + "='" + firstName + "' and @" + ATTR_LAST_NAME + "='" + lastName
				+ "']/roles/role";
		NodeList nodeList = (NodeList) xpath.evaluate(xpathExprRoles, doc, XPathConstants.NODESET);
		List<RoleTypesEnum> roles = new ArrayList<RoleTypesEnum>();
		for (int j = 0; j < nodeList.getLength(); j++) {
			Node roleNode = nodeList.item(j);
			// Read only text node
			CustomLog.d(this.getClass(), "node=" + roleNode.getNodeName() + " " + roleNode.getNodeType() + " " + roleNode.getTextContent());
			try {
				roles.add(RoleTypesEnum.valueOf(roleNode.getTextContent().toUpperCase()));
			} catch (Exception e) {
				CustomLog.e(this.getClass(), "contact=" + firstName + ", Invalid status: " + roleNode.getNodeName() + "=" + roleNode.getTextContent());
			}
		}
		return roles;
	}

	private Match mapNodeToMatch(Team team, Season season, Node matchNode) {
		String dateTimeStr = getAttributeValue(matchNode, ATTR_DATE) + " " + getAttributeValue(matchNode, ATTR_START_TIME);
		MatchTypesEnum matchType = MatchTypesEnum.toType(Integer.parseInt(getAttributeValue(matchNode, "typeId")));
		return new Match(null, season, Utility.timeToDate(dateTimeStr, "dd.MM.yyyy HH:mm").getTime(), new Team(team.getId(), team.getName()), new Team(
				getAttributeValue(matchNode, "homeTeam")), new Team(getAttributeValue(matchNode, "awayTeam")), Integer.parseInt(getAttributeValue(matchNode,
				"goalsHomeTeam")), Integer.parseInt(getAttributeValue(matchNode, "goalsAwayTeam")), getAttributeValue(matchNode, "venue"), new Referee(
				getAttributeValue(matchNode, "referee"), null, getAttributeValue(matchNode, "referee")), matchType, getAttributeValue(matchNode, "status"));
	}

	private List<Match> getCupMatchList(Team team, XPath xpath, Document doc, Season season, String cupName) throws XPathExpressionException {
		String xpathExprParents = "/team/seasons/season[@period='" + season.getPeriod() + "']/cups/cup[@" + ATTR_CUP_NAME + "='" + cupName + "']/matches/match";
		CustomLog.d(this.getClass(), "xpath expr=" + xpathExprParents);
		NodeList nodeList = (NodeList) xpath.evaluate(xpathExprParents, doc, XPathConstants.NODESET);
		List<Match> matchList = new ArrayList<Match>();
		for (int j = 0; j < nodeList.getLength(); j++) {
			Node parentNode = nodeList.item(j);
			try {
				matchList.add(mapNodeToMatch(team, season, parentNode));
			} catch (Exception e) {
				CustomLog.e(this.getClass(), "cup=" + cupName + ", Invalid status: " + parentNode.getNodeName() + "=" + parentNode.getTextContent());
				CustomLog.e(this.getClass(), e.toString());
			}
		}
		return matchList;
	}

	private List<Contact> getParentList(XPath xpath, Document doc, String firstName, String lastName) throws XPathExpressionException {
		String xpathExprParents = "/team/players/player[@" + ATTR_FIRST_NAME + "='" + firstName + "' and @" + ATTR_LAST_NAME + "='" + lastName
				+ "']/parents/parent";
		CustomLog.d(this.getClass(), "xpath expr=" + xpathExprParents);
		NodeList nodeList = (NodeList) xpath.evaluate(xpathExprParents, doc, XPathConstants.NODESET);
		List<Contact> parentList = new ArrayList<Contact>();
		for (int j = 0; j < nodeList.getLength(); j++) {
			Node parentNode = nodeList.item(j);
			try {
				parentList.add(new Contact(null, null, getAttributeValue(parentNode, ATTR_FIRST_NAME), getAttributeValue(parentNode, ATTR_MIDDLE_NAME),
						getAttributeValue(parentNode, ATTR_LAST_NAME), getAttributeValue(parentNode, ATTR_GENDER)));
			} catch (Exception e) {
				CustomLog.e(this.getClass(), "contact=" + firstName + ", Invalid status: " + parentNode.getNodeName() + "=" + parentNode.getTextContent());
				CustomLog.e(this.getClass(), e.toString());
			}
		}
		return parentList;
	}

	private String getAttributeValue(Node node, String name) {
		String value = null;
		if (node == null) {
			throw new RuntimeException("Error node is null! name=" + name);
		}
		if (node.hasAttributes() && node.getAttributes().getNamedItem(name) != null) {
			value = node.getAttributes().getNamedItem(name).getNodeValue().trim();
		} else {
			CustomLog.e(this.getClass(), "node=" + node.getNodeName() + ", hasAttribuest=" + node.hasAttributes() + ", value=" + node.getNodeValue()
					+ ", text=" + node.getTextContent() + ", attribute=" + name + ", is missing!");
		}
		return value;
	}
}