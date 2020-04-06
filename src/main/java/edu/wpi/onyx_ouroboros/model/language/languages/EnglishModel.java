package edu.wpi.onyx_ouroboros.model.language.languages;

import edu.wpi.onyx_ouroboros.model.language.LanguageModel;

/**
 * The English language model
 */
public class EnglishModel implements LanguageModel {

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
