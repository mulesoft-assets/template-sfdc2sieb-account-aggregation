package org.mule.templates.integration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mule.MessageExchangePattern;
import org.mule.api.MuleEvent;
import org.mule.modules.siebel.api.model.response.CreateResult;
import org.mule.processor.chain.SubflowInterceptingChainLifecycleWrapper;
import org.mule.templates.utils.VariableNames;
import org.mule.util.UUID;

import com.sforce.soap.partner.SaveResult;

/**
 * The objective of this class is to validate the correct behavior of the flows
 * for this Mule Template that make calls to external systems.
 * 
 */
public class BusinessLogicIT extends AbstractTemplateTestCase {

	private List<Map<String, Object>> createdAccountsInSalesforce = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> createdAccountsInSiebel = new ArrayList<Map<String, Object>>();

	@BeforeClass
	public static void init() {
		System.setProperty("mail.subject", "Accounts Report");
		System.setProperty("mail.body", "Please find attached your Accounts Report");
		System.setProperty("attachment.name", "AccountsReport.csv");
	}
	
	@Before
	public void setUp() throws Exception {
		createAccounts();
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

	private void createAccounts() throws Exception {
		SubflowInterceptingChainLifecycleWrapper createAccountInSalesforceFlow = getSubFlow("createAccountsInSalesforceFlow");
		createAccountInSalesforceFlow.initialise();

		Map<String, Object> salesforceAccount = new HashMap<String, Object>();
		salesforceAccount.put("Name", "Name_Salesforce_0_" + TEMPLATE_NAME + "_" + UUID.getUUID());
		createdAccountsInSalesforce.add(salesforceAccount);

		MuleEvent event = createAccountInSalesforceFlow.process(getTestEvent(createdAccountsInSalesforce, MessageExchangePattern.REQUEST_RESPONSE));
		List<?> results = (List<?>) event.getMessage().getPayload();
		for (int i = 0; i < results.size(); i++) {
			createdAccountsInSalesforce.get(i).put(VariableNames.ID, ((SaveResult) results.get(i)).getId());
		}

		Map<String, Object> siebelAccount = new HashMap<String, Object>();
		siebelAccount.put(VariableNames.NAME, "Name_Siebel_0_" + TEMPLATE_NAME + "_" + UUID.getUUID());
//		siebelAccount.put(VariableNames.INDUSTRY, "Education");
		siebelAccount.put("Description", "Some account description");
		createdAccountsInSiebel.add(siebelAccount);

		MuleEvent event1 = runFlow("createAccountsInSiebelFlow", createdAccountsInSiebel);
//		SubflowInterceptingChainLifecycleWrapper createAccountInSiebelFlow = getSubFlow("createAccountsInSiebelFlow");
//		createAccountInSiebelFlow.setFlowConstruct(getTestService());
//		createAccountInSiebelFlow.initialise();
//
//		MuleEvent event = createAccountInSiebelFlow.process(getTestEvent(createdAccountsInSiebel, MessageExchangePattern.REQUEST_RESPONSE));
		
		List<?> results1 = (List<?>) event1.getMessage().getPayload();
		
		// assign Siebel-generated IDs
		for (int i = 0; i < createdAccountsInSiebel.size(); i++) {
			createdAccountsInSiebel.get(i).put(VariableNames.ID, ((CreateResult) results1.get(i)).getCreatedObjects().get(0));
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
		flow.initialise();

		MuleEvent event = flow.process(getTestEvent("", MessageExchangePattern.REQUEST_RESPONSE));
		Set<String> flowVariables = event.getFlowVariableNames();

		Assert.assertTrue("The variable " + VariableNames.ACCOUNTS_FROM_SALESFORCE + " is missing.", flowVariables.contains(VariableNames.ACCOUNTS_FROM_SALESFORCE));
		Assert.assertTrue("The variable " + VariableNames.ACCOUNTS_FROM_SIEBEL + " is missing.", flowVariables.contains(VariableNames.ACCOUNTS_FROM_SIEBEL));

		Iterator<?> accountsFromSalesforce = event.getFlowVariable(VariableNames.ACCOUNTS_FROM_SALESFORCE);
		Collection<?> accountsFromSiebel = event.getFlowVariable(VariableNames.ACCOUNTS_FROM_SIEBEL);

		Assert.assertTrue("There should be accounts in the variable " + VariableNames.ACCOUNTS_FROM_SALESFORCE + ".", accountsFromSalesforce.hasNext());
		Assert.assertTrue("There should be accounts in the variable " + VariableNames.ACCOUNTS_FROM_SIEBEL + ".", !accountsFromSiebel.isEmpty());
	}

}
