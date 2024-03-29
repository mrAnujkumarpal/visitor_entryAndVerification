package com.vevs.common.notification;

import com.vevs.entity.employee.Users;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.vevs.common.util.VmsConstants;
import com.vevs.entity.common.VMSEnum;
import com.vevs.entity.visitor.Visitor;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private final JavaMailSender javaMailSender;

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
    msg.setText(
        "Please enter this OTP in app to complete your entry and verification"
            + " in our organization : "
            + otp
            + " \n \n \n Best Regards "
            + " \n Pinki Lakhera \n +91 8095446907 \n \n Visitor Entry & Verification System  \n \n");
        javaMailSender.send(msg);
    }

    @Async
    public void sendEmailToHostEmployee(Visitor visitor) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(visitor.getHostEmployee().getEmail());
        msg.setFrom(VmsConstants.MAIL_SENDER);
        msg.setSubject(VmsConstants.APPLICATION.concat(visitor.getPurposeOfVisit().concat(" with ").concat(visitor.getName())));

        msg.setText("Dear " +StringUtils.capitalize(visitor.getHostEmployee().getName()) +", \n\n"
                +":::::::::::::::::::::::::::::::::::::::::::::::::::::: "
                +"\n: "+ visitor.getName()
                +"\n: "+ visitor.getVisitorEmail()
                +"\n: "+ visitor.getMobileNumber()
                +"\n:\n: "+ visitor.getPurposeOfVisit()
                +"\n:\n:\n:"+ VMSEnum.VISITOR_STATUS.CHECK_IN.name()+" - "+ visitor.getCreatedOn()
                +"\n:\n::::::::::::::::::::::::::::::::::::::::::::::::::::: "
                +"\n\n\n With Warm Regards "
                +"\n Anuj Kumar Pal \n +91 8095446907 \n \n Visitor Entry & Verification System  \n \n");
        javaMailSender.send(msg);
    }

    @Override
    public void sendEmailAboutVEVSOpenAccount(Users user,String password) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(user.getEmail());
        msg.setFrom(VmsConstants.MAIL_SENDER);
        msg.setSubject(VmsConstants.APPLICATION.concat(VmsConstants.CRA_SUBJECT));

        msg.setText("Dear " + StringUtils.capitalize(user.getName()) +", \n\n"
                +"::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: "
                +"\n: Your account has been created in Visitor Entry & Verification App"
                +"\n: This app will inform you when ever any visitor or outsider comes to meet with you" +
                " inside the organisation. "
                +"\n: "
                +"\n: You can logged-in with these credentials "
                +"\n: Username "+ user.getUsername()
                +"\n: Password "+ password
                +"\n: You are requests to you please change the password for the security reasons."
                +"\n:\n:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: "
                +"\n\n\n With Warm Regards "
                +"\n Pinki Lakhera \n +91 8095446907 \n \n Visitor Entry & Verification System  \n \n");
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
