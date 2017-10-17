package com.engage;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

@Profile(value={"staging", "prod"})
@RestController
@RequestMapping("/email")
public class EmailRestController {

	private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(EmailRestController.class);
    @Value("${mail.from}")
    public String from;

    public String username=System.getProperty("awsSesUsername");

    public String password=System.getProperty("awsSesPassword");;

    @Autowired
    private VelocityEngine velocityEngine;

    private AmazonSimpleEmailService client;

    @PostConstruct
    public void init() {
        this.client = AmazonSimpleEmailServiceClient.builder()
                .withRegion(Regions.US_EAST_1)
                .withCredentials(new AWSStaticCredentialsProvider(new AWSCredentials() {
                    @Override
                    public String getAWSAccessKeyId() {
                        return username;
                    }

                    @Override
                    public String getAWSSecretKey() {
                        return password;
                    }
                }))
                .build();
    }

    @RequestMapping("version")
    @ResponseStatus(HttpStatus.OK)
    public String version() {
    	LOGGER.error("mail test" );
    	return "[OK] Welcome to withdraw Restful version 1.0";
    }

    @RequestMapping(value = "send", method = RequestMethod.POST, produces = {"application/xml", "application/json"})
    public ResponseEntity<Email> sendSimpleMail(@RequestBody Email email) throws Exception {
        Message message = new Message()
                .withSubject(new Content(email.getSubject()))
                .withBody(new Body().withHtml(new Content().withData("<html><body>" + email.getText() + "</body></html>")));

        SendEmailRequest request = new SendEmailRequest()
                .withSource(from)
                .withDestination(new Destination().withToAddresses(email.getTo()))
                .withMessage(message);

        client.sendEmail(request);

        email.setStatus(true);

        return new ResponseEntity<>(email, HttpStatus.OK);
    }

    @RequestMapping(value = "attachments", method = RequestMethod.POST, produces = {"application/xml", "application/json"})
    public ResponseEntity<Email> attachments(@RequestBody Email email) throws Exception {
        Message message = new Message()
                .withSubject(new Content(email.getSubject()))
                .withBody(new Body().withHtml(new Content().withData("<html><body>" + email.getText() + "</body></html>")));

        SendEmailRequest request = new SendEmailRequest()
                .withSource(from)
                .withDestination(new Destination().withToAddresses(email.getTo()))
                .withMessage(message);

        client.sendEmail(request);

        email.setStatus(true);

        return new ResponseEntity<>(email, HttpStatus.OK);
    }

    @RequestMapping(value = "template", method = RequestMethod.POST, produces = {"application/xml", "application/json"})
    public ResponseEntity<Email> template(@RequestBody Email email) throws Exception {
        Map<String, Object> model = new HashMap<>();
        model.put("title", email.getSubject());
        model.put("body", email.getText());

        Message message = new Message()
                .withSubject(new Content(email.getSubject()))
                .withBody(new Body().withHtml(new Content().withData(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "email.vm", "UTF-8", model))));

        SendEmailRequest request = new SendEmailRequest()
                .withSource(from)
                .withDestination(new Destination().withToAddresses(email.getTo()))
                .withMessage(message);

        client.sendEmail(request);

        email.setStatus(true);

        return new ResponseEntity<>(email, HttpStatus.OK);
    }
}
