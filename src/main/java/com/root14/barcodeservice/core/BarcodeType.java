package com.root14.barcodeservice.core;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.aztec.AztecWriter;
import com.google.zxing.datamatrix.DataMatrixWriter;
import com.google.zxing.oned.*;
import com.google.zxing.pdf417.PDF417Writer;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Arrays;

/**
 * An enumeration representing different types of barcodes supported by the service.
 * Each enum constant maps to a specific {@link Writer} class from the Google ZXing library
 * and its corresponding {@link BarcodeFormat}.
 * This enum facilitates easy lookup and association of barcode types with their respective
 * writer implementations and formats.
 */
public enum BarcodeType {
    /** QR Code barcode type. */
    QR("qr", QRCodeWriter.class, BarcodeFormat.QR_CODE),
    /** Data Matrix barcode type. */
    DATA_MATRIX("dataMatrix", DataMatrixWriter.class, BarcodeFormat.DATA_MATRIX),
    /** Aztec Code barcode type. */
    AZTEC("aztec", AztecWriter.class, BarcodeFormat.AZTEC),
    /** PDF417 barcode type. */
    PDF417("pdf417", PDF417Writer.class, BarcodeFormat.PDF_417),

    /** Code 39 barcode type. */
    CODE39("code39", Code39Writer.class, BarcodeFormat.CODE_39),
    /** Code 93 barcode type. */
    CODE93("code93", Code39Writer.class, BarcodeFormat.CODE_93), // Note: ZXing's Code93Writer extends OneDWriter directly, not Code39Writer, but the provided code uses Code39Writer.class for CODE93.
    /** Code 128 barcode type. */
    CODE128("code128", Code128Writer.class, BarcodeFormat.CODE_128),
    /** Codabar barcode type. */
    CODA_BAR("codebar", CodaBarWriter.class, BarcodeFormat.CODABAR),
    /** Interleaved 2 of 5 (ITF) barcode type. */
    ITF("itf", ITFWriter.class, BarcodeFormat.ITF),

    /** UPC-A barcode type. */
    UPC_A("upc-a", UPCAWriter.class, BarcodeFormat.UPC_A),
    /** UPC-E barcode type. */
    UPC_E("upc-e", UPCEWriter.class, BarcodeFormat.UPC_E),
    /** EAN-8 barcode type. */
    EAN8("ean-8", EAN8Writer.class, BarcodeFormat.EAN_8),
    /** EAN-13 barcode type. */
    EAN13("ean-13", EAN13Writer.class, BarcodeFormat.EAN_13);

    private final String key;
    private final Class<? extends Writer> writerClass;
    private final BarcodeFormat format;

    /**
     * Constructs a BarcodeType enum constant.
     *
     * @param key A string key representing the barcode type.
     * @param writerClass The {@link Class} of the ZXing {@link Writer} associated with this barcode type.
     * @param format The {@link BarcodeFormat} enum value for this barcode type.
     */
    BarcodeType(String key, Class<? extends Writer> writerClass, BarcodeFormat format) {
        this.key = key;
        this.writerClass = writerClass;
        this.format = format;
    }

    /**
     * Returns the BarcodeType enum constant corresponding to the given key string.
     * The comparison is case-insensitive.
     *
     * @param key The string key to look up.
     * @return The {@link BarcodeType} enum constant.
     * @throws IllegalArgumentException If no barcode type is found for the given key.
     */
    public static BarcodeType fromKey(String key) {
        return Arrays.stream(values())
                .filter(bt -> bt.key.equalsIgnoreCase(key))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown barcode type: " + key));
    }

    /**
     * Returns the {@link Writer} class associated with this barcode type.
     *
     * @return The {@link Class} of the ZXing {@link Writer}.
     */
    public Class<? extends Writer> getWriterClass() {
        return writerClass;
    }

    /**
     * Returns the {@link BarcodeFormat} enum value associated with this barcode type.
     *
     * @return The {@link BarcodeFormat} for this barcode type.
     */
    public BarcodeFormat getFormat() {
        return format;
    }
}