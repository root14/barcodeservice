package com.root14.barcodeservice;

import com.google.zxing.aztec.AztecWriter;
import com.google.zxing.datamatrix.DataMatrixWriter;
import com.google.zxing.pdf417.PDF417Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.oned.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BarcodeWriterConfigIntegrationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void allBarcodeWritersShouldBeAvailableInContext() {
        // Test D2WriterProvider beans
        assertThat(applicationContext.getBean(QRCodeWriter.class)).isNotNull();
        assertThat(applicationContext.getBean(DataMatrixWriter.class)).isNotNull();
        assertThat(applicationContext.getBean(AztecWriter.class)).isNotNull();
        assertThat(applicationContext.getBean(PDF417Writer.class)).isNotNull();

        // Test IndustrialWriterProvider beans
        assertThat(applicationContext.getBean(Code39Writer.class)).isNotNull();
        assertThat(applicationContext.getBean(Code93Writer.class)).isNotNull();
        assertThat(applicationContext.getBean(Code128Writer.class)).isNotNull();
        assertThat(applicationContext.getBean(CodaBarWriter.class)).isNotNull();
        assertThat(applicationContext.getBean(ITFWriter.class)).isNotNull();

        // Test ProductWriterProvider beans
        assertThat(applicationContext.getBean(UPCAWriter.class)).isNotNull();
        assertThat(applicationContext.getBean(UPCEWriter.class)).isNotNull();
        assertThat(applicationContext.getBean(EAN8Writer.class)).isNotNull();
        assertThat(applicationContext.getBean(EAN13Writer.class)).isNotNull();
    }
}