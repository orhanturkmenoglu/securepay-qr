package com.example.secure.pay.qr.util;

import com.example.secure.pay.qr.model.Payment;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class PdfInvoiceUtil {

    public static byte[] createSimpleInvoicePdf(Payment payment){
        Document document = new Document();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        PdfWriter.getInstance(document,stream);
        document.open();

        Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD);
        Font normalFont = new Font(Font.HELVETICA,12);

        document.add(new Paragraph("Fatura/Invoice",titleFont));
        document.add(new Paragraph("-------------"));

        document.add(new Paragraph("Fatura No (Token): " + payment.getToken(), normalFont));
        document.add(new Paragraph("Müşteri: Stripe Müşterisi", normalFont)); // Dilersen Payment'e müşteri alanı ekle
        document.add(new Paragraph("Tutar: " + payment.getAmount().toString() + " " + payment.getCurrency(), normalFont));
        document.add(new Paragraph("Açıklama: " + payment.getDescription(), normalFont));

        Instant updatedAt = payment.getUpdatedAt();

        String formattedDate = updatedAt
                .atZone(ZoneId.systemDefault())   // JVM default zone
                .toLocalDate()
                .format(DateTimeFormatter.ISO_LOCAL_DATE);

        document.add(new Paragraph("Ödeme Tarihi: " + formattedDate, normalFont));

        document.close();

        return  stream.toByteArray();
    }
    public static void  createInvoicePdfFile(Payment payment,String directoryPath) throws IOException {
        // Önce PDF byte dizisini oluştur
        byte[] pdfBytes = createSimpleInvoicePdf(payment);

        // Klasör yoksa oluştur
        Path dirPath = Paths.get(directoryPath);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        // Dosya yolu
        String fileName = "invoice_" + payment.getToken() + ".pdf";
        Path filePath = dirPath.resolve(fileName);

        // Dosyaya yaz
        try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
            fos.write(pdfBytes);
        }
    }
}
