package org.mule.templates.transformers;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.templates.utils.VariableNames;
import org.mule.templates.utils.Utils;
import org.mule.transformer.AbstractMessageTransformer;

/**
 * This transformer will sort a list of map defining a weight for each map base
 * on the value of its keys.
 * 
 * @author damian.sima
 * @author martin
 */
public final class SortAccountListTransformer extends AbstractMessageTransformer {
	
	private static final Comparator<Map<String, String>> ACCOUNT_MAP_COMPARATOR = new Comparator<Map<String, String>>() {

		@Override
		public int compare(Map<String, String> account1, Map<String, String> account2) {
			String key1 = buildKey(account1);
			String key2 = buildKey(account2);

			return key1.compareTo(key2);
		}

		private String buildKey(Map<String, String> account) {
			StringBuilder key = new StringBuilder();

			if (StringUtils.isNotBlank(account.get(VariableNames.ID_IN_SALESFORCE)) && StringUtils.isNotBlank(account.get(VariableNames.ID_IN_SIEBEL))) {
				key.append("~~~").append(account.get(VariableNames.IDENTITY_FIELD_KEY));
			}

			if (StringUtils.isNotBlank(account.get(VariableNames.ID_IN_SALESFORCE)) && StringUtils.isBlank(account.get(VariableNames.ID_IN_SIEBEL))) {
				key.append("~").append(account.get(VariableNames.IDENTITY_FIELD_KEY));
			}

			if (StringUtils.isBlank(account.get(VariableNames.ID_IN_SALESFORCE)) && StringUtils.isNotBlank(account.get(VariableNames.ID_IN_SIEBEL))) {
				key.append("~~").append(account.get(VariableNames.IDENTITY_FIELD_KEY));
			}

			return key.toString();
		}

	};

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
		List<Map<String, String>> sortedAccountsList = Utils.buildList(message.getPayload());
		Collections.sort(sortedAccountsList, ACCOUNT_MAP_COMPARATOR);
		return sortedAccountsList;
	}
	
}
