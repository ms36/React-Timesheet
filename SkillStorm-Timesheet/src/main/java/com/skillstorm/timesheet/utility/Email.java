package com.skillstorm.timesheet.utility;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.jboss.logging.Logger;

public class Email
{
	private Session mailSession;
	private String emailTo;
	private String emailSenderName;
	private String emailSubject;
	private String emailBody;
    // Sender's credentials
	private String fromUserEmailAccountName;
	private String fromUserEmailPassword;
	private String emailHost;
	
	String fileName;
	
	private static final Logger log = Logger.getLogger(Email.class);
 
    public Email(String weekEndingDate)
    {// Smtp.live.com // Outlook/Hotmail
    	// smtp-mail.outlook.com
        Properties emailProperties = System.getProperties();
        emailProperties.put("mail.smtp.port", "587");
        emailProperties.put("mail.smtp.auth", "true");
        emailProperties.put("mail.smtp.starttls.enable", "true");
        mailSession = Session.getDefaultInstance(emailProperties, null);
        
        emailTo = "email@example.com";
        emailSenderName = "John Smith";
        emailSubject = "Time Sheet week ending " + weekEndingDate;
        emailBody = "";
        fromUserEmailAccountName = "email@example.com";
        fromUserEmailPassword = "password";
        emailHost = "smtp.gmail.com";
        
        fileName = "Timesheet.xlsx";
    }
 
    // Draft an email with just text
    private MimeMessage draftEmailMessage()
    {
        MimeMessage emailMessage = new MimeMessage(mailSession);

        try
        {
        /**
         * Since this is using gmail smtp, the email address 
         * will always be the user's gmail account and not the 
         * email address entered here. The sender's name will 
         * be correct, though.
         */
        // Set the email with the sender's address
        emailMessage.setFrom(new InternetAddress(fromUserEmailAccountName, emailSenderName));
        
        // Set the mail recipients
        emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
        
        emailMessage.setSubject(emailSubject);
        
        // If sending HTML mail
        // emailMessage.setContent(emailBody, "text/html");
        
        // If sending only text mail
        emailMessage.setText(emailBody);
        }
        catch (UnsupportedEncodingException | MessagingException e) 
    	{			
			e.printStackTrace();
		} 
        return emailMessage;
    }
    
    // Draft an email with text and an attachment
    private MimeMessage draftEmailMessage(String fileName)
    {
        MimeMessage emailMessage = new MimeMessage(mailSession);

        // Create the message part 
        BodyPart messageBodyPart = new MimeBodyPart();
        
        // Create a multipart message
        Multipart multipart = new MimeMultipart();
        
        DataSource source = new FileDataSource(fileName);
        
        try 
        {
        	/**
             * Since this is using gmail smtp, the email address 
             * will always be the user's gmail account and not the 
             * email address entered here. The sender's name will 
             * be correct, though.
             */
            // Set the email with the sender's address
            emailMessage.setFrom(new InternetAddress(fromUserEmailAccountName, emailSenderName));
            
	        // Set the mail recipients
	        emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));	       
	        
	        emailMessage.setSubject(emailSubject);
	                        
	        // Fill the message
	        messageBodyPart.setText(emailBody + "\r" + emailSenderName);                
	
	        // Set text message part
	        multipart.addBodyPart(messageBodyPart);
	
	        // Part two is attachment
	        messageBodyPart = new MimeBodyPart();                
	        messageBodyPart.setDataHandler(new DataHandler(source));
	        messageBodyPart.setFileName("Timesheet for Michael Stevenson.xlsx");
	        multipart.addBodyPart(messageBodyPart);
	        
	        // Send the complete message parts
	        emailMessage.setContent(multipart);
        }
        catch (UnsupportedEncodingException | MessagingException e) 
    	{			
			e.printStackTrace();
		} 
        return emailMessage;
    }
 
    // Send an email with just text
    public void sendEmail()
    { 
    	try 
    	{
    	// Draft the message
        MimeMessage emailMessage = draftEmailMessage(fileName); // Take out the file name to send without a file
               	       
        Transport transport = mailSession.getTransport("smtp");
        transport.connect(emailHost, fromUserEmailAccountName, fromUserEmailPassword);       
        
        // Send the mail
        transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
        transport.close();
        
        log.info("Email sent successfully.");
        } 
    	catch (MessagingException e) 
    	{			
			e.printStackTrace();
		} 
    }
    
    // Send email with a file
    public void sendEmail(String fileName)
    { 
    	try 
    	{
    	// Draft the message
        MimeMessage emailMessage = draftEmailMessage(fileName);
               	       
        Transport transport = mailSession.getTransport("smtp");
        transport.connect(emailHost, fromUserEmailAccountName, fromUserEmailPassword);       
        
        // Send the mail
        transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
        transport.close();
        
        log.info("Email sent successfully.");
        } 
    	catch (MessagingException e) 
    	{			
			e.printStackTrace();
		} 
    }
}
