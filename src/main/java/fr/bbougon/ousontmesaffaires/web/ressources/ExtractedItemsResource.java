package fr.bbougon.ousontmesaffaires.web.ressources;

import fr.bbougon.ousontmesaffaires.command.extracteditem.ExtractedItemAddItemCommand;
import fr.bbougon.ousontmesaffaires.command.extracteditem.ExtractedItemGetAllCommand;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandResponse;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import fr.bbougon.ousontmesaffaires.web.helpers.URIBuilder;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Path(ExtractedItemsResource.PATH)
public class ExtractedItemsResource {

    @POST
    @Consumes(APPLICATION_JSON)
    public Response addItem(final String payload) {
        CommandResponse commandResponse = commandBus.send(new ExtractedItemAddItemCommand(payload));
        if(commandResponse.isEmpty()) {
            return Response.status(NOT_FOUND).build();
        }
        URI uri = new URIBuilder().build(PATH + "/" + new Codec().urlSafeToBase64(commandResponse.getResponse().toString()));
        return Response.created(uri).build();
    }

    @GET
    @Produces(APPLICATION_JSON)
    public Response getAll() {
        CommandResponse commandResponse = commandBus.send(new ExtractedItemGetAllCommand());
        return Response.ok(commandResponse.getResponse()).build();
    }

    @Inject
    CommandBus commandBus;
    static final String PATH = "/extracted-items";
}
