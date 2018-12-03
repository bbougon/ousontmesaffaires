package fr.bbougon.ousontmesaffaires.infrastructure.nlp;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.infrastructure.NLPService;
import fr.bbougon.ousontmesaffaires.infrastructure.TechnicalError;
import fr.bbougon.ousontmesaffaires.repositories.FileRepositories;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public class RemoteNLPService implements NLPService {

    public RemoteNLPService(final ClientBuilder clientBuilder) {
        this.clientBuilder = clientBuilder;
    }

    @Override
    public List<NLPAnalysis> analyze(final List<Item> itemsToAnalyse) {
        List<NLPAnalysis> result = Lists.newArrayList();
        itemsToAnalyse.forEach(item -> {
            try (Response response = clientBuilder.build().target(FileRepositories.remoteServiceConfiguration().get().nlpUrl())
                    .path("/request")
                    .request()
                    .post(Entity.entity(new Gson().toJson(new TextToAnalyse(item.getItem())), MediaType.APPLICATION_JSON_TYPE))) {
                NLPAnalysis nlpAnalysis = NLPAnalysis.fromJsonString(response.readEntity(String.class));
                nlpAnalysis.itemHash = item.getItemHash();
                result.add(nlpAnalysis);
            } catch (IllegalStateException e) {
                throw new TechnicalError("REMOTE_SERVICE_ERROR");
            }
        });
        return result;
    }

    static class TextToAnalyse {
        TextToAnalyse(final String text) {
            this.text = text;
        }

        @SuppressWarnings("unused")
        private String text;
        @SuppressWarnings("unused")
        private Double minimumScore = 0.8D;
    }

    private final ClientBuilder clientBuilder;
}
