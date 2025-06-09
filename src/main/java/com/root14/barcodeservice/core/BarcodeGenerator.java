package com.root14.barcodeservice.core;

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.awt.image.BufferedImage;
import java.util.Map;

public class BarcodeGenerator implements Generator {
    Writer writer;
    public BarcodeFormat barcodeFormat;

    @Override
    public BufferedImage generate(String data, int width, int height) throws WriterException {
        return generateQr(data, width, height);
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
     * @apiNote hints.put(EncodeHintType.CHARACTER_SET, " UTF - 8 ");
     * @apiNote hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
     */
    public BufferedImage generateQr(String data, int width, int height, MatrixToImageConfig config, Map<EncodeHintType, Object> hints) throws WriterException {
        BitMatrix bitMatrix = writer.encode(data, barcodeFormat, width, height, hints);

        return (config != null) ? MatrixToImageWriter.toBufferedImage(bitMatrix, config) : MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    public BarcodeFormat getBarcodeFormat() {
        return barcodeFormat;
    }

    public BarcodeGenerator setBarcodeFormat(BarcodeFormat barcodeFormat) {
        this.barcodeFormat = barcodeFormat;
        return this;
    }

    public Writer getWriter() {
        return writer;
    }

    public BarcodeGenerator setWriter(Writer writer) {
        this.writer = writer;
        return this;
    }
}
