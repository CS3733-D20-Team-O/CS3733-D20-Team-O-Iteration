package edu.wpi.onyx_ouroboros.model.language.languages;

import edu.wpi.onyx_ouroboros.model.language.LanguageModel;

/**
 * The Spanish language model
 */
public class SpanishModel implements LanguageModel {
  // todo fill all of these fields

  @Override
  public String mainWelcome() {
    return "Some stuff in Spanish lmao";
  }

  @Override
  public String serviceSelectionSubmitButton() {
    return "";
  }

  @Override
  public String serviceSelectionCancelButton() {
    return "";
  }
}
