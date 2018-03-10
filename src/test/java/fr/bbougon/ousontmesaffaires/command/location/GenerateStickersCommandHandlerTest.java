package fr.bbougon.ousontmesaffaires.command.location;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fr.bbougon.ousontmesaffaires.domain.location.Feature;
import fr.bbougon.ousontmesaffaires.domain.location.Item;
import fr.bbougon.ousontmesaffaires.domain.location.Location;
import fr.bbougon.ousontmesaffaires.infrastructure.pdf.PdfGenerator;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import fr.bbougon.ousontmesaffaires.web.helpers.URIBuilder;
import org.apache.commons.lang3.tuple.Pair;
import org.jboss.resteasy.spi.ResteasyUriInfo;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import javax.ws.rs.core.UriInfo;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class GenerateStickersCommandHandlerTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void before() throws IOException {
        file = temporaryFolder.newFile();
        pdfGenerator = mock(PdfGenerator.class);
        uriInfo = new ResteasyUriInfo(new URIBuilder().build("http://locahost/uuid/stickers"));
        codec = new Codec();
    }

    @Ignore
    @Test
    public void canGenerateASticker() throws IOException {
        Location location = Location.create("Location 1", Item.create(Lists.newArrayList(Feature.create("type", "chaussure"))));
        Repositories.locationRepository().persist(location);
        String locationId = new Codec().urlSafeToBase64(location.getId().toString());
        GenerateStickersCommandHandler generateStickersCommandHandler = new GenerateStickersCommandHandler(pdfGenerator);

        Pair<File, Object> result = generateStickersCommandHandler.execute(new GenerateStickersCommand(codec, uriInfo, locationId));

        assertThat(result).isNotNull();
        HashMap<String, String> content = Maps.newHashMap();
        content.put("title", location.getLocation());
        content.put("image", codec.urlSafeToBase64(uriInfo.getAbsolutePath().toASCIIString()));
        verify(pdfGenerator).generate("Location_1", content);
    }

    private File file;
    private PdfGenerator pdfGenerator;
    private ResteasyUriInfo uriInfo;
    private Codec codec;
}