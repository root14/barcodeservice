package com.root14.barcodeservice.core;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.aztec.AztecWriter;
import com.google.zxing.datamatrix.DataMatrixWriter;
import com.google.zxing.oned.*;
import com.google.zxing.pdf417.PDF417Writer;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Arrays;

public enum BarcodeType {
    QR("qr", QRCodeWriter.class, BarcodeFormat.QR_CODE),
    DATA_MATRIX("dataMatrix", DataMatrixWriter.class, BarcodeFormat.DATA_MATRIX),
    AZTEC("aztec", AztecWriter.class, BarcodeFormat.AZTEC),
    PDF417("pdf417", PDF417Writer.class, BarcodeFormat.PDF_417),

    CODE39("code39", Code39Writer.class, BarcodeFormat.CODE_39),
    CODE93("code93", Code39Writer.class, BarcodeFormat.CODE_93),
    CODE128("code128", Code128Writer.class, BarcodeFormat.CODE_128),
    CODA_BAR("codebar", CodaBarWriter.class, BarcodeFormat.CODABAR),
    ITF("itf", ITFWriter.class, BarcodeFormat.ITF),

    UPC_A("upc-a", UPCAWriter.class, BarcodeFormat.UPC_A),
    UPC_E("upc-e", UPCEWriter.class, BarcodeFormat.UPC_E),
    EAN8("ean-8", EAN8Writer.class, BarcodeFormat.EAN_8),
    EAN13("ean-13", EAN13Writer.class, BarcodeFormat.EAN_13);

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
