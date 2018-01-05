package fr.bbougon.ousontmesaffaires.test.utils;

import com.google.common.io.Resources;

import java.io.IOException;
import java.nio.charset.Charset;

public class JsonFileUtils {

    public JsonFileUtils(final String resourceFilePath) {
        this.resourceFilePath = resourceFilePath;
    }

    public String getPayload() {
        try {
            return Resources.toString(Resources.getResource(resourceFilePath), Charset.defaultCharset());
        } catch (IOException e) {
            return null;
        }
    }

    private final String resourceFilePath;
}
