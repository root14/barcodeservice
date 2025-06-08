package com.root14.barcodeservice;

import com.google.zxing.*;
import com.google.zxing.qrcode.QRCodeWriter;
import com.root14.barcodeservice.core.BarcodeGenerator;
import com.root14.barcodeservice.core.BarcodeReader;
import com.root14.barcodeservice.core.BarcodeType;
import com.root14.barcodeservice.dto.ImageObject;
import com.root14.barcodeservice.entity.BarcodeEntity;
import com.root14.barcodeservice.repository.BarcodeRepository;
import com.root14.barcodeservice.service.BarcodeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BarcodeServiceTest {

    AutoCloseable closeable;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private BarcodeRepository barcodeRepository;

    @Mock
    private BarcodeReader barcodeReader;

    @Mock
    private BarcodeGenerator barcodeGenerator;

    @InjectMocks
    private BarcodeService barcodeService;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        barcodeService = new BarcodeService(applicationContext, barcodeRepository); // Re-initialize after mocks are opened
        ReflectionTestUtils.setField(barcodeService, "profile", "postgres"); // Default to postgres profile for tests
        ReflectionTestUtils.setField(barcodeService, "barcodeReader", barcodeReader);
        ReflectionTestUtils.setField(barcodeService, "barcodeGenerator", barcodeGenerator);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void generate_shouldReturnImageObjectWithDbStore() throws Exception {
        String data = "test-data";
        int width = 200, height = 200;
        BarcodeType type = BarcodeType.QR;
        Writer writer = mock(QRCodeWriter.class); // use a specific writer for the mock

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        when((Writer) applicationContext.getBean(type.getWriterClass())).thenReturn(writer);
        when(barcodeGenerator.generate(data, width, height)).thenReturn(image);

        byte[] imageBytes;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", baos);
            imageBytes = baos.toByteArray();
        }

        BarcodeEntity entity = new BarcodeEntity(imageBytes);
        UUID generatedUuid = UUID.randomUUID();
        Instant createdAt = Instant.now();
        ReflectionTestUtils.setField(entity, "id", generatedUuid);
        ReflectionTestUtils.setField(entity, "createdAt", createdAt);

        when(barcodeRepository.save(any(BarcodeEntity.class))).thenReturn(entity);

        Optional<ImageObject> result = barcodeService.generate("QR", data, width, height, true);

        assertTrue(result.isPresent());
        assertNotNull(result.get().barcode());
        assertEquals(generatedUuid.toString(), result.get().uuid());
        assertEquals(createdAt, result.get().createdAt());
        verify(barcodeRepository, times(1)).save(any(BarcodeEntity.class));
    }

    @Test
    void generate_shouldReturnImageObjectWithoutDbStore_whenProfileIsNotPostgres() throws Exception {
        ReflectionTestUtils.setField(barcodeService, "profile", "dev"); // set profile to non-postgres

        String data = "no-db";
        int width = 100, height = 100;
        BarcodeType type = BarcodeType.QR;
        Writer writer = mock(QRCodeWriter.class);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        when((Writer)applicationContext.getBean(type.getWriterClass())).thenReturn(writer);
        when(barcodeGenerator.generate(data, width, height)).thenReturn(image);

        Optional<ImageObject> result = barcodeService.generate("QR", data, width, height, true); // Still pass true for store

        assertTrue(result.isPresent());
        assertNotNull(result.get().barcode());
        assertNull(result.get().uuid()); // assert UUID is null as it shouldn't be stored
        verify(barcodeRepository, never()).save(any(BarcodeEntity.class)); // verify save was NOT called
    }

    @Test
    void generate_shouldReturnImageObjectWithoutDbStore_whenStoreIsFalse() throws Exception {
        // profile is "postgres" but 'store' is false
        ReflectionTestUtils.setField(barcodeService, "profile", "postgres");

        String data = "no-db-explicitly";
        int width = 100, height = 100;
        BarcodeType type = BarcodeType.QR;
        Writer writer = mock(QRCodeWriter.class);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        when((Writer)applicationContext.getBean(type.getWriterClass())).thenReturn(writer);
        when(barcodeGenerator.generate(data, width, height)).thenReturn(image);

        Optional<ImageObject> result = barcodeService.generate("QR", data, width, height, false); // explicitly false

        assertTrue(result.isPresent());
        assertNotNull(result.get().barcode());
        assertNull(result.get().uuid());
        verify(barcodeRepository, never()).save(any(BarcodeEntity.class)); // verify save was NOT called
    }

    @Test
    void findBarcode_shouldReturnImageObject() {
        UUID id = UUID.randomUUID();
        byte[] testBytes = "test".getBytes();
        BarcodeEntity entity = new BarcodeEntity(testBytes);
        ReflectionTestUtils.setField(entity, "id", id);
        ReflectionTestUtils.setField(entity, "createdAt", Instant.now());

        when(barcodeRepository.findById(id)).thenReturn(Optional.of(entity));

        Optional<ImageObject> result = barcodeService.findBarcode(id.toString());

        assertTrue(result.isPresent());
        assertEquals(id.toString(), result.get().uuid());
        assertArrayEquals(testBytes, result.get().barcode());
    }

    @Test
    void findBarcode_shouldReturnEmpty() {
        UUID id = UUID.randomUUID();
        when(barcodeRepository.findById(id)).thenReturn(Optional.empty());

        Optional<ImageObject> result = barcodeService.findBarcode(id.toString());

        assertFalse(result.isPresent());
    }

    @Test
    void read_shouldDecodeFromBase64() throws Exception {
        String testData = "test-barcode";
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", byteArrayOutputStream);
        String base64 = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());

        Result mockResult = new Result(testData, null, null, BarcodeFormat.QR_CODE);
        when(barcodeReader.read(any(InputStream.class), anyMap())).thenReturn(mockResult);

        Result result = barcodeService.read(base64, Collections.emptyMap());

        assertNotNull(result);
        assertEquals(testData, result.getText());
        verify(barcodeReader, times(1)).read(any(InputStream.class), anyMap()); // verify barcodeReader.read was called
    }

    @Test
    void read_shouldDecodeFromMultipartFile() throws Exception {
        String testData = "multipart-file-barcode";
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", byteArrayOutputStream);
        MockMultipartFile mockFile = new MockMultipartFile("file", "barcode.png", "image/png", byteArrayOutputStream.toByteArray());

        Result mockResult = new Result(testData, null, null, BarcodeFormat.CODE_128);
        when(barcodeReader.read(any(InputStream.class), anyMap())).thenReturn(mockResult);

        Result result = barcodeService.read(mockFile, Collections.emptyMap());

        assertNotNull(result);
        assertEquals(testData, result.getText());
        verify(barcodeReader, times(1)).read(any(InputStream.class), anyMap());
    }

    @Test
    void read_shouldThrowNotFoundException() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("file", "empty.png", "image/png", new byte[]{});

        // simulate NotFoundException from barcodeReader
        when(barcodeReader.read(any(InputStream.class), anyMap())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> barcodeService.read(mockFile, Collections.emptyMap()));
        verify(barcodeReader, times(1)).read(any(InputStream.class), anyMap());
    }

    @Test
    void generate_shouldThrowWriterException() throws Exception {
        String data = "error-data";
        int width = 200, height = 200;
        BarcodeType type = BarcodeType.QR;
        Writer writer = mock(QRCodeWriter.class);

        when((Writer)applicationContext.getBean(type.getWriterClass())).thenReturn(writer);
        // simulate WriterException during barcode generation
        when(barcodeGenerator.generate(data, width, height)).thenThrow(WriterException.class);

        assertThrows(WriterException.class, () -> barcodeService.generate("QR", data, width, height, false));
        verify(barcodeRepository, never()).save(any(BarcodeEntity.class)); // ensure no save attempt
    }

    @Test
    void generate_shouldThrowIOExceptionOnImageWrite() throws Exception {
        String data = "io-error";
        int width = 200, height = 200;
        BarcodeType type = BarcodeType.QR;
        Writer writer = mock(QRCodeWriter.class);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        when((Writer)applicationContext.getBean(type.getWriterClass())).thenReturn(writer);
        when(barcodeGenerator.generate(data, width, height)).thenReturn(image);
    }
}