package com.root14.barcodeservice.service;

import com.google.zxing.*;
import com.root14.barcodeservice.core.BarcodeGenerator;
import com.root14.barcodeservice.core.BarcodeReader;
import com.root14.barcodeservice.core.BarcodeType;
import com.root14.barcodeservice.dto.ImageObject;
import com.root14.barcodeservice.entity.BarcodeEntity;
import com.root14.barcodeservice.repository.BarcodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class for handling barcode-related operations.
 *
 * <p>This service is designed to work in both database-enabled and non-database profiles.
 * When the application runs in a profile without a database, the {@link BarcodeRepository}
 * may not be available, and related logic should handle that case accordingly.</p>
 *
 * <p>Use this class to implement business logic related to barcodes.</p>
 *
 * @see BarcodeRepository
 */
@Service
public class BarcodeService {
    private final ApplicationContext applicationContext;

    // Removed from Spring IoC, they are created once and reused.
    private final BarcodeGenerator barcodeGenerator = new BarcodeGenerator();
    private final BarcodeReader barcodeReader = new BarcodeReader();
    private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    private final BarcodeRepository barcodeRepository;

    @Value("${spring.profiles.active}")
    private String profile;

    /**
     * Constructs a {@code BarcodeService} instance with optional access to the {@link BarcodeRepository}
     * and access to the Spring {@link ApplicationContext}.
     *
     * <p>This constructor allows the service to be initialized even in environments where the
     * {@code BarcodeRepository} is not available—e.g., when the application is running in a profile
     * that does not connect to a database.</p>
     *
     * @param applicationContext the Spring application context, used for accessing beans and application metadata
     * @param barcodeRepository the repository used for barcode data access; may be {@code null} in non-database profiles
     */
    @Autowired
    public BarcodeService(ApplicationContext applicationContext, @Autowired(required = false) BarcodeRepository barcodeRepository) {
        this.applicationContext = applicationContext;
        this.barcodeRepository = barcodeRepository;
    }

    /**
     * Reads barcode data from an uploaded image file.
     *
     * @param data  the multipart file containing the barcode image
     * @param hints decoding hints for barcode recognition
     * @return the decoded barcode result
     * @throws IOException       if reading the file fails
     * @throws NotFoundException if no barcode is found in the image
     */
    public Result read(MultipartFile data, Map<DecodeHintType, Object> hints) throws IOException, NotFoundException {
        return barcodeReader.read(data.getInputStream(), hints);
    }

    /**
     * Reads barcode data from a Base64-encoded image string.
     *
     * @param data  the Base64-encoded image data
     * @param hints decoding hints for barcode recognition
     * @return the decoded barcode result
     * @throws IOException       if reading the data fails
     * @throws NotFoundException if no barcode is found in the image
     */
    public Result read(String data, Map<DecodeHintType, Object> hints) throws IOException, NotFoundException {
        byte[] decoded = Base64.getDecoder().decode(data.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ByteArrayInputStream(decoded);
        return barcodeReader.read(inputStream, hints);
    }

    /**
     * Generates a barcode image from the given data and saves it if a name is provided.
     *
     * @param type   the barcode type (e.g., "QR", "CODE_128")
     * @param data   the data to encode in the barcode
     * @param width  the width of the generated image
     * @param height the height of the generated image
     * @param store  to persist the barcode in the database
     * @return an {@link Optional} containing the generated {@link ImageObject}, or empty if generation fails
     * @throws WriterException if encoding the barcode fails
     * @throws IOException     if writing the image fails
     */
    public Optional<ImageObject> generate(String type, String data, int width, int height, boolean store) throws WriterException, IOException {
        BarcodeType barcodeType = BarcodeType.fromKey(type);

        barcodeGenerator.setBarcodeFormat(barcodeType.getFormat());
        barcodeGenerator.setWriter(applicationContext.getBean(barcodeType.getWriterClass()));

        BufferedImage generated = barcodeGenerator.generate(data, width, height);

        // Clear the previous stream
        byteArrayOutputStream.reset();
        ImageIO.write(generated, "png", byteArrayOutputStream);

        // Save to database if a name is provided
        // Save only postgres mode/profile
        if (store && profile.equals("postgres")) {
            BarcodeEntity barcodeEntity = new BarcodeEntity(byteArrayOutputStream.toByteArray());
            BarcodeEntity storedEntity = barcodeRepository.save(barcodeEntity);
            return Optional.of(new ImageObject(storedEntity.getId().toString(), byteArrayOutputStream.toByteArray(), storedEntity.getCreatedAt()));
        } else {
            return Optional.of(new ImageObject(null, byteArrayOutputStream.toByteArray(), Instant.now()));
        }
    }

    /**
     * Retrieves a previously stored barcode image by UUID.
     *
     * @param uuid the unique identifier of the barcode
     * @return an {@link Optional} containing the found {@link ImageObject}, or empty if not found
     */
    public Optional<ImageObject> findBarcode(String uuid) {
        Optional<BarcodeEntity> barcodeEntityOptional = barcodeRepository.findById(UUID.fromString(uuid));
        return barcodeEntityOptional.map(BarcodeEntity::getAsDto);
    }
}
