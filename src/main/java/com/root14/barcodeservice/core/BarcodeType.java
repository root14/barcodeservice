package com.root14.barcodeservice.core;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.datamatrix.DataMatrixWriter;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Arrays;

public enum BarcodeType {
    QR("qr", QRCodeWriter.class, BarcodeFormat.QR_CODE),
    DATA_MATRIX("dataMatrix", DataMatrixWriter.class, BarcodeFormat.DATA_MATRIX),
    CODE_128("code128", Code128Writer.class, BarcodeFormat.CODE_128);

    private final String key;
    private final Class<? extends Writer> writerClass;
    private final BarcodeFormat format;

    BarcodeType(String key, Class<? extends Writer> writerClass, BarcodeFormat format) {
        this.key = key;
        this.writerClass = writerClass;
        this.format = format;
    }

    public static BarcodeType fromKey(String key) {
        return Arrays.stream(values())
                .filter(bt -> bt.key.equalsIgnoreCase(key))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown barcode type: " + key));
    }

    public Class<? extends Writer> getWriterClass() {
        return writerClass;
    }

    public BarcodeFormat getFormat() {
        return format;
    }
}
