package fr.bbougon.ousontmesaffaires.domain.patch;

import com.google.gson.internal.LinkedTreeMap;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.domain.container.PatchedContainer;
import fr.bbougon.ousontmesaffaires.domain.container.image.Image;
import fr.bbougon.ousontmesaffaires.domain.container.image.ResizedImage;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ImagePatch extends PatchData<Image> implements Patch<Item> {

    public ImagePatch(final String id, final Object data) {
        super(id, data);
    }

    @Override
    public Image getData() {
        LinkedTreeMap map = (LinkedTreeMap) data;
        String signature = (String) map.get("signature");
        String url = (String) map.get("url");
        String secureUrl = (String) map.get("secure_url");
        List<ResizedImage> resizedImages = ((List<LinkedTreeMap>) map.get("resizedImages")).stream()
                .map(resizedImage -> {
                    final Double width = (Double) resizedImage.get("width");
                    final Double height = (Double) resizedImage.get("height");
                    return ResizedImage.create((String) resizedImage.get("url"),
                            (String) resizedImage.get("secure_url"), height, width);
                })
                .collect(Collectors.toList());
        return Image.create(signature, url, secureUrl, resizedImages);
    }

    @Override
    public PatchedContainer<Item> apply(final Supplier<Container> supplier) {
        return supplier.get().patchImage(getData(), getId());
    }
}
