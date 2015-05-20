/**
 * Mule Anypoint Template
 * Copyright (c) MuleSoft, Inc.
 * All rights reserved.  http://www.mulesoft.com
 */

package org.mule.templates.integration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mule.MessageExchangePattern;
import org.mule.api.MuleEvent;
import org.mule.modules.siebel.api.model.response.CreateResult;
import org.mule.processor.chain.SubflowInterceptingChainLifecycleWrapper;
import org.mule.tck.junit4.rule.DynamicPort;
import org.mule.templates.utils.VariableNames;
import org.mule.util.UUID;

import com.google.common.collect.Lists;
import com.sforce.soap.partner.SaveResult;

/**
 * The objective of this class is to validate the correct behavior of the flows
 * for this Mule Template that make calls to external systems.
 * 
 */
public class BusinessLogicIT extends AbstractTemplateTestCase {

	private List<Map<String, Object>> createdAccountsInSalesforce = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> createdAccountsInSiebel = new ArrayList<Map<String, Object>>();

	@Rule
	public DynamicPort port = new DynamicPort("http.port");

	
	@BeforeClass
	public static void init() {
		System.setProperty("mail.subject", "Accounts Report");
		System.setProperty("mail.body", "Please find attached your Accounts Report");
		System.setProperty("attachment.name", "AccountsReport.csv");
	}
	
	@Before
	public void setUp() throws Exception {
		createTestAccounts();
	}

	@After
	public void tearDown() throws Exception {
		deleteTestAccountFromSandBox(createdAccountsInSalesforce, "deleteAccountsFromSalesforceFlow");
		deleteTestAccountFromSandBox(createdAccountsInSiebel, "deleteAccountsFromSiebelFlow");
	}

	@Override
	protected String getConfigResources() {
		return super.getConfigResources() + getTestFlows();
	}

	private void createTestAccounts() throws Exception {
		/* SFDC account */

		// create test account object
		Map<String, Object> salesforceAccount = new HashMap<String, Object>();
		salesforceAccount.put("Name", "Name_Salesforce_0_" + TEMPLATE_NAME + "_" + UUID.getUUID());
		createdAccountsInSalesforce.add(salesforceAccount);

		// run flow and save the account ID
		SubflowInterceptingChainLifecycleWrapper createAccountInSalesforceFlow = getSubFlow("createAccountsInSalesforceFlow");
		createAccountInSalesforceFlow.initialise();
		MuleEvent eventSFDC = createAccountInSalesforceFlow.process(getTestEvent(createdAccountsInSalesforce, MessageExchangePattern.REQUEST_RESPONSE));
		List<?> results = (List<?>) eventSFDC.getMessage().getPayload();
		for (int i = 0; i < results.size(); i++) {
			createdAccountsInSalesforce.get(i).put(VariableNames.ID, ((SaveResult) results.get(i)).getId());
		}

		/* Siebel account */
		// create test account object
		Map<String, Object> siebelAccount = new HashMap<String, Object>();
		siebelAccount.put(VariableNames.NAME, "Name_Siebel_0_" + TEMPLATE_NAME + "_" + UUID.getUUID());
		createdAccountsInSiebel.add(siebelAccount);

		// run flow and save the account ID
		MuleEvent eventSiebel = runFlow("createAccountsInSiebelFlow", createdAccountsInSiebel);
		List<?> resultsSiebel = (List<?>) eventSiebel.getMessage().getPayload();
		for (int i = 0; i < createdAccountsInSiebel.size(); i++) {
			createdAccountsInSiebel.get(i).put(VariableNames.ID, ((CreateResult) resultsSiebel.get(i)).getCreatedObjects().get(0));
		}
	}

	protected void deleteTestAccountFromSandBox(List<Map<String, Object>> createdAccounts, String deleteFlow) throws Exception {
		List<String> idList = new ArrayList<String>();

		SubflowInterceptingChainLifecycleWrapper flow = getSubFlow(deleteFlow);
		flow.initialise();
		for (Map<String, Object> c : createdAccounts) {
			idList.add((String) c.get(VariableNames.ID));
		}
		flow.process(getTestEvent(idList, MessageExchangePattern.REQUEST_RESPONSE));
	}
	
	@Test
	public void testGatherDataFlow() throws Exception {
		SubflowInterceptingChainLifecycleWrapper flow = getSubFlow("gatherDataFlow");
		flow.setMuleContext(muleContext);
		flow.initialise();
		flow.start();
		MuleEvent event = flow.process(getTestEvent("", MessageExchangePattern.REQUEST_RESPONSE));
		
		List<Map<String, String>> mergedAccountList = Lists.newArrayList((Iterator<Map<String, String>>)event.getMessage().getPayload());
		Assert.assertTrue("There should be Accounts from SFDC or Siebel.", mergedAccountList.size() > 0);
		
		// find the test accounts in the merged list
		boolean sfdcAccountFound = false;
		boolean siebelAccountFound = false;
		for (Map<String, String> account : mergedAccountList) {
			if (account.get("Name").equals(createdAccountsInSalesforce.get(0).get("Name"))) {
				sfdcAccountFound = true;
			}
			if (account.get("Name").equals(createdAccountsInSiebel.get(0).get("Name"))) {
				siebelAccountFound = true;
			}
		}
		Assert.assertTrue("Salesforce Account should be in the results.", sfdcAccountFound);
		Assert.assertTrue("Siebel Account should be in the results.", siebelAccountFound);
		
	}
}
