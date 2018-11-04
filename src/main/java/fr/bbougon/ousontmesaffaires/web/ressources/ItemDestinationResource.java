package fr.bbougon.ousontmesaffaires.web.ressources;

import com.google.gson.Gson;
import fr.bbougon.ousontmesaffaires.command.Destination;
import fr.bbougon.ousontmesaffaires.command.container.ItemDestinationCommand;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandResponse;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/containers")
public class ItemDestinationResource {

    @POST
    @Path("/{UUID}/items/{itemHash}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response destination(@PathParam("UUID") final String containerId, @PathParam("itemHash") final String itemHash, final String payload) {
        ContainerAddResource.checkPayload(payload);
        ItemDestinationCommand command = new ItemDestinationCommand(containerId, itemHash, new Gson().fromJson(payload, Destination.class));
        CommandResponse commandResponse = commandBus.send(command);
        return Response.ok(commandResponse.getResponse()).build();
    }

    @Inject
    CommandBus commandBus;
}
