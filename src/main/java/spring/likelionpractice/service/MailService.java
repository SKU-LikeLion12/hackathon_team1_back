package spring.likelionpractice.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private static final String senderEmail = "sung25480@gmail.com";
    private static int number;

    // 난수 생성
    public static void createNumber() {
        number = (int)(Math.random() * (999999)) + 9000000;
    }

    @Transactional
    public MimeMessage createMail(String mail) throws MessagingException, UnsupportedEncodingException {
        createNumber();
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            mimeMessage.setFrom(senderEmail);       // 보내는 사람
            mimeMessage.setRecipients(MimeMessage.RecipientType.TO, mail);      // 받는 사람 설정
            mimeMessage.setSubject("이메일 인증");
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "<h3>";
            body += "<h1>" + number + "</h1>";

            mimeMessage.setText(body, "utf-8", "html");      // 위 html 코드를 이메일로 보냄
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return mimeMessage;
    }

    // 실제 메일 전송 코드
    @Transactional
    public int sendMail(String mail) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = createMail(mail);
        mailSender.send(mimeMessage);       // 메일 전송

        return number;
    }
}
