package com.root14.barcodeservice.dto;

import java.time.Instant;

/**
 * Data Transfer Object (DTO) representing a stored image containing a barcode.
 * <p>
 * This record is typically used to encapsulate metadata and raw image data
 * for barcodes that have been saved to a persistent store.
 *
 * @param uuid a unique identifier associated with the image object
 * @param barcode the raw byte array representing the barcode image
 * @param createdAt the timestamp indicating when the image was created or stored
 */
public record ImageObject(String uuid, byte[] barcode, Instant createdAt) {
}
