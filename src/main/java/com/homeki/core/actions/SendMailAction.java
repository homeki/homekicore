package com.homeki.core.actions;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.Session;

import com.homeki.core.logging.L;
import com.homeki.core.main.Setting;

@Entity
public class SendMailAction extends Action {
	@Column
	private String subject;
	
	@Column
	private String recipients;
	
	@Column
	private String text;
	
	public SendMailAction() {
		
	}
	
	public SendMailAction(String subject, String recipients, String text) {
		this.subject = subject;
		this.recipients = recipients;
		this.text = text;
	}
	
	@Override
	public void execute(Session ses) {
		boolean smtpAuth = Setting.getBoolean(ses, Setting.SMTP_AUTH);
		final String username = Setting.getString(ses, Setting.SMTP_USER);
		
		Properties props = new Properties();
		props.put("mail.smtp.auth", String.valueOf(smtpAuth));
		props.put("mail.smtp.starttls.enable", Setting.getString(ses, Setting.SMTP_TLS));
		props.put("mail.smtp.host", Setting.getString(ses, Setting.SMTP_HOST));
		props.put("mail.smtp.port", Setting.getString(ses, Setting.SMTP_PORT));
 
		javax.mail.Session session;
		if (smtpAuth) {
			final String password = Setting.getString(ses, Setting.SMTP_PASSWORD);
			session = javax.mail.Session.getInstance(props,
				  new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				  });
		}
		else {
			session = javax.mail.Session.getInstance(props);
		}
 
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
			message.setSubject(subject);
			message.setText(text);
			Transport.send(message);
 
			L.i("Mail sent successfully to " + recipients + ".");
 
		} catch (MessagingException e) {
			L.e("Failed sending mail.", e);
		}
	}

	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getRecipients() {
		return recipients;
	}
	
	public void setRecipients(String recipients) {
		this.recipients = recipients;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public String getType() {
		return "sendmail";
	}
}
