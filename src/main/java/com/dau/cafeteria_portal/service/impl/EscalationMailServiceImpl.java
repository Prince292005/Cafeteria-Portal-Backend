package com.dau.cafeteria_portal.service.impl;
import jakarta.mail.MessagingException;
import com.dau.cafeteria_portal.service.EscalationMailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.util.Base64;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
// package com.dau.cafeteria_portal.service.impl;
// ... (imports)

@Service
@RequiredArgsConstructor
public class EscalationMailServiceImpl implements EscalationMailService {

    private final JavaMailSender mailSender;

     @Value("${brevo.api.key}")
    private String brevoApiKey;

    @Override
public void sendEscalationMail(
        String to,
        String subject,
        String htmlBody,
        File attachmentPath
) {

    RestTemplate restTemplate = new RestTemplate();

    String url = "https://api.brevo.com/v3/smtp/email";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("api-key", brevoApiKey);

    Map<String, Object> body = new HashMap<>();

    Map<String, String> sender = new HashMap<>();
    sender.put("name", "Cafeteria Portal");
    sender.put("email", "princesojitra29@gmail.com");

    Map<String, String> recipient = new HashMap<>();
    recipient.put("email", to);

    body.put("sender", sender);
    body.put("to", java.util.List.of(recipient));
    body.put("subject", subject);
    body.put("htmlContent", htmlBody);
    


try {
    if (attachmentPath != null && attachmentPath.exists()) {

        String base64Content = Base64.getEncoder()
                .encodeToString(Files.readAllBytes(attachmentPath.toPath()));

Map<String, String> attachment = new HashMap<>();

String fileName = attachmentPath.getName();

if (!fileName.contains(".")) {
    fileName += ".jpg";
}

attachment.put("name", fileName);
attachment.put("content", base64Content);

body.put("attachment", List.of(attachment));
    }
} catch (Exception e) {
    throw new RuntimeException("Failed to process attachment", e);
}

    HttpEntity<Map<String, Object>> request =
            new HttpEntity<>(body, headers);

    ResponseEntity<String> response =
            restTemplate.postForEntity(
                    url,
                    request,
                    String.class
            );

    System.out.println("Escalation Email Response: "
            + response.getStatusCode());
}
}

