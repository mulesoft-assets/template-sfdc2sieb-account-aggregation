/**
 * Mule Anypoint Template
 * Copyright (c) MuleSoft, Inc.
 * All rights reserved.  http://www.mulesoft.com
 */

package org.mule.templates.transformers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleContext;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.templates.utils.Utils;
import org.mule.templates.utils.VariableNames;

@RunWith(MockitoJUnitRunner.class)
public class SortAccountListTransformerTest {
	
	private static final Logger log = LogManager.getLogger(SortAccountListTransformerTest.class);
	
	@Mock
	private MuleContext muleContext;

	@Test
	public void testSort() throws TransformerException {
		MuleMessage message = new DefaultMuleMessage(createOriginalList(), muleContext);

		SortAccountListTransformer transformer = new SortAccountListTransformer();
		List<Map<String, String>> sortedList = Utils.buildList(transformer.transform(message, "UTF-8"));

		log.info(sortedList);
		Assert.assertEquals("The merged list obtained is not as expected", createExpectedList(), sortedList);
	}

	private List<Map<String, String>> createExpectedList() {
		Map<String, String> record0 = new HashMap<String, String>();
		record0.put(VariableNames.ID_IN_SALESFORCE, "0");
		record0.put(VariableNames.ID_IN_SIEBEL, "");
		record0.put(VariableNames.NAME, "SomeName_0");
		record0.put(VariableNames.INDUSTRY_IN_SALESFORCE, "industry_0_Salesforce");
		record0.put(VariableNames.INDUSTRY_IN_SIEBEL, "");

		Map<String, String> record1 = new HashMap<String, String>();
		record1.put(VariableNames.ID_IN_SALESFORCE, "1");
		record1.put(VariableNames.ID_IN_SIEBEL, "1");
		record1.put(VariableNames.NAME, "SomeName_1");
		record1.put(VariableNames.INDUSTRY_IN_SALESFORCE, "industry_1_Salesforce");
		record1.put(VariableNames.INDUSTRY_IN_SIEBEL, "industry_1_Siebel");

		Map<String, String> record2 = new HashMap<String, String>();
		record2.put(VariableNames.ID_IN_SALESFORCE, "");
		record2.put(VariableNames.ID_IN_SIEBEL, "2");
		record2.put(VariableNames.NAME, "SomeName_2");
		record2.put(VariableNames.INDUSTRY_IN_SALESFORCE, "");
		record2.put(VariableNames.INDUSTRY_IN_SIEBEL, "industry_2_Siebel");

		List<Map<String, String>> expectedList = new ArrayList<Map<String, String>>();
		expectedList.add(record0);
		expectedList.add(record2);
		expectedList.add(record1);

		return expectedList;

	}

	private List<Map<String, String>> createOriginalList() {
		Map<String, String> record0 = new HashMap<String, String>();
		record0.put(VariableNames.ID_IN_SALESFORCE, "0");
		record0.put(VariableNames.ID_IN_SIEBEL, "");
		record0.put(VariableNames.NAME, "SomeName_0");
		record0.put(VariableNames.INDUSTRY_IN_SALESFORCE, "industry_0_Salesforce");
		record0.put(VariableNames.INDUSTRY_IN_SIEBEL, "");

		Map<String, String> record1 = new HashMap<String, String>();
		record1.put(VariableNames.ID_IN_SALESFORCE, "1");
		record1.put(VariableNames.ID_IN_SIEBEL, "1");
		record1.put(VariableNames.NAME, "SomeName_1");
		record1.put(VariableNames.INDUSTRY_IN_SALESFORCE, "industry_1_Salesforce");
		record1.put(VariableNames.INDUSTRY_IN_SIEBEL, "industry_1_Siebel");

		Map<String, String> record2 = new HashMap<String, String>();
		record2.put(VariableNames.ID_IN_SALESFORCE, "");
		record2.put(VariableNames.ID_IN_SIEBEL, "2");
		record2.put(VariableNames.NAME, "SomeName_2");
		record2.put(VariableNames.INDUSTRY_IN_SALESFORCE, "");
		record2.put(VariableNames.INDUSTRY_IN_SIEBEL, "industry_2_Siebel");

		List<Map<String, String>> originalList = new ArrayList<Map<String, String>>();
		originalList.add(record0);
		originalList.add(record1);
		originalList.add(record2);

		return originalList;

	}

}
