package fr.bbougon.ousontmesaffaires.web.ressources;

import fr.bbougon.ousontmesaffaires.command.container.ContainerGetCommand;
import fr.bbougon.ousontmesaffaires.command.container.ContainersGetCommand;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandResponse;
import fr.bbougon.ousontmesaffaires.infrastructure.module.mongolink.MongolinkTransaction;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.OK;

@Path("/containers")
public class ContainerResource {

    @MongolinkTransaction
    @GET
    public Response getAll() {
        CommandResponse commandResponse = commandBus.send(new ContainersGetCommand());
        return Response.ok().entity(commandResponse.getResponse()).build();
    }

    @MongolinkTransaction
    @GET
    @Path("/{UUID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getContainer(@PathParam("UUID") final String containerId) {
        CommandResponse commandResponse = commandBus.send(new ContainerGetCommand(containerId));
        return Response.status(OK).entity(commandResponse.getResponse()).build();
    }

    @Inject
    CommandBus commandBus;
}
