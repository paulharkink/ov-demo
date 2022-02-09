package com.example.ovticket;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@Slf4j
public class QRGenerator {

    private final QRCodeWriter qrCodeWriter;
    private final int width;
    private final int height;

    public QRGenerator(@Value("${qr.width: 300}") int width, @Value("${qr.height: 300}") int height) {
        this.qrCodeWriter = new QRCodeWriter();
        this.width = width;
        this.height = height;
    }

    @Nullable
    public byte[] render(String text) {
        BitMatrix bitMatrix;
        try {
            bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageConfig con = new MatrixToImageConfig( 0xFF000002 , 0xFFFFC041 ) ;

            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream,con);
            return pngOutputStream.toByteArray();
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
