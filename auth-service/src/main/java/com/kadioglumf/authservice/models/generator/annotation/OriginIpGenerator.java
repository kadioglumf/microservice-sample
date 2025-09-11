package com.kadioglumf.authservice.models.generator.annotation;

import com.kadioglumf.authservice.models.generator.OriginIpGeneratorImpl;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.hibernate.annotations.ValueGenerationType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@ValueGenerationType(generatedBy = OriginIpGeneratorImpl.class)
public @interface OriginIpGenerator {}
