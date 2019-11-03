package org.icc.mybatisextensions.plugins.annotation;


import org.icc.mybatisextensions.plugins.encryption.CryptogramType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Encryption {

  CryptogramType type() default CryptogramType.AES256;


}
