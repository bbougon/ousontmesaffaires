package fr.bbougon.ousontmesaffaires.domain.container;

import com.google.common.collect.Lists;
import fr.bbougon.ousontmesaffaires.domain.BusinessError;
import fr.bbougon.ousontmesaffaires.domain.container.image.Image;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ContainerTest {

    @Test
    public void handleExceptionWhenItemCannotBeFoundOnPatchImage() {
        try {
            Container container = Container.create("a container", Lists.newArrayList());
            container.patchImage(Image.create("signature", "url", "secure-url", Lists.newArrayList()), "abcd");
        } catch (BusinessError e) {
            assertThat(e.getCode()).isEqualTo("UNKNOWN_ITEM_TO_PATCH");
            assertThat(e.getTarget().get()).isEqualTo("a container");
        }
    }
}