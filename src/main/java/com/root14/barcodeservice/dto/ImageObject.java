package com.root14.barcodeservice.dto;

import java.time.Instant;

public record ImageObject(String uuid, byte[] barcode, Instant createdAt) {
}
