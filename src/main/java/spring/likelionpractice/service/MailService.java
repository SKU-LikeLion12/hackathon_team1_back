package spring.likelionpractice.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.likelionpractice.Exception.MailSendException;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private static final String senderEmail = "sung25480@gmail.com";
    private Map<String, String> codes = new ConcurrentHashMap<>();


    // 임시 비밀번호 난수 생성
    public String tmpPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        SecureRandom random = new SecureRandom();
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    // 임시 비밀번호 메일 생성
    public MimeMessage createTmpMail(String mail, String tmpPassword) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            mimeMessage.setFrom(senderEmail);
            mimeMessage.setRecipients(MimeMessage.RecipientType.TO, mail);
            mimeMessage.setSubject("임시 비밀번호 발급");
            String body = "";
            body += "<h3>" + "요청하신 임시 비밀번호입니다." + "<h3>";
            body += "<h1>" + tmpPassword + "</h1>";
            mimeMessage.setText(body, "utf-8", "html");
        } catch (MessagingException e) {
            throw new MailSendException();
        }
        return mimeMessage;
    }

    // 난수 생성
    public String createNumber(String email) {
        String number = String.format("%06d", (int) (Math.random() * 1000000));
        codes.put(email, number);
        return number;
    }

    // 인증번호 메일 생성
    @Transactional
    public MimeMessage createMail(String mail) throws MessagingException, UnsupportedEncodingException {
        String verificationCode = createNumber(mail);
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            mimeMessage.setFrom(senderEmail);       // 보내는 사람
            mimeMessage.setRecipients(MimeMessage.RecipientType.TO, mail);      // 받는 사람 설정
            mimeMessage.setSubject("이메일 인증");
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "<h3>";
            body += "<h1>" + verificationCode + "</h1>";

            mimeMessage.setText(body, "utf-8", "html");      // 위 html 코드를 이메일로 보냄
        } catch (MessagingException e) {
            throw new MailSendException();
        }

        return mimeMessage;
    }

    // 인증번호 메일 전송
    @Transactional
    public void sendMail(String mail) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = createMail(mail);
        mailSender.send(mimeMessage);       // 메일 전송
    }

    // 임시비밀번호 메일 전송
    @Transactional
    public void sendTmpMail(String mail, String tmpPassword) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = createTmpMail(mail, tmpPassword);
        mailSender.send(mimeMessage);
    }

    public boolean verifyCode(String email, String userNumber) {
        String storedNumber = codes.get(email);
        return storedNumber != null && storedNumber.equals(userNumber);
    }
}
