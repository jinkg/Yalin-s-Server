package com.yalin.fidouaf.response;

import org.ebayopensource.fido.uaf.msg.AuthenticationRequest;
import org.ebayopensource.fido.uaf.msg.AuthenticationResponse;
import org.ebayopensource.fido.uaf.msg.RegistrationRequest;
import org.ebayopensource.fido.uaf.storage.AuthenticatorRecord;
import org.ebayopensource.fido.uaf.storage.RegistrationRecord;

public class ResponseBuilder {
	public static String buildStartRegistrationResponse(RegistrationRequest[] requests) {
		String s = new JsonHttpResponse(ErrorCode.SUCCESS, requests).toJson();
		return s;
	}

	public static String buildFinishRegistrationResultData(RegistrationRecord[] records) {
		String s = new JsonHttpResponse(ErrorCode.SUCCESS, records).toJson();
		return s;
	}

	public static String buildStartAuthenticateData(AuthenticationRequest[] requests) {
		return new JsonHttpResponse(ErrorCode.SUCCESS, requests).toJson();
	}
	
	public static String buildFinishAuthenticateResultData(AuthenticatorRecord[] records) {
		String s = new JsonHttpResponse(ErrorCode.SUCCESS, records).toJson();
		return s;
	}

	public static String buildFinishAuthenticateResultData() {
		return null;
	}

	public static String buildNoDeviceError() {
		return new JsonHttpResponse(ErrorCode.ERROR_NO_DEVICE_ERROR).toJson();
	}
}
