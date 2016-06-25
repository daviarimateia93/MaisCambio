package br.com.maiscambio.model.service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import br.com.maiscambio.util.Constants;

@Service
public class EmailService implements BaseService
{
	private static class Email
	{
		String to, subject, body;
		
		Email(String to, String subject, String body)
		{
			this.to = to;
			this.subject = subject;
			this.body = body;
		}
	}
	
	@Autowired
	private JavaMailSenderImpl javaMailSenderImpl;
	
	private BlockingQueue<Email> emailQueue = new LinkedBlockingQueue<>();
	
	private boolean sendThreadStarted = false;
	
	private Thread sendThread = new Thread()
	{
		@Override
		public void run()
		{
			Email email = null;
			
			try
			{
				while((email = emailQueue.take()) != null)
				{
					try
					{
						javaMailSenderImpl.send(setupMimeMessage(email.to, email.subject, email.body));
					}
					catch(MailException | MessagingException exception)
					{
						
					}
				}
			}
			catch(InterruptedException interruptedException)
			{
				
			}
		}
	};
	
	public void send(String to, String subject, String body) throws MessagingException
	{
		javaMailSenderImpl.send(setupMimeMessage(to, subject, body));
	}
	
	public void sendAsynchronously(String to, String subject, String body) throws InterruptedException
	{
		if(!sendThreadStarted)
		{
			sendThread.start();
			
			sendThreadStarted = true;
		}
		
		emailQueue.put(new Email(to, subject, body));
	}
	
	private MimeMessage setupMimeMessage(String to, String subject, String body) throws MessagingException
	{
		MimeMessage mimeMessage = javaMailSenderImpl.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, Constants.TEXT_CHARSET_UTF_8);
		mimeMessage.setContent(body, Constants.TEXT_CONTENT_TYPE_TEXT_HTML);
		mimeMessageHelper.setTo(to);
		mimeMessageHelper.setSubject(subject);
		mimeMessageHelper.setFrom(javaMailSenderImpl.getUsername());
		
		return mimeMessage;
	}
}
