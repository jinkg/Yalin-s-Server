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

package org.ebayopensource.fido.uaf.storage;

import com.google.gson.Gson;

public class RegistrationRecord {
	public static final String KEY_REG_INDEXID = "indexKey";
	public static final String KEY_REG_AUTHID = "authenticatorId";
	public static final String KEY_REG_PUBLIC_KEY = "publicKey";
	public static final String KEY_REG_SIGN_COUNTER = "signCounter";
	public static final String KEY_REG_AUTH_VERSION= "authenticatorVersion";
	public static final String KEY_REG_PNG= "tcDisplayPNGCharacteristics";
	public static final String KEY_REG_USERNAME= "username";
	public static final String KEY_REG_USER_ID= "userId";
	public static final String KEY_REG_DEVICE_ID ="deviceId";
	public static final String KEY_REG_TIME_STAMP= "timeStamp";
	public static final String KEY_REG_STATUS= "status";
	public static final String KEY_REG_AC= "attestCert";
	public static final String KEY_REG_ADTS= "attestDataToSign";
	public static final String KEY_REG_AS= "attestSignature";
	public static final String KEY_REG_AVS= "attestVerifiedStatus";
	
	public AuthenticatorRecord authenticator;
	public String PublicKey;
	public String SignCounter;
	public String AuthenticatorVersion;
	public String tcDisplayPNGCharacteristics;
	public String username;
	public String userId;
	public String deviceId;
	public String timeStamp;
	public String status;
	public String attestCert;
	public String attestDataToSign;
	public String attestSignature;
	public String attestVerifiedStatus;

	public String toJson() {
		return new Gson().toJson(this);
	}
}
