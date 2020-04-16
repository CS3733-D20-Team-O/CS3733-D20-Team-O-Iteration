package edu.wpi.cs3733.d20.teamO.model.language;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to annotate fields that they contain text that should change based on the current language
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Language {

  /**
   * @return the key for the ResourceBundle that fuels what the field should display
   */
  String key() default "";
}
