package fr.bbougon.ousontmesaffaires.infrastructure.module.mongolink;

import java.lang.annotation.*;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface MongolinkTransaction {
}
