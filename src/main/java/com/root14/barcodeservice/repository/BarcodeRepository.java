package com.root14.barcodeservice.repository;

import com.root14.barcodeservice.entity.BarcodeEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface BarcodeRepository extends CrudRepository<BarcodeEntity, UUID> {
}
