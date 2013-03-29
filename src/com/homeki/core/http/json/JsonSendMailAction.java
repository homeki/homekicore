package com.homeki.core.http.json;

import com.homeki.core.actions.SendMailAction;

public class JsonSendMailAction extends JsonAction {
	public String subject;
	public String recipients;
	public String text;
	
	public JsonSendMailAction(SendMailAction action) {
		super(action);
		this.subject = action.getSubject();
		this.recipients = action.getRecipients();
		this.text = action.getText();
	}
}
