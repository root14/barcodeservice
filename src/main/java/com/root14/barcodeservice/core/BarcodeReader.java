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
 * MultiFormatReader implement all readers. it's an orchestrator class
 * InputStream is used to abstract from the spring framework.
 */
public class BarcodeReader {
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
