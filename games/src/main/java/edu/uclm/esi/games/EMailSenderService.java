package edu.uclm.esi.games;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/*public class EMailSenderService {


	public EMailSenderService() {

	}

	public void enviarPorGmail(String destino, String token)  {
		 Properties props = new Properties();
         props.setProperty("mail.smtp.host", "smtp.live.com");
         props.setProperty("mail.smtp.starttls.enable", "true");
         props.setProperty("mail.smtp.port", "587");
         props.setProperty("mail.smtp.auth", "true");
         Session session = Session.getDefaultInstance(props);
         session.setDebug(false);
         BodyPart texto = new MimeBodyPart();
         try {
            	 texto.setText("Copie el siguiente código en la ventana del texto:" + token);
                 MimeMultipart multiParte = new MimeMultipart();
                 multiParte.addBodyPart(texto);
                 MimeMessage message = new MimeMessage(session);
                 message.setFrom(new InternetAddress("tysistemasweb@gmail.com"));
                 message.addRecipient(Message.RecipientType.TO, new InternetAddress(destino));
                 message.setSubject("Recuperación credenciales");
                 message.setContent(multiParte);
                 Transport t = null;
                 t = session.getTransport("smtp");
                 t.connect("systemasWeb@outlook.es", "SystemasWeb2018");
                 t.sendMessage(message, message.getAllRecipients());
                 t.close();
             
         } catch (MessagingException e) {
        	 System.out.println(e.toString());
         }
	}
}*/
public class EMailSenderService {
	private final Properties properties = new Properties();
	private String smtpHost, startTTLS, port;
	private String remitente, serverUser, userAutentication, pwd;
	
	public void enviarPorGmail(String email, String string)  {
		this.smtpHost="smtp.gmail.com";
		this.startTTLS="true";
		this.port="465";
		this.remitente="tysistemasweb@gmail.com";
		this.serverUser="tysistemasweb@gmail.com";
		this.userAutentication="true";
		this.pwd="TySistemasWeb2018";
		properties.put("mail.smtp.host", this.smtpHost);  
        properties.put("mail.smtp.starttls.enable", this.startTTLS);  
        properties.put("mail.smtp.port", this.port);  
        properties.put("mail.smtp.mail.sender", this.remitente);  
        properties.put("mail.smtp.user", this.serverUser);  
        properties.put("mail.smtp.auth", this.userAutentication);
        properties.put("mail.smtp.socketFactory.port", this.port);
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.socketFactory.fallback", "false");
        
        Authenticator auth = new autentificadorSMTP();
        Session session = Session.getInstance(properties, auth);
		try {
			MimeMessage msg = new MimeMessage(session);
		    msg.setSubject("Games - recuperación de contraseña");
		    msg.setText("Copie el siguiente código en la ventana de texto: " + string );
		    msg.setFrom(new InternetAddress(this.remitente));
		    msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
		    Transport.send(msg);
		}catch (Exception e) {
			System.out.println("Error correo: "+e.getMessage());
		}
	}
	
	private class autentificadorSMTP extends javax.mail.Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(remitente, pwd);
        }
    }
}