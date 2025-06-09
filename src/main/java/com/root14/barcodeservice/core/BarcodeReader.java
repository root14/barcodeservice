package com.root14.barcodeservice.core;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * A utility class for reading and decoding barcode images.
 * It leverages the {@link MultiFormatReader} from the Google ZXing library,
 * which acts as an orchestrator for various barcode decoding algorithms.
 * This class abstracts the image input using {@link InputStream} to remain independent
 * of specific frameworks like Spring.
 */
public class BarcodeReader {
    /**
     * Reads and decodes a barcode from the given {@link InputStream}.
     * The method processes the input stream to create a {@link BufferedImage},
     * converts it into a {@link BinaryBitmap}, and then uses {@link MultiFormatReader}
     * to decode the barcode with optional hints.
     *
     * @param inputStreamData The {@link InputStream} containing the barcode image data.
     * @param hints An optional {@link Map} of {@link DecodeHintType} to {@link Object} providing decoding hints.
     * @return A {@link Result} object containing the decoded barcode information.
     * @throws IOException If an I/O error occurs while reading the image stream.
     * @throws NotFoundException If no barcode can be found or decoded from the image.
     * @throws IllegalArgumentException If the input stream does not contain a valid image.
     */
    public Result read(InputStream inputStreamData, Map<DecodeHintType, Object> hints) throws IOException, NotFoundException {
        BufferedImage bufferedImage = ImageIO.read(inputStreamData);

        if (bufferedImage == null) {
            throw new IllegalArgumentException("invalid image.");
        }

        LuminanceSource luminanceSource = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(luminanceSource));

        return new MultiFormatReader().decode(binaryBitmap, hints);
    }
}