package com.root14.barcodeservice.entity;

import com.root14.barcodeservice.dto.ImageObject;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

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

    public BarcodeEntity(byte[] barcode) {
        this.barcode = barcode;
    }

    public ImageObject getAsDto() {
        return new ImageObject(getId().toString(), getBarcode(), getCreatedAt());
    }

    public byte[] getBarcode() {
        return barcode;
    }

    public BarcodeEntity setBarcode(byte[] barcode) {
        this.barcode = barcode;
        return this;
    }

    public BarcodeEntity() {
    }

    public UUID getId() {
        return id;
    }

    public BarcodeEntity setId(UUID id) {
        this.id = id;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public BarcodeEntity setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}
