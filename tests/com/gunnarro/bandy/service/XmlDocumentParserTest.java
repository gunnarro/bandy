package com.gunnarro.bandy.service;

import junit.framework.TestCase;

import com.gunnarro.android.bandy.service.impl.DataLoader;
import com.gunnarro.android.bandy.service.impl.XmlDocumentParser;

public class XmlDocumentParserTest extends TestCase {

	public void testParseXml() throws Exception {
		XmlDocumentParser parser = new XmlDocumentParser();
		parser.testParseByXpath(DataLoader.TEAM_XML_URL, null);
	}

}
