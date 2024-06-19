package com.compilou.regex.services;

import com.compilou.regex.models.Users;
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

    private static final String TEMPLATE_NAME = "registration";
    private static final String WELCOME_IMAGE = "templates/images/welcome.png";
    private static final String SPRING_LOGO_IMAGE = "templates/images/spring.png";
    private static final String PNG_MIME = "image/png";
    private static final String MAIL_SUBJECT = "Seja bem-vindo(a)!";

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

    public void sendMailWithInline(Users users) throws MessagingException, UnsupportedEncodingException{
        String confirmationUrl = "generated_confirmation_url";
        String mailFrom = environment.getProperty("spring.mail.properties.mail.smtp.from");
        String mailFromName = "no-reply";
        String mailFromName = "Identity";

        if (mailFrom == null || mailFrom.isEmpty()) {
            throw new MessagingException("O e-mail do endereço não está configurado corretamente.");
        }

        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper email;
        email = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        email.setTo(users.getEmail());
        email.setSubject(MAIL_SUBJECT);
        email.setFrom(new InternetAddress(mailFrom, mailFromName));

        final Context ctx = new Context(LocaleContextHolder.getLocale());
        ctx.setVariable("email", users.getEmail());
        ctx.setVariable("name", users.getFullName());
        ctx.setVariable("welcome", WELCOME_IMAGE);
        ctx.setVariable("logo", SPRING_LOGO_IMAGE);
        ctx.setVariable("url", confirmationUrl);

        final String htmlContent = this.htmlTemplateEngine.process(TEMPLATE_NAME, ctx);

        email.setText(htmlContent, true);

        ClassPathResource clr = new ClassPathResource(WELCOME_IMAGE);
        email.addInline("welcomeImage", clr, PNG_MIME);

        mailSender.send(mimeMessage);
    }
}
        ClassPathResource clr = new ClassPathResource(SPRING_LOGO_IMAGE);

        email.addInline("logoimage", clr, PNG_MIME);

        mailSender.send(mimeMessage);
    }
}