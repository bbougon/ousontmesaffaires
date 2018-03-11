package fr.bbougon.ousontmesaffaires.command.location;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fr.bbougon.ousontmesaffaires.command.sticker.GenerateStickersCommand;
import fr.bbougon.ousontmesaffaires.command.sticker.GenerateStickersCommandHandler;
import fr.bbougon.ousontmesaffaires.domain.location.Feature;
import fr.bbougon.ousontmesaffaires.domain.location.Item;
import fr.bbougon.ousontmesaffaires.domain.location.Location;
import fr.bbougon.ousontmesaffaires.infrastructure.pdf.PdfGenerator;
import fr.bbougon.ousontmesaffaires.infrastructure.pdf.StickerContent;
import fr.bbougon.ousontmesaffaires.infrastructure.qrcode.QRGenerator;
import fr.bbougon.ousontmesaffaires.infrastructure.qrcode.QRGeneratorForTest;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.repositories.WithMemoryRepositories;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import fr.bbougon.ousontmesaffaires.command.sticker.Sticker;
import fr.bbougon.ousontmesaffaires.web.ressources.UriInfoBuilderForTest;
import org.apache.commons.lang3.tuple.Pair;
import org.jboss.resteasy.spi.ResteasyUriInfo;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.UUID;

import static fr.bbougon.ousontmesaffaires.infrastructure.pdf.StickerContent.IMAGE;
import static fr.bbougon.ousontmesaffaires.infrastructure.pdf.StickerContent.TITLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


public class GenerateStickersCommandHandlerTest {

    @Rule
    public WithMemoryRepositories withMemoryRepositories = new WithMemoryRepositories();

    @Before
    public void before() {
        pdfGenerator = mock(PdfGenerator.class);
        qrCodeGenerator = new QRGeneratorForTest();
        codec = new Codec();
        location = Location.create("Location 1", Item.create(Lists.newArrayList(Feature.create("type", "chaussure"))));
        Repositories.locationRepository().persist(location);
        uriInfo = new UriInfoBuilderForTest().forStickers(codec.urlSafeToBase64(location.getId().toString()));
    }

    @Test
    public void canGenerateASticker() {
        String locationId = codec.urlSafeToBase64(location.getId().toString());
        GenerateStickersCommandHandler generateStickersCommandHandler = new GenerateStickersCommandHandler(pdfGenerator, qrCodeGenerator);

        Pair<Sticker, Object> result = generateStickersCommandHandler.execute(new GenerateStickersCommand(uriInfo, locationId));

        assertThat(result).isNotNull();
        HashMap<StickerContent, String> content = Maps.newHashMap();
        content.put(TITLE, location.getLocation());
        content.put(IMAGE, qrCodeGenerator.encodeToBase64(uriInfo.getBaseUri().toASCIIString() + "locations/" + locationId));
        verify(pdfGenerator).generate(new Sticker("Location_1.pdf"), content);
    }

    @Test
    public void doNotGenerateAnyStickerIfLocationNotFound() {
        GenerateStickersCommandHandler commandHandler = new GenerateStickersCommandHandler(pdfGenerator, qrCodeGenerator);

        Pair<Sticker, Object> result = commandHandler.execute(new GenerateStickersCommand(uriInfo, new Codec().urlSafeToBase64(UUID.randomUUID().toString())));

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