package fr.bbougon.ousontmesaffaires.web.ressources;

import fr.bbougon.ousontmesaffaires.command.extracteditem.ExtractedItemAddItemCommand;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandResponse;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import fr.bbougon.ousontmesaffaires.web.helpers.URIBuilder;

import javax.ws.rs.core.Response;
import java.net.URI;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

public class ExtractedItemsResource {

    public Response addItem(final String containerId, final String itemHash) {
        CommandResponse commandResponse = commandBus.send(new ExtractedItemAddItemCommand(containerId, itemHash));
        if(commandResponse.isEmpty()) {
            return Response.status(NOT_FOUND).build();
        }
        URI uri = new URIBuilder().build(PATH + "/" + new Codec().urlSafeToBase64(commandResponse.getResponse().toString()));
        return Response.created(uri).build();
    }

    CommandBus commandBus;
    private static final String PATH = "/extracted-items";
}
