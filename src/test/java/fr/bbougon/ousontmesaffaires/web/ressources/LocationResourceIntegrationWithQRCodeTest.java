package fr.bbougon.ousontmesaffaires.web.ressources;

import fr.bbougon.ousontmesaffaires.WithEmbeddedServer;
import fr.bbougon.ousontmesaffaires.test.utils.FileUtilsForTest;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;

import static fr.bbougon.ousontmesaffaires.infrastructure.pdf.StickerPdfAssertions.assertQrCode;
import static fr.bbougon.ousontmesaffaires.infrastructure.pdf.StickerPdfAssertions.assertTitle;
import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;

public class LocationResourceIntegrationWithQRCodeTest {

    @Rule
    public WithEmbeddedServer withEmbeddedServer = new WithEmbeddedServer(true);

    @Test
    public void canGenerateSticker() throws IOException {
        Response location = createLocation("json/pantalon.json");
        String stringUri = location.getLocation().toASCIIString();
        String locationId = stringUri.substring(stringUri.lastIndexOf("/") + 1);

        Response response = ClientBuilder.newClient().target("http://localhost:17000")
                .path("locations")
                .path("sticker")
                .queryParam("locationId", locationId)
                .queryParam("for", "http://another-host/locations")
                .request()
                .accept(MediaType.APPLICATION_OCTET_STREAM_TYPE)
                .get();

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        InputStream pdfStream = response.readEntity(InputStream.class);
        PDDocument document = PDDocument.load(pdfStream);
        assertTitle("placard", document);
        assertQrCode("http://another-host/locations/" + locationId, document);
        document.close();
    }

    private Response createLocation(final String resourceFilePath) {
        String payload = new FileUtilsForTest(resourceFilePath).getContent();

        return ClientBuilder.newClient().target("http://localhost:17000")
                .path("/locations")
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(payload), Response.class);
    }

}