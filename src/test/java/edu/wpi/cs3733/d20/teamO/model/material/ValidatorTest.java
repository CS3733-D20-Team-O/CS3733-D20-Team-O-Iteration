package edu.wpi.cs3733.d20.teamO.model.material;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith({MockitoExtension.class, ApplicationExtension.class})
public class ValidatorTest {

  @InjectMocks
  Validator validator;

  private JFXTextField[] validFields, invalidFields;

  @Start
  public void start(Stage stage) {
    // Test where 2 out of 3 fields are invalid
    invalidFields = new JFXTextField[]{new JFXTextField(), new JFXTextField(), new JFXTextField()};
    invalidFields[0].setValidators(new RequiredFieldValidator());
    invalidFields[1].setValidators(new RequiredFieldValidator());
    invalidFields[2].requestFocus();
    val invalidRoot = new VBox(invalidFields);

    // Test where all fields are valid
    validFields = new JFXTextField[]{new JFXTextField(), new JFXTextField(), new JFXTextField()};
    val validRoot = new VBox(validFields);

    // Setup the stage for the tests
    stage.setScene(new Scene(new HBox(invalidRoot, validRoot)));
    stage.setAlwaysOnTop(true);
    stage.show();
  }

  @Test
  public void testInvalidFields() {
    assertFalse(validator.validate(invalidFields));
    verifyThat(invalidFields[0], Node::isFocused);
    verifyThat(invalidFields[1], node -> !node.isFocused());
    verifyThat(invalidFields[2], node -> !node.isFocused());
  }

  @Test
  public void testValidFields() {
    assertTrue(validator.validate(validFields));
    for (val field : validFields) {
      verifyThat(field, node -> !node.isFocused());
    }
  }
}
