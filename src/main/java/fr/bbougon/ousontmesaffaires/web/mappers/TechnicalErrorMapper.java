package fr.bbougon.ousontmesaffaires.web.mappers;

import com.google.common.collect.Maps;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import fr.bbougon.ousontmesaffaires.infrastructure.TechnicalError;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Map;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

@Provider
public class TechnicalErrorMapper implements ExceptionMapper<TechnicalError> {

    private Map<String, TechnicalErrorTranslation> errors = Maps.newHashMap();

    {
        errors.put("REMOTE_SERVICE_ERROR", new TechnicalErrorTranslation(INTERNAL_SERVER_ERROR, "An error occurred while reaching a remote service."));
    }

    @Override
    public Response toResponse(final TechnicalError technicalError) {
        TechnicalErrorTranslation technicalErrorTranslation = errors.get(technicalError.getCode());
        return Response.status(technicalErrorTranslation.status)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(toJson(technicalErrorTranslation))
                .build();
    }

    private String toJson(final TechnicalErrorTranslation technicalErrorTranslation) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("error", technicalErrorTranslation.message);
        return new GsonBuilder()
                .disableHtmlEscaping()
                .create()
                .toJson(jsonObject);
    }

    private class TechnicalErrorTranslation {
        TechnicalErrorTranslation(final Status status, final String message) {
            this.status = status;
            this.message = message;
        }

        private final Status status;
        private final String message;
    }
}
