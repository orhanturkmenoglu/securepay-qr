package com.example.secure.pay.qr.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender javaMailSender;

    private String buildPaymentSuccessBody(String customerName, String paymentToken) {
        String namePart = (customerName == null || customerName.isBlank()) ? "Değerli Müşteri" : customerName;
        return String.format(
                "Merhaba %s,\n\n" +
                        "Ödemeniz başarıyla tamamlandı.\n" +
                        "Fatura numaranız: %s\n\n" +
                        "Teşekkür ederiz.",
                namePart,
                paymentToken
        );
    }
    public void sendPaymentSuccessMail(String to, String customerName, String paymentToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Ödemeniz Başarıyla Tamamlandı - Fatura");
        message.setText(buildPaymentSuccessBody(customerName, paymentToken));

        javaMailSender.send(message);
        log.info("Mail sent to: {}", to);
    }
}
