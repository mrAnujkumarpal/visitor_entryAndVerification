package vms.vevs.common.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import vms.vevs.common.util.VmsConstants;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.javaMailSender = mailSender;
    }

    @Async
    public void sendEmailOTP(String to,String otp) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setFrom(VmsConstants.MAIL_SENDER);
        msg.setSubject(VmsConstants.OTP_SUBJECT);
        msg.setText("Please enter otp in app to complete your entry and verification" +
                " in our organization : " + otp +" \n \n \n With Warm Regards " +
                " \n Anuj Kumar Pal \n +91 8095446907 \n \n");
        javaMailSender.send(msg);
    }

    /*
    public void sendEmailWithAttachment(EMail mail) throws MessagingException, IOException {
        MimeMessage msg = javaMailSender.createMimeMessage();
        // true = multipart message
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);

        helper.setTo(mail.getTo());
        helper.setFrom(mail.getFrom());
        helper.setSubject(mail.getSubject());
        helper.setText(mail.getContent());

        // hard coded a file path
        // FileSystemResource file = new FileSystemResource(new    File("path/img.png"));
        // helper.addAttachment("Google Photo",file);
        helper.addAttachment("Google Photo", new ClassPathResource("img.png"));
        javaMailSender.send(msg);
    }
 */
}
