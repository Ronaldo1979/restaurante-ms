package br.com.cognitivebrasil.mail;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import br.com.cognitivebrasil.mail.dto.MailDTO;

@Service
public class MailService{

    JavaMailSender javaMailSender;

    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMail(MailDTO mailDTO){

    	try {
    		
    		 String content = mailDTO.getContent();
    	     String destination = mailDTO.getTo();

    	     SimpleMailMessage message = new SimpleMailMessage();
    	     message.setTo(destination);
    	     message.setText(content);
    	     message.setSubject(mailDTO.getSubject());
    	     message.setFrom("noreply@rndti.com");
    	     javaMailSender.send(message);
    		
    	}catch (Exception e) {
			e.printStackTrace();
		}

    }
}
