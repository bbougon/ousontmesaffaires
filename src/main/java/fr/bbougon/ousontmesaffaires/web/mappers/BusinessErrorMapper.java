package fr.bbougon.ousontmesaffaires.web.mappers;

import com.google.common.collect.Maps;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import fr.bbougon.ousontmesaffaires.domain.BusinessError;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Map;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Provider
public class BusinessErrorMapper implements ExceptionMapper<BusinessError> {

    private Map<String, BusinessErrorTranslation> errors = Maps.newHashMap();

    {
        errors.put("UNEXISTING_CONTAINER", new BusinessErrorTranslation(NOT_FOUND, "The container you are requesting does not exist."));
        errors.put("UNEXISTING_DESTINATION_CONTAINER", new BusinessErrorTranslation(NOT_FOUND, "The destination container you are requesting does not exist."));
        errors.put("UNKNOWN_PATCH", new BusinessErrorTranslation(NOT_FOUND, "An error occurred during patch, current target '%s' is unknown."));
        errors.put("UNKNOWN_ITEM_TO_PATCH", new BusinessErrorTranslation(NOT_FOUND, "The item you are trying to patch on container '%s' is unknown."));
        errors.put("UNKNOWN_ITEM_TO_MOVE", new BusinessErrorTranslation(NOT_FOUND, "The item you are trying to move to container '%s' is unknown."));
        errors.put("UNKNOWN_EXTRACTED_ITEM", new BusinessErrorTranslation(NOT_FOUND, "The item you are requesting does not exist."));
        errors.put("UNKNOWN_ITEM_TO_ANALYSE", new BusinessErrorTranslation(NOT_FOUND, "The item you are trying to analyse in container '%s' is unknown."));
    }

    @Override
    public Response toResponse(final BusinessError exception) {
        BusinessErrorTranslation businessErrorTranslation = errors.get(exception.getCode());
        return Response
                .status(businessErrorTranslation.status)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(toJson(businessErrorTranslation, exception))
                .build();
    }

    private String toJson(final BusinessErrorTranslation businessErrorTranslation, final BusinessError exception) {
        JsonObject jsonObject = new JsonObject();
        String target = exception.getTarget().orElse("");
        String message = String.format(businessErrorTranslation.translatedMessage, target);
        jsonObject.addProperty("error", message);
        return new GsonBuilder()
                .disableHtmlEscaping()
                .create()
                .toJson(jsonObject);
    }

    private class BusinessErrorTranslation {
        BusinessErrorTranslation(final Response.Status status, final String translatedMessage) {
            this.status = status;
            this.translatedMessage = translatedMessage;
        }

        private final Response.Status status;
        private final String translatedMessage;
    }
}
