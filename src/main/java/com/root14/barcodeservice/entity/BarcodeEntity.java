package com.root14.barcodeservice.entity;

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

    private String imageName;

    @CreatedDate
    private Instant createdAt;


    public BarcodeEntity(String imageName, Instant createdAt) {
        this.imageName = imageName;
        this.createdAt = createdAt;
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

    public String getImageName() {
        return imageName;
    }

    public BarcodeEntity setImageName(String imageName) {
        this.imageName = imageName;
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
