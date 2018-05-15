package fr.bbougon.ousontmesaffaires.command;

import com.google.gson.internal.LinkedTreeMap;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.image.Image;
import fr.bbougon.ousontmesaffaires.domain.container.image.ResizedImage;
import fr.bbougon.ousontmesaffaires.infrastructure.security.SecurityService;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ItemStrategy implements Strategy {
    @Override
    public void apply(final Patch patch, final Supplier supplier) {
        Container container = (Container) supplier.get();
        LinkedTreeMap map = (LinkedTreeMap) patch.getData();
        String signature = (String) map.get("signature");
        String url = (String) map.get("url");
        String secureUrl = (String) map.get("secure_url");
        List<LinkedTreeMap> resizedImages = (List<LinkedTreeMap>) map.get("resizedImages");
        Image image = Image.create(signature, url, secureUrl, resizedImages
                .stream()
                .map(resizedImage -> {
                    final Double width = (Double) resizedImage.get("width");
                    final Double height = (Double) resizedImage.get("height");
                    return ResizedImage.create((String) resizedImage.get("url"),
                            (String) resizedImage.get("secure_url"), height, width);
                })
                .collect(Collectors.toList()));

        container.getItems()
                .stream()
                .filter(item -> SecurityService.sha1().encrypt(item.toString().getBytes()).equals(patch.getId()))
                .findFirst()
                .ifPresent(foundItem -> foundItem.add(image));
    }
}
