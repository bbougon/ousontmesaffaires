package fr.bbougon.ousontmesaffaires.web.ressources;

import fr.bbougon.ousontmesaffaires.command.sign.SignCommand;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandResponse;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

public class SignResource {

    public Response sign(final String data) {
        CommandResponse commandResponse = commandBus.send(new SignCommand(data));
        return Response.ok(commandResponse.getResponse()).build();
    }

    @Inject
    CommandBus commandBus;
}
