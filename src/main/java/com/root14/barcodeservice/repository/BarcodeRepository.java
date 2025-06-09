package com.root14.barcodeservice.repository;

import com.root14.barcodeservice.entity.BarcodeEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

/**
 * Repository interface for managing {@link BarcodeEntity} instances.
 * This interface extends Spring Data JPA's {@link CrudRepository}, providing
 * standard CRUD (Create, Read, Update, Delete) operations for {@link BarcodeEntity}
 * objects with {@link UUID} as the primary key type.
 */
public interface BarcodeRepository extends CrudRepository<BarcodeEntity, UUID> {
}