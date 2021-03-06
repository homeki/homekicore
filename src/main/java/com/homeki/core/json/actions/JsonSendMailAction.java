package com.homeki.core.json.actions;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.homeki.core.actions.SendMailAction;

@JsonTypeName("sendmail")
public class JsonSendMailAction extends JsonAction {
	public String subject;
	public String recipients;
	public String text;

	public JsonSendMailAction() {

	}
	
	public JsonSendMailAction(SendMailAction action) {
		super(action);
		this.subject = action.getSubject();
		this.recipients = action.getRecipients();
		this.text = action.getText();
	}
}
