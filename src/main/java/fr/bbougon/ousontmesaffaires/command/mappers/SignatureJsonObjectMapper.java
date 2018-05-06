package fr.bbougon.ousontmesaffaires.command.mappers;

import com.google.gson.JsonObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.function.Function;

public class SignatureJsonObjectMapper implements JsonMapper<String, JsonObject, String> {

    public JsonObject map(final String signature, String apiKey) {
        JsonObject sign = new JsonObject();
        sign.addProperty("apiKey", apiKey);
        sign.addProperty("signature", signature);
        return sign;
    }

    @Override
    public JsonObject map(final List<String> objects, final Function<String, String> function) {
        throw new NotImplementedException();
    }

}
