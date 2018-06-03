package fr.bbougon.ousontmesaffaires.command.mappers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.bbougon.ousontmesaffaires.infrastructure.security.Signature;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public class SignatureJsonMapper implements JsonMapper<Signature> {

    public JsonElement map(final Signature signature) {
        JsonObject sign = new JsonObject();
        sign.addProperty("apiKey", signature.getApiKey());
        sign.addProperty("signature", signature.getSignature());
        return sign;
    }

    @Override
    public JsonElement map(final List<Signature> objects) {
        throw new NotImplementedException();
    }

}
