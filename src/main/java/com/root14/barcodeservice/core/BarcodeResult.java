package com.root14.barcodeservice.core;

import com.google.zxing.BarcodeFormat;

/**
 * Represents the result of a successfully decoded barcode.
 * <p>
 * This record encapsulates the key details extracted from the barcode,
 * including the timestamp of decoding, the textual content, and the barcode format.
 *
 * @param timestamp The time (in milliseconds) when the barcode was decoded.
 * @param text The decoded text contained in the barcode.
 * @param barcodeFormat The format/type of the barcode (e.g., QR_CODE, CODE_128).
 */
public record BarcodeResult(long timestamp, String text, BarcodeFormat barcodeFormat) {
}