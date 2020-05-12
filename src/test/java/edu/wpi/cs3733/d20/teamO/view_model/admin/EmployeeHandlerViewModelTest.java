package edu.wpi.cs3733.d20.teamO.view_model.admin;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.wpi.cs3733.d20.teamO.Main;
import edu.wpi.cs3733.d20.teamO.ResourceBundleMock;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.EmployeeProperty;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.Table;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Employee;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import edu.wpi.cs3733.d20.teamO.model.material.Validator;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
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
public class EmployeeHandlerViewModelTest extends FxRobot {

  @Mock
  EventBus eventbus;
  @Mock
  DatabaseWrapper database;
  @Mock
  SnackBar snackbar;
  @Mock
  Validator validator;
  @Mock
  Dialog dialog;
  @Mock
  AddEmployeeViewModel addEmployeeViewModel;
  @Mock
  UpdateEmployeeViewModel updateEmployeeViewModel;
  @InjectMocks
  EmployeeHandlerViewModel viewModel;

  @Spy
  private final ResourceBundleMock bundle = new ResourceBundleMock();

  @Start
  public void start(Stage stage) throws IOException {
    Employee e1 = new Employee("1414", "name", "Wash", "password", true);
    List<Employee> list = Arrays.asList(e1);
    when(database.exportEmployees()).thenReturn(list);
    val loader = new FXMLLoader();
    loader.setControllerFactory(o -> viewModel);
    loader.setResources(bundle);
    stage.setScene(new Scene(loader.load(Main.class
        .getResourceAsStream("views/admin/EmployeeHandler.fxml"))));
    stage.setAlwaysOnTop(true);
    stage.show();
  }

  @Test
  public void addEmployee() throws IOException {
    when(dialog.showFullscreenFXML(anyString())).thenReturn(addEmployeeViewModel);
    clickOn("Add");
    verify(dialog, times(1)).showFullscreenFXML(anyString());
  }

  @Test
  public void deleteEmployee() {
    clickOn("1414");
    clickOn("Delete");
    verify(database, times(1))
        .deleteFromTable(Table.EMPLOYEE_TABLE, EmployeeProperty.EMPLOYEE_ID, "1414");
    verify(database, times(3)).exportEmployees();
  }

  @Test
  public void deleteNoEmployee() {
    clickOn("Delete");
    verify(database, times(0))
        .deleteFromTable(Table.EMPLOYEE_TABLE, EmployeeProperty.EMPLOYEE_ID, "1414");
    verify(database, times(1)).exportEmployees();
    verify(snackbar, times(1)).show(anyString());
  }

  @Test
  public void updateNoEmployee() {
    clickOn("Edit");
    verify(database, times(0))
        .update(eq(Table.EMPLOYEE_TABLE), eq(EmployeeProperty.EMPLOYEE_ID), eq("1414"), any(),
            anyString());
    verify(database, times(1)).exportEmployees();
    verify(snackbar, times(1)).show(anyString());
  }

  @Test
  public void updateEmployee() throws IOException {
    when(dialog.showFullscreenFXML(anyString())).thenReturn(updateEmployeeViewModel);
    clickOn("1414");
    clickOn("Edit");
    verify(dialog, times(1)).showFullscreenFXML(anyString());
  }


}
