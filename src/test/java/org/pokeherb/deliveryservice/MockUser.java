package org.pokeherb.deliveryservice;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockUserSecurityContextFactory.class)
public @interface MockUser {
    String uuid() default "009f60a3-619f-4cc5-9577-390878c4856e";
    String username() default "testuser";
    String name() default "테스트사용자";
    String email() default "test@test.org";
    String[] roles() default "MASTER";
}