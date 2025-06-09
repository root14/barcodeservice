package com.root14.barcodeservice.core;

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * A utility class responsible for generating barcode images using the Google ZXing library.
 * This class implements the {@link Generator} interface and provides various overloaded
 * methods to generate barcode images with different configurations, including
 * custom image configurations and encoding hints.
 */
public class BarcodeGenerator implements Generator {
    private Writer writer;
    public BarcodeFormat barcodeFormat;

    /**
     * Generates a barcode image with the specified data, width, and height.
     * This method delegates to the {@link #generateQr(String, int, int)} method.
     *
     * @param data The data to encode in the barcode.
     * @param width The desired width of the barcode image.
     * @param height The desired height of the barcode image.
     * @return A {@link BufferedImage} representing the generated barcode.
     * @throws WriterException If an error occurs during barcode encoding.
     */
    @Override
    public BufferedImage generate(String data, int width, int height) throws WriterException {
        return generateQr(data, width, height);
    }

    /**
     * Generates a barcode image with the specified data, width, height, and encoding hints.
     *
     * @param data The data to encode in the barcode.
     * @param width The desired width of the barcode image.
     * @param height The desired height of the barcode image.
     * @param hints A {@link Map} of {@link EncodeHintType} to {@link Object} providing encoding hints.
     * @return A {@link BufferedImage} representing the generated barcode.
     * @throws WriterException If an error occurs during barcode encoding.
     */
    public BufferedImage generateQr(String data, int width, int height, Map<EncodeHintType, Object> hints) throws WriterException {
        return generateQr(data, width, height, null, hints);
    }

    /**
     * Generates a barcode image with the specified data, width, height, and image configuration.
     *
     * @param data The data to encode in the barcode.
     * @param width The desired width of the barcode image.
     * @param height The desired height of the barcode image.
     * @param config A {@link MatrixToImageConfig} object to customize image rendering.
     * @return A {@link BufferedImage} representing the generated barcode.
     * @throws WriterException If an error occurs during barcode encoding.
     */
    public BufferedImage generateQr(String data, int width, int height, MatrixToImageConfig config) throws WriterException {
        return generateQr(data, width, height, config, null);
    }

    /**
     * Generates a barcode image with the specified data, width, and height using default configurations.
     *
     * @param data The data to encode in the barcode.
     * @param width The desired width of the barcode image.
     * @param height The desired height of the barcode image.
     * @return A {@link BufferedImage} representing the generated barcode.
     * @throws WriterException If an error occurs during barcode encoding.
     */
    public BufferedImage generateQr(String data, int width, int height) throws WriterException {
        return generateQr(data, width, height, null, null);
    }

    /**
     * Generates a barcode image with the specified data, dimensions, image configuration, and encoding hints.
     * This is the most comprehensive `generateQr` method.
     *
     * @apiNote hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
     * @apiNote hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
     *
     * @param data The data to encode in the barcode.
     * @param width The desired width of the barcode image.
     * @param height The desired height of the barcode image.
     * @param config An optional {@link MatrixToImageConfig} object to customize image rendering (can be null for default).
     * @param hints An optional {@link Map} of {@link EncodeHintType} to {@link Object} providing encoding hints (can be null for default).
     * @return A {@link BufferedImage} representing the generated barcode.
     * @throws WriterException If an error occurs during barcode encoding.
     */
    public BufferedImage generateQr(String data, int width, int height, MatrixToImageConfig config, Map<EncodeHintType, Object> hints) throws WriterException {
        BitMatrix bitMatrix = writer.encode(data, barcodeFormat, width, height, hints);

        return (config != null) ? MatrixToImageWriter.toBufferedImage(bitMatrix, config) : MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    /**
     * Retrieves the currently set barcode format.
     *
     * @return The current {@link BarcodeFormat}.
     */
    public BarcodeFormat getBarcodeFormat() {
        return barcodeFormat;
    }

    /**
     * Sets the barcode format to be used for generation.
     *
     * @param barcodeFormat The {@link BarcodeFormat} to set.
     * @return The current {@link BarcodeGenerator} instance for method chaining.
     */
    public BarcodeGenerator setBarcodeFormat(BarcodeFormat barcodeFormat) {
        this.barcodeFormat = barcodeFormat;
        return this;
    }

    /**
     * Retrieves the currently set barcode writer.
     *
     * @return The current {@link Writer} instance.
     */
    public Writer getWriter() {
        return writer;
    }

    /**
     * Sets the barcode writer to be used for generation.
     *
     * @param writer The {@link Writer} instance to set.
     * @return The current {@link BarcodeGenerator} instance for method chaining.
     */
    public BarcodeGenerator setWriter(Writer writer) {
        this.writer = writer;
        return this;
    }
}