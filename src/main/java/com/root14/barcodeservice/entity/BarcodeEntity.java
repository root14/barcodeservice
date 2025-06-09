package com.root14.barcodeservice.entity;

import com.root14.barcodeservice.dto.ImageObject;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

/**
 * Represents a barcode entity stored in the database.
 * This entity is mapped to the "barcodes" table and includes fields for a unique ID,
 * creation timestamp, and the barcode image data as a byte array.
 * It uses JPA annotations for persistence and Spring Data's auditing features to
 * automatically populate the creation date.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "barcodes")
public class BarcodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @CreatedDate
    private Instant createdAt;

    @Lob
    private byte[] barcode;

    /**
     * Constructs a new BarcodeEntity with the given barcode byte array.
     *
     * @param barcode The byte array representing the barcode image.
     */
    public BarcodeEntity(byte[] barcode) {
        this.barcode = barcode;
    }

    /**
     * Converts this BarcodeEntity into an {@link ImageObject} DTO.
     *
     * @return An {@link ImageObject} containing the ID, barcode byte array, and creation timestamp.
     */
    public ImageObject getAsDto() {
        return new ImageObject(getId().toString(), getBarcode(), getCreatedAt());
    }

    /**
     * Retrieves the barcode image data as a byte array.
     *
     * @return The barcode image data.
     */
    public byte[] getBarcode() {
        return barcode;
    }

    /**
     * Sets the barcode image data for this entity.
     *
     * @param barcode The byte array representing the barcode image.
     * @return The current BarcodeEntity instance for method chaining.
     */
    public BarcodeEntity setBarcode(byte[] barcode) {
        this.barcode = barcode;
        return this;
    }

    /**
     * Default constructor for BarcodeEntity. Required by JPA.
     */
    public BarcodeEntity() {
    }

    /**
     * Retrieves the unique identifier of the barcode entity.
     *
     * @return The UUID of the barcode.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the barcode entity.
     *
     * @param id The UUID to set for the barcode.
     * @return The current BarcodeEntity instance for method chaining.
     */
    public BarcodeEntity setId(UUID id) {
        this.id = id;
        return this;
    }

    /**
     * Retrieves the creation timestamp of the barcode entity.
     *
     * @return The {@link Instant} when the barcode entity was created.
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp of the barcode entity.
     *
     * @param createdAt The {@link Instant} to set as the creation timestamp.
     * @return The current BarcodeEntity instance for method chaining.
     */
    public BarcodeEntity setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}