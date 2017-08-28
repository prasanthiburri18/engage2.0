package com.engage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/email")
@PropertySource("classpath:mail.properties")
public class EmailRestController{
	@Value("${mail.from}")	
public String fromEmail;
	@Autowired
	private JavaMailSender javaMailSender;


	@Autowired
	private VelocityEngine velocityEngine;

	@RequestMapping("version")
	@ResponseStatus(HttpStatus.OK)
	public String version() {
		return "[OK] Welcome to withdraw Restful version 1.0";
	}

	@RequestMapping(value = "send", method = RequestMethod.POST, produces = { "application/xml", "application/json" })
	public ResponseEntity<Email> sendSimpleMail(@RequestBody Email email)throws Exception {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();

		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
		mimeMessageHelper.setFrom(fromEmail);
		mimeMessageHelper.setTo(email.getTo());
		mimeMessageHelper.setSubject(email.getSubject());
		mimeMessageHelper.setText("<html><body>" + email.getText() + "</body></html>", true);
		javaMailSender.send(mimeMessage);
		email.setStatus(true);
		return new ResponseEntity<Email>(email, HttpStatus.OK);
	}

	@RequestMapping(value = "attachments", method = RequestMethod.POST, produces = { "application/xml", "application/json" })
	public ResponseEntity<Email> attachments(@RequestBody Email email) throws Exception {

		MimeMessage mimeMessage = javaMailSender.createMimeMessage();

		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
		mimeMessageHelper.setFrom(fromEmail);
		mimeMessageHelper.setTo(email.getTo());
		mimeMessageHelper.setSubject(email.getSubject());
		mimeMessageHelper.setText("<html><body><img src=\"cid:banner\" >" + email.getText() + "</body></html>", true);

		FileSystemResource file = new FileSystemResource(new File("banner.jpg"));
		mimeMessageHelper.addInline("banner", file);

		FileSystemResource fileSystemResource = new FileSystemResource(new File("Attachment.jpg"));
		mimeMessageHelper.addAttachment("Attachment.jpg", fileSystemResource);

		javaMailSender.send(mimeMessage);
		email.setStatus(true);

		return new ResponseEntity<Email>(email, HttpStatus.OK);
	}

	@RequestMapping(value = "template", method = RequestMethod.POST, produces = { "application/xml", "application/json" })
	public ResponseEntity<Email> template(@RequestBody Email email) throws Exception {

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("title", email.getSubject());
		model.put("body", email.getText());
		String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "email.vm", "UTF-8", model);
		
		System.out.println(text);

		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
		mimeMessageHelper.setFrom(fromEmail);
		mimeMessageHelper.setTo(email.getTo());
		mimeMessageHelper.setSubject(email.getSubject());
		mimeMessageHelper.setText(text, true);

		javaMailSender.send(mimeMessage);
		
		email.setStatus(true);

		return new ResponseEntity<Email>(email, HttpStatus.OK);
	}
}
