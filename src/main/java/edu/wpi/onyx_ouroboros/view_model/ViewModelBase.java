package edu.wpi.onyx_ouroboros.view_model;

import javafx.fxml.Initializable;

public abstract class ViewModelBase implements Initializable {
/*
todo
Handles language switching via subscribing to sticky posted lang events in constructor, holding onto curr lang, and using reflection to annotate fields to get their appropriate strings?
Get all fields via this.class.getFields()
For each field, call getAnnotation(Language.class)
  If non-null, call setText(LanguageModel.getClass().getMethod(value).invoke(null);
  @MustCallSuper FOR CONSTRUCTOR
 */
}
