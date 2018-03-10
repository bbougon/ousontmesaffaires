package fr.bbougon.ousontmesaffaires.command.location;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fr.bbougon.ousontmesaffaires.domain.location.Feature;
import fr.bbougon.ousontmesaffaires.domain.location.Item;
import fr.bbougon.ousontmesaffaires.domain.location.Location;
import fr.bbougon.ousontmesaffaires.infrastructure.pdf.PdfGenerator;
import fr.bbougon.ousontmesaffaires.infrastructure.qrcode.QRGenerator;
import fr.bbougon.ousontmesaffaires.infrastructure.qrcode.QRGeneratorForTest;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.repositories.WithMemoryRepositories;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import fr.bbougon.ousontmesaffaires.web.helpers.URIBuilder;
import org.apache.commons.lang3.tuple.Pair;
import org.jboss.resteasy.spi.ResteasyUriInfo;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;


public class GenerateStickersCommandHandlerTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Rule
    public WithMemoryRepositories withMemoryRepositories = new WithMemoryRepositories();

    @Before
    public void before() {
        pdfGenerator = mock(PdfGenerator.class);
        qrCodeGenerator = new QRGeneratorForTest();
        codec = new Codec();
        location = Location.create("Location 1", Item.create(Lists.newArrayList(Feature.create("type", "chaussure"))));
        Repositories.locationRepository().persist(location);
        uriInfo = new ResteasyUriInfo(new URIBuilder().build("http://localhost/locations/"
                + codec.urlSafeToBase64(location.getId().toString())
                + "/stickers"));
    }

    @Test
    public void canGenerateASticker() {
        String locationId = new Codec().urlSafeToBase64(location.getId().toString());
        GenerateStickersCommandHandler generateStickersCommandHandler = new GenerateStickersCommandHandler(pdfGenerator, qrCodeGenerator);

        Pair<File, Object> result = generateStickersCommandHandler.execute(new GenerateStickersCommand(codec, uriInfo, locationId));

        assertThat(result).isNotNull();
        HashMap<String, String> content = Maps.newHashMap();
        content.put("title", location.getLocation());
        content.put("image", qrCodeGenerator.encodeToBase64(uriInfo.getBaseUri().toASCIIString() + "locations/" + locationId));
        verify(pdfGenerator).generate("Location_1.pdf", content);
    }

    @Test
    public void doNotGenerateAnyStickerIfLocationNotFound() {
        GenerateStickersCommandHandler commandHandler = new GenerateStickersCommandHandler(pdfGenerator, qrCodeGenerator);

        Pair<File, Object> result = commandHandler.execute(new GenerateStickersCommand(codec, uriInfo, new Codec().urlSafeToBase64(UUID.randomUUID().toString())));

        assertThat(result).isNotNull();
        assertThat(result.getLeft()).isNull();
        assertThat(result.getRight()).isNull();
        verifyZeroInteractions(pdfGenerator);
    }

    private PdfGenerator pdfGenerator;
    private ResteasyUriInfo uriInfo;
    private Codec codec;
    private QRGenerator qrCodeGenerator;
    private Location location;
}