package fr.bbougon.ousontmesaffaires.web.ressources;

import fr.bbougon.ousontmesaffaires.command.container.ContainerPatchCommand;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandResponse;
import fr.bbougon.ousontmesaffaires.web.ressources.javax.ws.rs.PATCH;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.OK;

@Path("/containers")
public class ContainerPatchResource {

    @PATCH
    @Path("/{UUID}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response patchContainer(@PathParam("UUID") final String containerId, final String patch) {
        CommandResponse commandResponse = commandBus.send(new ContainerPatchCommand(containerId, patch));
        return Response.status(OK).entity(commandResponse.getResponse()).build();
    }

    @Inject
    CommandBus commandBus;
}
