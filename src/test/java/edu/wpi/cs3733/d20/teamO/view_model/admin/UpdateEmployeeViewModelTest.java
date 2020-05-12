package edu.wpi.cs3733.d20.teamO.view_model.admin;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.wpi.cs3733.d20.teamO.Main;
import edu.wpi.cs3733.d20.teamO.ResourceBundleMock;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.model.material.Validator;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.val;
import org.greenrobot.eventbus.EventBus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith({MockitoExtension.class, ApplicationExtension.class})
public class UpdateEmployeeViewModelTest extends FxRobot {

  @Mock
  EventBus eventbus;
  @Mock
  DatabaseWrapper database;
  @Mock
  SnackBar snackbar;
  @Mock
  Validator validator;
  @InjectMocks
  UpdateEmployeeViewModel viewModel;
  @Mock
  EmployeeHandlerViewModel empViewModel;
  @Mock
  Dialog dialog;
  @Spy
  private final ResourceBundleMock bundle = new ResourceBundleMock();

  @Start
  public void start(Stage stage) throws IOException {
    val loader = new FXMLLoader();
    loader.setControllerFactory(o -> viewModel);
    loader.setResources(bundle);
    stage.setScene(new Scene(loader.load(Main.class
        .getResourceAsStream("views/admin/UpdateEmployee.fxml"))));
    stage.setAlwaysOnTop(true);
    stage.show();
  }

  @Test
  public void updateEmployee() {
    when(validator.validate(any())).thenReturn(true);
    clickOn("Full Name");
    write("Steve");
    clickOn("Employee Type");
    clickOn("Admin");
    clickOn("Employee ID");
    write("stevec");
    clickOn("Confirm");
    verify(database, times(3)).update(any(), any(), any(), any(), any());
    verify(snackbar, times(1)).show(anyString());
  }

  @Test
  public void updateNoEmployee() {
    clickOn("Confirm");
    verify(validator, times(1)).validate(any());
    verify(database, times(0)).update(any(), any(), any(), any(), any());
    verify(snackbar, times(0)).show(anyString());
  }
}
