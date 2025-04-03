package org.apache.commons.mail;

import static org.junit.Assert.*;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.Session;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.apache.commons.mail.EmailException;



public class EmailTestCases{
	protected SimpleEmail emailTest;
	@Before
	public void setUp() 
	{
		try {
			emailTest = new SimpleEmail();
	        emailTest.setHostName("smtp.gmail.com");
	        emailTest.setFrom("zanecc@umich.edu");
	        emailTest.addTo("zanecarlson03@gmail.com");
	        emailTest.setSubject("Test Email");
	        emailTest.setMsg("This is a test email.");
		}catch (EmailException e) {
			System.out.print(e.toString());
		}
	}
	
	@After
	public void tearDown() {
		emailTest = null;
	}
	
	@Test
	public void shouldThrowExceptionWhenNoEmailsAreProvidedInBcc() {
		try {
			emailTest.addBcc();
			fail("addBcc did not see that the email was empty");
		}catch(EmailException e) {
			assertEquals("Address List provided was invalid", e.getMessage());
		}		
	}
	
	@Test
	public void shouldReturnEmailWithValidEmailsAreProvidedInBcc() {
		try {
			String email1 = "zanecc@umich.edu.com";
			String email2 = "jwens@umich.edu.com";
			String email3 = "otherFriend@gmail.com";
			String email4 = "bonusFriend@gmail.com";
			assertEquals(emailTest, emailTest.addBcc(email1, email2, email3, email4));
		}catch(EmailException e) {
			fail(e.getMessage());
		}		
	}
	
	@Test
	public void addCcIsWorking() {
		try {
			String email1 = "zanecc@umich.edu";
			assertEquals(emailTest, emailTest.addCc(email1));
		} catch(EmailException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void shouldThrowExceptionIfAddingEmptyHeaderName() {
		try {
			String name = ""; //empty name should cause a problem
			String value = "TestValue";
			emailTest.addHeader(name, value);
			fail("Header name was not found to be empty");
		}catch(IllegalArgumentException e){
			assertEquals("name can not be null or empty", e.getMessage());
		}
	}
	
	@Test
	public void shouldThrowExceptionIfAddingEmptyHeaderValue() {
		try {
			String name = "TestName"; 
			String value = ""; //empty value should cause a problem
			emailTest.addHeader(name, value);
			fail("Header value was not found to be empty");
		}catch(IllegalArgumentException e){
			assertEquals("value can not be null or empty", e.getMessage());
		}
	}
	
	@Test
	public void addHeaderValidInput() {
		try {
			String name = "TestName"; 
			String value = "TestValue"; //empty value should cause a problem
			emailTest.addHeader(name, value);
			assertEquals("TestValue", emailTest.headers.get(name));
		}catch(IllegalArgumentException e){
			fail("Valid input flagged as wrong");
		}
	}
	
	@Test
	public void addReplyToWorksCorrectly() {
		try {
			String testAddress = "zanecc@umich.edu";
			assertEquals(emailTest, emailTest.addReplyTo(testAddress));
		}catch(EmailException e){
			fail(e.getMessage());
		}
	}
	
	@Test
	public void setFromWorks() {
		try {
			String testAddress = "zanecc@umich.edu";
			assertEquals(emailTest, emailTest.setFrom(testAddress));
		}catch(EmailException e){
			fail(e.getMessage());
		}
	}
	
	@Test
	public void getSocketConnectionTimeoutWorks() {
			assertEquals(emailTest.socketConnectionTimeout, emailTest.getSocketConnectionTimeout());
	}
	
	@Test
	public void getSentDateWorksWhenSentDateIsNull() {
		emailTest.sentDate = null;
		assertNotNull(emailTest.getSentDate());
	}
	
	@Test
	public void getSentDateReturnsCorrectSavedDate() {
		emailTest.sentDate = new Date();
		assertEquals(emailTest.sentDate, emailTest.getSentDate());
	}
	
	@Test
	public void getHostNameWorksWhenSessionIsNull() {
		assertEquals(emailTest.hostName, emailTest.getHostName() );
	}
	
	@Test
	public void getHostNameWorksWhenHostNameIsEmpty() {
		emailTest.hostName = null;
		assertEquals(null, emailTest.getHostName());
	}
	
	@Test
	public void getMailSessionReturnsNonNull() {
		try {
			assertNotNull(emailTest.getMailSession());
		}catch(EmailException e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void getMailSessionCalledWhenHostNameIsNull() {
		try {
			emailTest.hostName = "";
			emailTest.getMailSession();
			fail("Exception should have been thrown, but was not");
		}catch(EmailException e) {
			assertEquals("Cannot find valid hostname for mail session", e.getMessage());
		}
	}
	
	@Test
	public void buildMimeMessageNullBeforeNotNullAfter() {
		try {
			assertEquals(null, emailTest.message); //if message is null, then the sentDate will also be null.
			emailTest.buildMimeMessage();
			assertNotNull(emailTest.message);
			
		}catch(EmailException e){
			fail(e.toString());
		}
	}
	
	@Test
	public void buildMimeMessageCcListIsPopulated(){
		try {
			emailTest.addCc("zanecarlson03@gmail.com");
			assertEquals(1, emailTest.ccList.size());
			emailTest.buildMimeMessage();
			assertNotNull(emailTest.message);
		}catch(Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void buildMimeMessageBccListIsPopulated(){
		try {
			emailTest.addBcc("zanecarlson03@gmail.com");
			assertEquals(1, emailTest.bccList.size());
			emailTest.buildMimeMessage();
			assertNotNull(emailTest.message);
		}catch(Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void buildMimeMessageHeaderIsPopulated(){
		try {
			emailTest.addHeader("Testname", "TestValue");
			assertEquals(1, emailTest.headers.size());
			emailTest.buildMimeMessage();
			assertNotNull(emailTest.message);
		}catch(Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void buildMimeMessageDuplicateIsBlocked() {
		try {
			emailTest.buildMimeMessage();
			emailTest.buildMimeMessage();
			fail("Exceptio: 'MimeMessage is already built' was not thrown");
		}catch(EmailException | IllegalStateException e){
			assertEquals("The MimeMessage is already built.", e.getMessage());
		}
	}
	
	@Test
	public void buildMimeMessageSubjectAndCharsetNotEmpty() {
		try {
			emailTest.setCharset("UTF-8");
			emailTest.setSubject("TestSubject");
			emailTest.buildMimeMessage();
			assertNotNull(emailTest.message);
		}catch(EmailException e) {
			fail(e.getMessage());
		}
	}
}
