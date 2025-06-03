package com.root14.barcodeservice.dto;

/**
 * Data Transfer Object (DTO) used for submitting barcode data in base64 format.
 * <p>
 * This record is typically used in JSON requests where the barcode image
 * is provided as a base64-encoded string.
 *
 * @param data the base64-encoded representation of the barcode image
 */
public record ReadDto(String data) {
}
