package com.root14.barcodeservice.core;

import com.google.zxing.BarcodeFormat;

public record BarcodeResult(long timestamp, String text, BarcodeFormat barcodeFormat) {
}
