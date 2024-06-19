package com.compilou.regex.services;

import com.compilou.regex.models.Users;
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
    private final JavaMailSender mailSender;
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
}