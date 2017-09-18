package com.engage.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.engage.Email;
import com.engage.EmailRestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailControllerTest {
	
	/*@Autowired
	private MockMvc mockClient;
	*/
	@Autowired
	private EmailRestController emailRestController;
	/*@Test
	public void emailTest() throws JsonProcessingException, Exception{
		ObjectMapper objectMapper = new ObjectMapper();
		Email email = new Email();
		email.from="svprudhvi13@gmail.com";
		email.to="s.krishna@mindtechlabs.com";
		email.text="Hello";
		email.subject="test";
		MvcResult result =mockClient.perform(
				MockMvcRequestBuilders.post("/email/send")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(email)))
				.andReturn();
	}
	
	*/
	@Test
	public void sendEmailTest() throws JsonProcessingException, Exception{
		//EmailRestController erc = new EmailRestController();
		Email email = new Email();
		email.from="svprudhvi13@gmail.com";
		email.to="s.krishna@mindtechlabs.com";
		email.text="Hello";
		email.subject="test";
		emailRestController.sendSimpleMail(email);
		/*
		MvcResult result =mockClient.perform(
				MockMvcRequestBuilders.post("/email/send")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(email)))
				.andReturn();*/
	}
	

}
