package edu.wpi.onyx_ouroboros.model.language;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to annotate fields that they contain text that should change based on the current language
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Language {

  /**
   * @return the ID in LanguageModel that the field should display
   */
  String ID() default "";
}
