package edu.wpi.onyx_ouroboros.model.language.languages;

import edu.wpi.onyx_ouroboros.model.language.LanguageModel;

public class EnglishModel implements LanguageModel {

  @Override
  public String mainWelcome() {
    return "Welcome to Brigham and Women's Hospital!";
  }
}
