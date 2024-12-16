package pe.edu.vallegrande.vgmsemail.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.vgmsemail.application.service.EmailService;
import pe.edu.vallegrande.vgmsemail.domain.model.Email;
import pe.edu.vallegrande.vgmsemail.domain.model.EmailResponse;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/management/email/v1")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public Mono<EmailResponse> sendEmail(@RequestBody Email email) {
        return emailService.sendEmail(email);
    }
}
