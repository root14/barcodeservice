package com.root14.barcodeservice.core;

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.awt.image.BufferedImage;
import java.util.Map;

public class QrGenerator implements Generator {
    private final QRCodeWriter qrCodeWriter;

    public QrGenerator(QRCodeWriter qrCodeWriter) {
        this.qrCodeWriter = qrCodeWriter;
    }

    public BufferedImage generateQr(String data, int width, int height, Map<EncodeHintType, Object> hints) throws WriterException {
        return generateQr(data, width, height, null, hints);
    }

    public BufferedImage generateQr(String data, int width, int height, MatrixToImageConfig config) throws WriterException {
        return generateQr(data, width, height, config, null);
    }

    public BufferedImage generateQr(String data, int width, int height) throws WriterException {
        return generateQr(data, width, height, null, null);
    }

    /**
     * @param minSize 1:1, 1:2, 2:3
     * @param ratio   for 2:3 -> 2.3
     */
    public BufferedImage generateQr(String data, int minSize, String ratio) {
        //todo calculate width and height
        //todo add auto scale calculation

        String[] parts = ratio.split(":");
        float widthRatio = Float.parseFloat(parts[0]);
        float heightRatio = Float.parseFloat(parts[1]);

        float scaleFactor = minSize / heightRatio;
        float scaledWidth = widthRatio * scaleFactor;

        //return generateQr(data,scaledWidth,minSize)
        return null;
    }

    /**
     * @apiNote hints.put(EncodeHintType.CHARACTER_SET, " UTF - 8 ");
     * @apiNote hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
     */
    public BufferedImage generateQr(String data, int width, int height, MatrixToImageConfig config, Map<EncodeHintType, Object> hints) throws WriterException {
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height, hints);

        return (config != null) ? MatrixToImageWriter.toBufferedImage(bitMatrix, config) : MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    @Override
    public BufferedImage generate(String data, int width, int height) throws WriterException {
        return generateQr(data, width, height);
    }

}
