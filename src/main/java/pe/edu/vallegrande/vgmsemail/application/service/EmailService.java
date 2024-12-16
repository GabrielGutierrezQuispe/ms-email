package pe.edu.vallegrande.vgmsemail.application.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.vgmsemail.domain.model.Email;
import pe.edu.vallegrande.vgmsemail.domain.model.EmailResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public Mono<EmailResponse> sendEmail(Email email) {
        return Mono.fromCallable(() -> {
                    MimeMessage mimeMessage = mailSender.createMimeMessage();
                    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                    helper.setTo(email.getTo());
                    helper.setSubject(email.getSubject());

                    String body = loadEmailTemplate()
                            .replace("${username}", email.getUsername())
                            .replace("${mainMessage}", email.getMainMessage())
                            .replace("${link}", email.getLink());

                    helper.setText(body, true);
                    mailSender.send(mimeMessage);
                    return new EmailResponse(true);
                })
                .subscribeOn(Schedulers.boundedElastic())
                .onErrorReturn(new EmailResponse(false));
    }

    private String loadEmailTemplate() throws IOException {
        Path path = new ClassPathResource("templates/emailTemplate.html").getFile().toPath();
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
