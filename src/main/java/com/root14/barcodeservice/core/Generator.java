package com.root14.barcodeservice.core;

import com.google.zxing.WriterException;

import java.awt.image.BufferedImage;

/**
 * An interface defining the contract for barcode generation.
 * Implementations of this interface are responsible for encoding data into a barcode
 * and returning the result as a {@link BufferedImage}.
 */
public interface Generator {
    /**
     * Generates a barcode image from the provided data with specified dimensions.
     *
     * @param data The string data to be encoded into the barcode.
     * @param width The desired width of the generated barcode image.
     * @param height The desired height of the generated barcode image.
     * @return A {@link BufferedImage} representing the encoded barcode.
     * @throws WriterException If an error occurs during the barcode writing process (e.g., invalid data for format).
     */
    //encode -> return -> barcode.png
    BufferedImage generate(String data, int width, int height) throws WriterException;
}