package com.kadioglumf.dataservice.model.generator.annotation;

import com.kadioglumf.dataservice.model.generator.LoggedUserIdGeneratorImpl;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.hibernate.annotations.ValueGenerationType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@ValueGenerationType(generatedBy = LoggedUserIdGeneratorImpl.class)
public @interface LoggedUserIdGenerator {}
