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

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.templates.utils.VariableNames;
import org.mule.templates.utils.Utils;
import org.mule.transformer.AbstractMessageTransformer;

/**
 * This transformer will take two lists as input and create a third one that
 * will be the merge of the previous two. The identity of list's element is
 * defined by its Name.
 * 
 * @author damian.sima
 * @author martin
 */
public final class AccountMergerTransformer extends AbstractMessageTransformer {

	private static final String EMPTY = "";

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
		List<Map<String, String>> mergedAccountsList = mergeList(
				Utils.buildList(message, VariableNames.ACCOUNTS_FROM_SALESFORCE),
				Utils.buildList(message, VariableNames.ACCOUNTS_FROM_SIEBEL));

		return mergedAccountsList;
	}

	/**
	 * The method will merge the accounts from the two lists creating a new one.
	 * 
	 * @param accountsFromSalesforce
	 *            accounts from Salesforce
	 * @param accountsFromSiebel
	 *            accounts from Oracle Siebel Business Objects
	 * @return a list with the merged content of the to input lists
	 */
	public static List<Map<String, String>> mergeList(List<Map<String, String>> accountsFromSalesforce, List<Map<String, String>> accountsFromSiebel) {
		List<Map<String, String>> mergedAccountList = new ArrayList<Map<String, String>>();

		// Put all accounts from Salesforce in the merged contactList
		for (Map<String, String> accountFromSalesforce : accountsFromSalesforce) {
			Map<String, String> mergedAccount = createMergedAccount(accountFromSalesforce);
			mergedAccount.put(VariableNames.ID_IN_SALESFORCE, accountFromSalesforce.get(VariableNames.ID));
			mergedAccount.put(VariableNames.INDUSTRY_IN_SALESFORCE, accountFromSalesforce.get(VariableNames.INDUSTRY));
			mergedAccount.put(VariableNames.NUMBER_OF_EMPLOYEES_IN_SALESFORCE, accountFromSalesforce.get(VariableNames.NUMBER_OF_EMPLOYEES));
			mergedAccountList.add(mergedAccount);
		}

		// Add the new accounts from Siebel and update the exiting ones
		for (Map<String, String> accountFromSiebel : accountsFromSiebel) {
			Map<String, String> accountFromSalesforce = findAccountInList(accountFromSiebel.get(VariableNames.IDENTITY_FIELD_KEY), mergedAccountList);
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

	private static Map<String, String> createMergedAccount(Map<String, String> account) {
		Map<String, String> mergedAccount = new HashMap<String, String>();
		mergedAccount.put(VariableNames.IDENTITY_FIELD_KEY, account.get(VariableNames.IDENTITY_FIELD_KEY));
		mergedAccount.put(VariableNames.ID_IN_SALESFORCE, EMPTY);
		mergedAccount.put(VariableNames.INDUSTRY_IN_SALESFORCE, EMPTY);
		mergedAccount.put(VariableNames.NUMBER_OF_EMPLOYEES_IN_SALESFORCE, EMPTY);
		mergedAccount.put(VariableNames.ID_IN_SIEBEL, EMPTY);
		mergedAccount.put(VariableNames.INDUSTRY_IN_SIEBEL, EMPTY);
		mergedAccount.put(VariableNames.NUMBER_OF_EMPLOYEES_IN_SIEBEL, EMPTY);
		return mergedAccount;
	}

	private static Map<String, String> findAccountInList(String accountName, List<Map<String, String>> accountList) {
		for (Map<String, String> account : accountList) {
			if (account.get(VariableNames.IDENTITY_FIELD_KEY).equals(accountName)) {
				return account;
			}
		}
		return null;
	}

}
