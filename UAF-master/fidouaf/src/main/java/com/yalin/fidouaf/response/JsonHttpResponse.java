package com.yalin.fidouaf.response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonHttpResponse {
	transient Gson gson = new GsonBuilder().disableHtmlEscaping().create();
	
	private int errorCode;
	private Object data;

	public JsonHttpResponse(int errorCode) {
		this.errorCode = errorCode;
		this.data = null;
	}

	public JsonHttpResponse(int errorCode, Object data) {
		this.errorCode = errorCode;
		this.data = data;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public Object getData() {
		return data;
	}

	public String toJson() {
		return gson.toJson(this);
	}

	@Override
	public String toString() {
		return toJson();
	}
}
