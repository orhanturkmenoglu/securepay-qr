package com.example.secure.pay.qr.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
public class QRCodeGenerator {

    public static byte[] generateQRCodeImage(String paymentUrl) throws WriterException, IOException {
        log.info("QRCodeGenerator ::generateQRCodeImage ");

        int width = 300;
        int height = 300;

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(paymentUrl, BarcodeFormat.QR_CODE, width, height);
        log.info("QRCodeGenerator ::generateQRCodeImage bitMatrix: {}", bitMatrix);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

        return pngOutputStream.toByteArray();
    }
}
