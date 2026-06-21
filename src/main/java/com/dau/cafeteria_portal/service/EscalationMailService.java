package com.dau.cafeteria_portal.service;

import jakarta.mail.MessagingException;

import java.io.File;

public interface EscalationMailService {
    void sendEscalationMail(String to, String subject, String htmlBody, File attachment) throws MessagingException;
}
