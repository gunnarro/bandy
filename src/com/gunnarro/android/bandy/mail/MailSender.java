package com.gunnarro.android.bandy.mail;

import java.security.Security;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.gunnarro.android.bandy.custom.CustomLog;

public class MailSender extends javax.mail.Authenticator {

	private String mailhost = "smtp.gmail.com";
	private String user;
	private String password;
	private Session session;

	static {
		Security.addProvider(new JSSEProvider());
	}

	public MailSender(String user, String password) {
		this.user = user;
		this.password = password;

		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.host", mailhost);
		props.put("mail.smtp.host", mailhost);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");
		props.put("mail.smtp.quitwait", "false");

		session = Session.getDefaultInstance(props, this);
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(user, password);
	}

	public synchronized void sendMail(String subject, String body, String sender, List<String> recipientList) throws Exception {
		try {
			MimeMessage message = new MimeMessage(session);
			DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
			message.setSender(new InternetAddress(sender));
			message.setSubject(subject);
			message.setDataHandler(handler);
			String recipients = recipientList.toString().replace("[", "").replace("]", "").trim();
			if (recipients.indexOf(',') > 0) {
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
			} else {
				message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
			}
			CustomLog.e(this.getClass(), "email to=" + recipients);
			Transport.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
