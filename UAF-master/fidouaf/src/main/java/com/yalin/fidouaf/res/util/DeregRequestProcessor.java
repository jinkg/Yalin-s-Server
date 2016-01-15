/*
 * Copyright 2015 eBay Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yalin.fidouaf.res.util;

import org.apache.commons.codec.binary.Base64;
import org.ebayopensource.fido.uaf.msg.DeregisterAuthenticator;
import org.ebayopensource.fido.uaf.msg.DeregistrationRequest;
import org.ebayopensource.fido.uaf.msg.Operation;
import org.ebayopensource.fido.uaf.msg.OperationHeader;
import org.ebayopensource.fido.uaf.msg.Version;
import org.ebayopensource.fido.uaf.storage.AuthenticatorRecord;

import com.google.gson.Gson;
import com.yalin.fidouaf.stats.Dash;

public class DeregRequestProcessor {
	public static final String APP_ID = "https://uaf.ebay.com/uaf/facets";
	private String appId = APP_ID;
	private Gson gson = new Gson();

	public DeregistrationRequest[] process(String payload) {
		DeregistrationRequest[] deregFromJson = null;
		try {
			deregFromJson = gson.fromJson(payload, DeregistrationRequest[].class);
			DeregistrationRequest deregRequest = deregFromJson[0];
			deregRequest.header = new OperationHeader();
			deregRequest.header.op = Operation.Dereg;
			deregRequest.header.appID = appId;
			deregRequest.header.upv = new Version(1, 0);
			Dash.getInstance().stats.put(Dash.LAST_DEREG_REQ, deregFromJson);
			AuthenticatorRecord authRecord = new AuthenticatorRecord();
			for (DeregisterAuthenticator authenticator : deregRequest.authenticators) {
				authRecord.AAID = authenticator.aaid;
				authRecord.KeyID = Base64.encodeBase64URLSafeString(authenticator.keyID.getBytes());
				try {
					String Key = authRecord.toString();
					StorageImpl.getInstance().deleteRegistrationRecord(Key);
				} catch (Exception e) {
					return null;
				}
			}
		} catch (Exception e) {
			return null;
		}
		return deregFromJson;
	}
}
