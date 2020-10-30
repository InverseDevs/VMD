package Application.Email;

import Application.Entities.User;
import com.sun.mail.smtp.SMTPTransport;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailSender {
    //private static final String URL = "https://inversedevs.herokuapp.com/verification/";
    private static final String URL = "http://localhost:8080/verification/";

    private static final String SMTP_SERVER = "smtp.yandex.ru";
    private static final String SMTP_PORT = "465";
    private static final String USERNAME = "kaa5843771@yandex.ru";
    private static final String PASSWORD = "starwars2001";
    private static final String EMAIL_FROM = "kaa5843771@yandex.ru";
    private static final String EMAIL_SUBJECT = "VMD registration";

    private Properties properties;
    private Session session;
    private Message msg;

    public MailSender() {
        properties = System.getProperties();
        properties.put("mail.smtp.host", SMTP_SERVER);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", SMTP_PORT);
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        session = Session.getInstance(properties, null);
        msg = new MimeMessage(session);
    }

    public void sendVerification(User user) {
        String name = user.getUsername();
        String mail = user.getEmail();
        String token = user.getToken();

        String emailText = "Здравствуйте, " + name + "!<br>" +
                "Благодарим за проявленный к нашему сервису интерес <br>" +
                "Для завершения регистрации, пожалуйста, перейдите по ссылке ниже<br>" +
                "<a href=\"" + URL + token + "\">" + URL + token + "</a>";

        send(emailText, mail);
    }

    public void sendPassword(User user) {
        String name = user.getUsername();
        String mail = user.getEmail();
        String login = user.getUsername();
        String password = user.getPassword();

        String emailText = "Здравствуйте, " + name + "!<br>" +
                "Ваш логин: " + login + "<br>" +
                "Ваш пароль: " + password;

        send(emailText, mail);
    }

    private void send(String emailText, String mail) {
        try {
            msg.setFrom(new InternetAddress(EMAIL_FROM));

            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(mail, false));

            msg.setSubject(EMAIL_SUBJECT);

            msg.setContent(emailText, "text/html; charset=utf-8");

            SMTPTransport t = (SMTPTransport) session.getTransport("smtp");

            t.connect(SMTP_SERVER, USERNAME, PASSWORD);
            t.sendMessage(msg, msg.getAllRecipients());

            t.close();

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}