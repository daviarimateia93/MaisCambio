package br.com.maiscambio.model.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import br.com.maiscambio.util.Constants;

@Service
public class EmailService
{
	@Autowired
	private JavaMailSenderImpl javaMailSenderImpl;
	
	public void send(String to, String subject, String body) throws MessagingException
	{
		MimeMessage mimeMessage = javaMailSenderImpl.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, Constants.TEXT_CHARSET_UTF_8);
		mimeMessage.setContent(body, Constants.TEXT_CONTENT_TYPE_TEXT_HTML);
		mimeMessageHelper.setTo(to);
		mimeMessageHelper.setSubject(subject);
		mimeMessageHelper.setFrom(javaMailSenderImpl.getUsername());
		
		javaMailSenderImpl.send(mimeMessage);
	}
}
