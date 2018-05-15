package fr.bbougon.ousontmesaffaires.repositories.mongo.mapping;

import fr.bbougon.ousontmesaffaires.domain.container.image.Image;
import org.mongolink.domain.mapper.ComponentMap;

@SuppressWarnings("UnusedDeclaration")
public class ImageMapping extends ComponentMap<Image> {
    @Override
    public void map() {
        property().onProperty(Image::getSignature);
        property().onProperty(Image::getUrl);
        property().onProperty(Image::getSecureUrl);
        collection().onProperty(Image::getResizedImages);
    }
}
