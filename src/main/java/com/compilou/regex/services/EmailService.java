package com.compilou.regex.services;

import com.compilou.regex.models.Users;
import com.compilou.regex.models.records.CreateUserRequestDto;
import com.compilou.regex.util.MailUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;

@Service("emailService")
public class EmailService {

    private final Environment environment;
    private static JavaMailSender mailSender;
    private final TemplateEngine htmlTemplateEngine;

    @Autowired
    public EmailService(Environment environment,
                        JavaMailSender mailSender, TemplateEngine htmlTemplateEngine) {
        this.environment = environment;
        this.mailSender = mailSender;
        this.htmlTemplateEngine = htmlTemplateEngine;
    }

    public void sendMailCreate(Users users) throws MessagingException, UnsupportedEncodingException{
        String confirmationUrl = "generated_confirmation_url";
        String mailFrom = environment.getProperty("spring.mail.properties.mail.smtp.from");
        String mailFromName = "no-reply";

        if (mailFrom == null || mailFrom.isEmpty()) {
            throw new MessagingException("O e-mail do endereço não está configurado corretamente.");
        }

        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper email;
        email = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        email.setTo(users.getEmail());
        email.setSubject(MailUtil.MAIL_SUBJECT);
        email.setFrom(new InternetAddress(mailFrom, mailFromName));

        final Context ctx = new Context(LocaleContextHolder.getLocale());
        ctx.setVariable("email", users.getEmail());
        ctx.setVariable("name", users.getFullName());
        ctx.setVariable("welcome", MailUtil.WELCOME_IMAGE);
        ctx.setVariable("url", confirmationUrl);

        final String htmlContent = this.htmlTemplateEngine.process(MailUtil.TEMPLATE_NAME_CREATE, ctx);

        email.setText(htmlContent, true);

        ClassPathResource clr = new ClassPathResource(MailUtil.WELCOME_IMAGE);
        email.addInline("welcomeImage", clr, MailUtil.PNG_MIME);

        mailSender.send(mimeMessage);
    }

    public void sendMailCreateUserDto(CreateUserRequestDto createUserRequestDto) throws MessagingException, UnsupportedEncodingException{
        String confirmationUrl = "generated_confirmation_url";
        String mailFrom = environment.getProperty("spring.mail.properties.mail.smtp.from");
        String mailFromName = "no-reply";

        if (mailFrom == null || mailFrom.isEmpty()) {
            throw new MessagingException("O e-mail do endereço não está configurado corretamente.");
        }

        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper email;
        email = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        email.setTo(createUserRequestDto.email());
        email.setSubject(MailUtil.MAIL_SUBJECT);
        email.setFrom(new InternetAddress(mailFrom, mailFromName));

        final Context ctx = new Context(LocaleContextHolder.getLocale());
        ctx.setVariable("email", createUserRequestDto.email());
        ctx.setVariable("name", createUserRequestDto.fullName());
        ctx.setVariable("welcome", MailUtil.WELCOME_IMAGE);
        ctx.setVariable("url", confirmationUrl);

        final String htmlContent = this.htmlTemplateEngine.process(MailUtil.TEMPLATE_NAME_CREATE, ctx);

        email.setText(htmlContent, true);

        ClassPathResource clr = new ClassPathResource(MailUtil.WELCOME_IMAGE);
        email.addInline("welcomeImage", clr, MailUtil.PNG_MIME);

        mailSender.send(mimeMessage);
    }

    public void sendMailUpdate(Users users) throws MessagingException, UnsupportedEncodingException{
        String confirmationUrl = "generated_confirmation_url";
        String mailFrom = environment.getProperty("spring.mail.properties.mail.smtp.from");
        String mailFromName = "no-reply";

        if (mailFrom == null || mailFrom.isEmpty()) {
            throw new MessagingException("O e-mail do endereço não está configurado corretamente.");
        }

        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper email;
        email = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        email.setTo(users.getEmail());
        email.setSubject(MailUtil.MAIL_SUBJECT_UPDATE);
        email.setFrom(new InternetAddress(mailFrom, mailFromName));

        final Context ctx = new Context(LocaleContextHolder.getLocale());
        ctx.setVariable("email", users.getEmail());
        ctx.setVariable("name", users.getFullName());
        ctx.setVariable("update", MailUtil.UPDATE_IMAGE);
        ctx.setVariable("url", confirmationUrl);

        final String htmlContent = this.htmlTemplateEngine.process(MailUtil.TEMPLATE_NAME_UPDATE, ctx);

        email.setText(htmlContent, true);

        ClassPathResource clr = new ClassPathResource(MailUtil.UPDATE_IMAGE);
        email.addInline("updateImage", clr, MailUtil.PNG_MIME);

        mailSender.send(mimeMessage);
    }

    public void sendOtpEmail(String to, String otp, String resetLink) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject("Password Reset Request");
        helper.setText(String.format("Your OTP is: %s. Click the following link to reset your password: %s", otp, resetLink), true);

        mailSender.send(message);
    }

    public static void sendOtpEmail(String email, String otp) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Verify Account");
        mimeMessageHelper.setText(String.format("""
        <div>
            <p>Click the link below to verify your account:</p>
            <p><a href="http://localhost:8080/verify-account?email=%s&otp=%s" target="_blank">Verify Account</a></p>
        </div>
        """, email, otp), true);
        mailSender.send(mimeMessage);
    }

    public static void sendResetEmail(String email, String otp) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Reset Password OTP Verification");

        String resetLink = "http://localhost:8080/reset-password?email=%s&otp=%s";
        String emailBody = String.format("""
        <html>
        <body>
            <p>Dear User,</p>
            <p>You have requested to reset your password. Please click on the link below to proceed:</p>
            <p><a href="%s" target="_blank">Reset Password</a></p>
            <p>If you did not request this, please ignore this email.</p>
            <p>Regards,<br/>Your Application Team</p>
        </body>
        </html>
        """, String.format(resetLink, email, otp));

        mimeMessageHelper.setText(emailBody, true);
        mailSender.send(mimeMessage);
    }
}