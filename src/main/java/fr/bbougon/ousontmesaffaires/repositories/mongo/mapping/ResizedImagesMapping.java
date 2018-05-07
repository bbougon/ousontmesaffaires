package fr.bbougon.ousontmesaffaires.repositories.mongo.mapping;

import fr.bbougon.ousontmesaffaires.domain.container.ResizedImage;
import org.mongolink.domain.mapper.ComponentMap;

@SuppressWarnings("UnusedDeclaration")
public class ResizedImagesMapping extends ComponentMap<ResizedImage> {
    
    @Override
    public void map() {
        property().onProperty(ResizedImage::getUrl);
        property().onProperty(ResizedImage::getSecureUrl);
        property().onProperty(ResizedImage::getHeight);
        property().onProperty(ResizedImage::getWidth);
    }
}
