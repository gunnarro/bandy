package com.gunnarro.android.bandy.service.impl;

import java.io.File;
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
import javax.xml.xpath.XPathFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.net.http.AndroidHttpClient;

import com.gunnarro.android.bandy.custom.CustomLog;
import com.gunnarro.android.bandy.domain.Club;
import com.gunnarro.android.bandy.domain.Contact;
import com.gunnarro.android.bandy.domain.Contact.ContactRoleEnum;
import com.gunnarro.android.bandy.domain.Cup;
import com.gunnarro.android.bandy.domain.Match;
import com.gunnarro.android.bandy.domain.Referee;
import com.gunnarro.android.bandy.domain.Team;
import com.gunnarro.android.bandy.domain.Traning;
import com.gunnarro.android.bandy.service.BandyService;
import com.gunnarro.android.bandy.utility.Utility;

public class XmlDocumentParser {

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

	public void testParseByXpath(String filePath, BandyService bandyService) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		DocumentBuilder db = dbf.newDocumentBuilder();

		// InputStream inputStream = getHttpsInput(url);
		// Document doc = db.parse(inputStream);
		Document doc = db.parse(new File(filePath));
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();

		// Node mapping to domain objects and saved to repository must be done
		// in strict order
		// due to database table references. We use foreing key as references
		// between tables.
		String expression = "/team/club";
		NodeList nodeList = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
		Club club = mapAndSaveClubNode(nodeList, bandyService);

		expression = "/team";
		nodeList = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
		Team team = mapAndSaveTeamNode(club, nodeList, bandyService);

		expression = "/team/contacts";
		nodeList = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
		mapAndSaveContacts(team, nodeList, bandyService);

		expression = "/team/matches/match";
		nodeList = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
		mapAndSaveMatchNodes(team, nodeList, bandyService);

		expression = "/team/trainings/traning";
		nodeList = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
		mapAndSaveTraningNodes(team, nodeList, bandyService);

		expression = "/team/cups/cup";
		nodeList = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
		mapAndSaveCupNodes(nodeList, bandyService);

		expression = "/team/players/player";
		nodeList = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
		mapAndSavePlayerNodes(team, nodeList, bandyService);

		expression = "/team/parents/parent";
		nodeList = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
		mapAndSaveParentNodes(nodeList, bandyService);
	}

	private Club mapAndSaveClubNode(NodeList nodeList, BandyService bandyService) {
		CustomLog.d(this.getClass(), "NODE=" + nodeList.item(0).getNodeName());
		NamedNodeMap attrMap = nodeList.item(0).getAttributes();
		Club club = new Club(attrMap.getNamedItem("name").getNodeValue());
		CustomLog.d(this.getClass(), club.toString());
		if (bandyService.getClub(club.getName()) == null) {
			bandyService.createClub(club);
		}
		return bandyService.getClub(club.getName());
	}

	private Team mapAndSaveTeamNode(Club club, NodeList nodeList, BandyService bandyService) {
		NamedNodeMap attrMap = nodeList.item(0).getAttributes();
		Team team = new Team(attrMap.getNamedItem("name").getNodeValue(), club);
		CustomLog.d(this.getClass(), team.toString());
		if (bandyService.getTeam(team.getName()) == null) {
			bandyService.createTeam(team);
		}
		bandyService.updateDataFileVersion(attrMap.getNamedItem("version").getNodeValue());
		return bandyService.getTeam(team.getName());
	}

	private void mapAndSaveParentNodes(NodeList nodeList, BandyService bandyService) {

	}

	private void mapAndSavePlayerNodes(Team team, NodeList nodeList, BandyService bandyService) {
	}

	private void mapAndSaveCupNodes(NodeList nodeList, BandyService bandyService) {
		for (int i = 0; i < nodeList.getLength(); i++) {
			NamedNodeMap attrMap = nodeList.item(i).getAttributes();
			String dateTimeStr = attrMap.getNamedItem("date").getNodeValue() + " " + attrMap.getNamedItem("startTime").getNodeValue();
			Cup cup = new Cup(Utility.timeToDate(dateTimeStr, "dd.MM.yyyy HH:mm").getTime(), attrMap.getNamedItem("clubName").getNodeValue(), attrMap
					.getNamedItem("cupName").getNodeValue(), attrMap.getNamedItem("venue").getNodeValue(), Utility.timeToDate(
					attrMap.getNamedItem("deadlineDate").getNodeValue(), "dd.MM.yyyy").getTime());
			CustomLog.d(this.getClass(), cup.toString());
			bandyService.createCup(cup);
		}
	}

	private void mapAndSaveTraningNodes(Team team, NodeList nodeList, BandyService bandyService) {
		for (int i = 0; i < nodeList.getLength(); i++) {
			NamedNodeMap attrMap = nodeList.item(i).getAttributes();
			String dateTimeStr = attrMap.getNamedItem("date").getNodeValue() + " " + attrMap.getNamedItem("fromTime").getNodeValue();
			String toTimeStr = attrMap.getNamedItem("toTime").getNodeValue();
			Traning traning = new Traning(Utility.timeToDate(dateTimeStr, "dd.MM.yyyy HH:mm").getTime(), Utility.timeToDate(toTimeStr, "HH:mm").getTime(),
					new Team(team.getId(), team.getName()), attrMap.getNamedItem("place").getNodeValue());
			CustomLog.d(this.getClass(), traning.toString());
			bandyService.createTraning(traning);
		}
	}

	private void mapAndSaveMatchNodes(Team team, NodeList nodeList, BandyService bandyService) {
		for (int i = 0; i < nodeList.getLength(); i++) {
			NamedNodeMap attrMap = nodeList.item(i).getAttributes();
			String dateTimeStr = attrMap.getNamedItem("date").getNodeValue() + " " + attrMap.getNamedItem("startTime").getNodeValue();
			Match match = new Match(Utility.timeToDate(dateTimeStr, "dd.MM.yyyy HH:mm").getTime(), new Team(team.getId(), team.getName()), new Team(attrMap
					.getNamedItem("homeTeam").getNodeValue()), new Team(attrMap.getNamedItem("awayTeam").getNodeValue()), attrMap.getNamedItem("venue")
					.getNodeValue(), new Referee(attrMap.getNamedItem("referee").getNodeValue()));
			CustomLog.d(this.getClass(), match.toString());
			bandyService.createMatch(match);
		}
	}

	private void mapAndSaveContacts(Team team, NodeList nodeList, BandyService bandyService) {
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node contactNode = nodeList.item(i);
			NodeList roleNodes = contactNode.getChildNodes();
			List<ContactRoleEnum> roles = new ArrayList<ContactRoleEnum>();
			for (int j = 0; j < roleNodes.getLength(); j++) {
				Node roleNode = roleNodes.item(j);
				roles.add(ContactRoleEnum.valueOf(roleNode.getNodeValue().toUpperCase()));
			}
			NamedNodeMap attrMap = contactNode.getAttributes();
			Contact contact = new Contact(new Team(team.getId(), team.getName()), roles, attrMap.getNamedItem("firstName").getNodeValue(), attrMap
					.getNamedItem("middleName").getNodeValue(), attrMap.getNamedItem("lastName").getNodeValue(), attrMap.getNamedItem("mobile").getNodeValue(),
					attrMap.getNamedItem("email").getNodeValue(), null);

			CustomLog.d(this.getClass(), contact.toString());
			bandyService.createContact(contact);
		}

	}
}
