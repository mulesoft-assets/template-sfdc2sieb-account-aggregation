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

import org.mule.templates.utils.VariableNames;

/**
 * The object of this class will take two lists as input and create a third one that
 * will be the merge of the previous two. The identity of list's element is
 * defined by its Name.
 */
public class AccountMerger {
	private static final String IDENTITY_FIELD_KEY = "Name";

	/**
	 * The method will merge the accounts from the two lists creating a new one.
	 * 
	 * @param accountsFromSFDC
	 *            accounts from Salesforce
	 * @param accountsFromSiebel
	 *            accounts from Siebel
	 * @return a list with the merged content of the to input lists
	 */
	public List<Map<String, String>> mergeList(List<Map<String, String>> accountsFromSFDC, List<Map<String, String>> accountsFromSiebel) {
		List<Map<String, String>> mergedAccountList = new ArrayList<Map<String, String>>();

		// Put all opportunities from SFDC in the merged accountList
		for (Map<String, String> accountFromSFDC : accountsFromSFDC) {
			Map<String, String> mergedAccount = createMergedAccount(accountFromSFDC);
			mergedAccount.put(VariableNames.ID_IN_SALESFORCE, accountFromSFDC.get(VariableNames.ID));
			mergedAccount.put(VariableNames.INDUSTRY_IN_SALESFORCE, accountFromSFDC.get(VariableNames.INDUSTRY));
			mergedAccount.put(VariableNames.NUMBER_OF_EMPLOYEES_IN_SALESFORCE, accountFromSFDC.get(VariableNames.NUMBER_OF_EMPLOYEES));
			mergedAccountList.add(mergedAccount);
		}

		// Add the new accounts from Siebel and update the exiting ones
		for (Map<String, String> accountFromSiebel : accountsFromSiebel) {
			Map<String, String> accountFromSalesforce = findAccountInList(accountFromSiebel, mergedAccountList);
			if (accountFromSalesforce != null) {
				accountFromSalesforce.put(VariableNames.ID_IN_SIEBEL, accountFromSiebel.get(VariableNames.ID));
				accountFromSalesforce.put(VariableNames.INDUSTRY_IN_SIEBEL, accountFromSiebel.get(VariableNames.INDUSTRY));
				accountFromSalesforce.put(VariableNames.NUMBER_OF_EMPLOYEES_IN_SIEBEL, accountFromSiebel.get(VariableNames.NUMBER_OF_EMPLOYEES));
			} else {
				Map<String, String> mergedAccount = createMergedAccount(accountFromSiebel);
				mergedAccount.put(VariableNames.ID_IN_SIEBEL, accountFromSiebel.get(VariableNames.ID));
				mergedAccount.put(VariableNames.INDUSTRY_IN_SIEBEL, accountFromSiebel.get(VariableNames.INDUSTRY));
				mergedAccount.put(VariableNames.NUMBER_OF_EMPLOYEES_IN_SIEBEL, accountFromSiebel.get(VariableNames.NUMBER_OF_EMPLOYEES));
				mergedAccountList.add(mergedAccount);
			}

		}
		return mergedAccountList;
	}

	private Map<String, String> createMergedAccount(Map<String, String> account) {
		Map<String, String> mergedAccount = new HashMap<String, String>();
		mergedAccount.put(VariableNames.IDENTITY_FIELD_KEY, account.get(VariableNames.IDENTITY_FIELD_KEY));
		mergedAccount.put(VariableNames.ID_IN_SALESFORCE, "");
		mergedAccount.put(VariableNames.INDUSTRY_IN_SALESFORCE, "");
		mergedAccount.put(VariableNames.NUMBER_OF_EMPLOYEES_IN_SALESFORCE, "");
		mergedAccount.put(VariableNames.ID_IN_SIEBEL, "");
		mergedAccount.put(VariableNames.INDUSTRY_IN_SIEBEL, "");
		mergedAccount.put(VariableNames.NUMBER_OF_EMPLOYEES_IN_SIEBEL, "");
		return mergedAccount;
	}

	private Map<String, String> findAccountInList(Map<String, String> accountToLookup, List<Map<String, String>> accountList) {
		for (Map<String, String> account : accountList) {
			if (account.get(IDENTITY_FIELD_KEY).equals(accountToLookup.get(IDENTITY_FIELD_KEY))) {
				return account;
			}
		}
		return null;
	}
}
