package edu.wpi.onyx_ouroboros.model.language;

/**
 * The Spanish language model
 */
class SpanishModel implements LanguageModel {

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
