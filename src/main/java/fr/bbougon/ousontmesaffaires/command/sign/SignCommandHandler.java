package fr.bbougon.ousontmesaffaires.command.sign;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.infrastructure.security.SecurityService;
import fr.bbougon.ousontmesaffaires.repositories.FileRepositories;
import org.apache.commons.lang3.tuple.Pair;

import java.util.stream.Collectors;

public class SignCommandHandler implements CommandHandler<SignCommand, String> {

    @Override
    public Pair<String, Object> execute(final SignCommand signCommand) {
        String secret = FileRepositories.securityConfiguration().get().securitySetting().thirdPartServiceSecret();
        JsonObject jsonObject = new JsonParser().parse(signCommand.getData()).getAsJsonObject();
        String dataToEncrypt = jsonObject
                .keySet()
                .stream()
                .map(key -> String.format("%s=%s", key, jsonObject.get(key).getAsString()))
                .collect(Collectors.joining("&"))
                .concat(secret);
        return Pair.of(SecurityService.sha1().encrypt(dataToEncrypt.getBytes()), signCommand);
    }

}
