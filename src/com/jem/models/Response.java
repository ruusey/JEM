package com.jem.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_EMPTY)
public class Response {

	@JsonProperty("status-id")
	protected Integer statusId = APIStatus.SUCCESS.id();
	@JsonProperty("status")
	protected String status = APIStatus.SUCCESS.name();
	@JsonProperty("message")
	protected String message;
	@JsonProperty("debug-message")
	protected String debugMessage;

	protected Response() {}

	public Response(APIStatus status, String message, String debugMessage) {
		setStatusEnum(status);
		setMessage(message);
		setDebugMessage(debugMessage);
	}

	public String getStatus() {
		return status;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setStatusEnum(APIStatus status) {
		statusId = status.id();
		this.status = status.name();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDebugMessage() {
		return debugMessage;
	}

	public void setDebugMessage(String debugMessage) {
		this.debugMessage = debugMessage;
	}

}
