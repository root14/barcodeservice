package com.root14.barcodeservice.core;

import com.google.zxing.WriterException;

import java.awt.image.BufferedImage;

public interface Generator {
    //encode -> return -> barcode.png
    BufferedImage generate(String data, int width, int height) throws WriterException;
}
