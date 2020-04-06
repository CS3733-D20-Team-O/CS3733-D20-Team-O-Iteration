package edu.wpi.onyx_ouroboros.model.language;

/**
 * The English language model
 */
class EnglishModel implements LanguageModel {

  @Override
  public String mainWelcome() {
    return "Welcome to Brigham and Women's Hospital!";
  }

  @Override
  public String serviceSelectionSubmitButton() {
    return "Submit";
  }

  @Override
  public String serviceSelectionCancelButton() {
    return "Cancel";
  }
}
