package edu.wpi.cs3733.d20.teamO.view_model.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import edu.wpi.cs3733.d20.teamO.Main;
import edu.wpi.cs3733.d20.teamO.model.csv.CSVHandler;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Employee;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.model.datatypes.ServiceRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
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
public class RequestHandlerViewModelTest extends FxRobot {

  //private final DatabaseWrapper database = TestInjector.create(DatabaseWrapper.class);
//  private final CSVHandler csvHandler;

  @Mock
  EventBus eventBus;
  @Mock
  DatabaseWrapper database;
  @Mock
  CSVHandler csvHandler;
  @InjectMocks
  RequestHandlerViewModel viewModel;

  @Spy
  private final ResourceBundle bundle = new ResourceBundle() {
    @Override
    protected Object handleGetObject(String s) {
      if (s.equals("serviceRequestLookupSubmit")) {
        return "Submit";
      }
      if (s.equals("serviceRequestConfirmationID")) {
        return "Click me";
      }
      if (s.equals("serviceRequestLookupFail")) {
        return "pass";
      }
      return "";
    }

    @Override
    public boolean containsKey(String key) {
      return true;
    }

    @Override
    public Enumeration<String> getKeys() {
      return Collections.emptyEnumeration();
    }
  };

  // Sets up the stage for testing
  @Start
  public void start(Stage stage) throws IOException {
    Employee e1 = new Employee("1414", "name", "Wash", true);
    Employee e2 = new Employee("855", "employee Name", "Wash", true);
    Employee e3 = new Employee("", "", "", true);
    Node n1 = new Node("RHVMNode", 1, 1, 1, "Main", "Test", "TestNode", "testnode");
    //can't add this service request for some reason
    List<Employee> list = new LinkedList<Employee>();
    list.add(e1);
    list.add(e2);
    list.add(e3);
    ServiceRequest req = new ServiceRequest("1555", "time", "RHVMNode", "Wash", "Request Name", "",
        "");
    ServiceRequest req2 = new ServiceRequest("576", "time", "RHVMNode", "Wash", "Request Name", "",
        "");
    List<ServiceRequest> reqs = new LinkedList<>();
    reqs.add(req);
    reqs.add(req2);
    when(database.exportServiceRequests()).thenReturn(reqs);
    when(database.exportEmployees()).thenReturn(list);

    val loader = new FXMLLoader();
    loader.setControllerFactory(o -> viewModel);
    //loader.setResources(bundle);
    stage.setScene(new Scene(loader.load(Main.class
        .getResourceAsStream("views/admin/RequestHandler.fxml"))));
    stage.setAlwaysOnTop(true);
    stage.show();
  }

//  @Test
//  public void testNo1() {
//    //database.addNode("", 0, 0, 0, "", "", "", "");
//    //database.addEmployee("", "", "", true);
//    //database.addServiceRequest("1", "0700", "", "Interpreter", "Bob", "", "");
//    //database.addEmployee("1", "Jo", "Interpreter", true);
//    viewModel.firstRunOnly();
//    viewModel.notFirstRun();
//    clickOn("Assign Employee");
//    viewModel.serviceTable.getItems().add(new ServiceRequest("1", "0700", "", "Interpreter", "Bob", "", ""));
//    viewModel.serviceTable.getSelectionModel().select(0);
//    System.out.println(viewModel.serviceTable.getSelectionModel().getSelectedItem().getType());
//    assertTrue(true);
//  }

  public RequestHandlerViewModelTest() {

  }

  @Test
  public void assignAnEmployeeValid() {
    clickOn("1555");
    clickOn("855");
    clickOn("Assign Employee");
    clickOn("1555");
    assertEquals(viewModel.serviceTable.getSelectionModel().getSelectedItem().getEmployeeAssigned(),
        "855");
  }

  @Test
  public void assignEmployeeInvalidAlreadyAssigned() {
    clickOn("1555");
    clickOn("855");
    clickOn("Assign Employee");

    clickOn("1555");
    clickOn("1414");
    clickOn("Assign Employee");
    assertEquals(viewModel.serviceTable.getSelectionModel().getSelectedItem().getEmployeeAssigned(),
        "855");

  }

  @Test
  public void assignEmployeeInvalidNoneAvail() {
    clickOn("1555");
    clickOn("855");
    clickOn("Assign Employee");

    clickOn("576");
    clickOn("Show Unavailable");
    clickOn("855");
    clickOn("Assign Employee");
    clickOn("576");
    assertEquals(viewModel.serviceTable.getSelectionModel().getSelectedItem().getEmployeeAssigned(),
        "");
  }

  @Test
  public void assignEmployeeTest_Admin() {
    database.addEmployee("1414", "Admin Name", "Admin", true);
    database.addEmployee("855", "employee Name", "Wash", true);
    database.addEmployee("", "", "", true);
    database.addNode("RHVMNode", 1, 1, 1, "Main", "Test", "TestNode", "testnode");
    //can't add this service request for some reason
    database.addServiceRequest("1555", "time", "RHVMNode", "Wash", "Request Name", "", "");
    System.out.println("The database employees size: " + database.exportEmployees().size());
    System.out.println("The database serv.req. size: " + database.exportServiceRequests().size());

    //assertTrue(database.exportServiceRequests().size()>0);
    assertTrue(database.exportEmployees().size() > 0);

  }


}

