package fr.bbougon.ousontmesaffaires.infrastructure.qrcode;

import ch.qos.logback.classic.Level;
import fr.bbougon.ousontmesaffaires.test.utils.FileUtils;
import fr.bbougon.ousontmesaffaires.test.utils.TestAppender;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class QRGeneratorEngineTest {

    @Test
    public void canGenerateQRCode() {
        QRGeneratorEngine qrGeneratorEngine = new QRGeneratorEngine(new Codec());

        String qrCOde = qrGeneratorEngine.encodeToBase64("http://localhost");

        assertThat(qrCOde).isEqualTo("iVBORw0KGgoAAAANSUhEUgAAAJYAAACWAQAAAAAUekxPAAAA3klEQVR42u2WMQ6DMAxFP+qQkSP4Js3FIoHExZqb9AiMGaK6dqBqgY720ApLCcpbov/5tgI+VMXJfoFVSA1c4izfzokR85xQunlgzl4McrzLvQnBkyVw9mYDI7qy5h++eGrHNAeJQt5nw5CthXjMuBmrKgso136SMPgwvsuaODzwoc2YVZJ2SbKtAl2YXNePCLe+IrAPE21Z+rIF7pU1c9Z+PjX/ghNb8qzbZeOfKdO+lJbceGrO0HKg84VdmR5HemvzYFTUv702O7bMSQk1ubFlTrLOyRJ92Plu+hP2BDUZM95rd7QpAAAAAElFTkSuQmCC");
    }

    @Test
    public void logWarnOnExceptions() {
        String content = new FileUtils("file/large-file.txt").getContent();

        String encodedQRCode = new QRGeneratorEngine(new Codec()).encodeToBase64(content);

        assertThat(encodedQRCode).isNull();
        assertThat(TestAppender.hasMessageInLevel(Level.DEBUG, "Error while generating QRCode for content : " + content)).isTrue();
    }
}