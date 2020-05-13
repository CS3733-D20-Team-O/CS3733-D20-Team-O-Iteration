package edu.wpi.cs3733.d20.teamO.view_model.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import edu.wpi.cs3733.d20.teamO.Main;
import edu.wpi.cs3733.d20.teamO.ResourceBundleMock;
import edu.wpi.cs3733.d20.teamO.model.data.CSVHandler;
import edu.wpi.cs3733.d20.teamO.model.data.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Employee;
import edu.wpi.cs3733.d20.teamO.model.datatypes.LoginDetails;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.model.datatypes.ServiceRequest;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.SanitationRequestData;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
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
public class RequestHandlerViewModelTest extends FxRobot {

  //private final DatabaseWrapper database = TestInjector.create(DatabaseWrapper.class);
//  private final CSVHandler csvHandler;

  @Mock
  EventBus eventBus;
  @Mock
  DatabaseWrapper database;
  @Mock
  CSVHandler csvHandler;
  @Mock
  LoginDetails loginDetails;
  @Mock
  SnackBar snackBar;

  @InjectMocks
  RequestHandlerViewModel viewModel;

  @Spy
  private final ResourceBundleMock bundle = new ResourceBundleMock();

  private void initializeBundle() {
    bundle.put("requestHandlerService", "Service");
    bundle.put("requestHandlerReqTime", "Request Time");
    bundle.put("requestHandlerReqNode", "Request Node");
    bundle.put("requestHandlerReqName", "Requester Name");
    bundle.put("requestHandlerEmployee", "Employee");
    bundle.put("requestHandlerAssigner", "Assigner");
    bundle.put("requestHandlerReqID", "Request ID");
    bundle.put("requestHandlerStatus", "Status");
    bundle.put("requestHandlerAvailable", "Available");
    bundle.put("requestHandlerName", "Name");
    bundle.put("requestHandlerShowAvail", "Show Unavailable");
    bundle.put("requestHandlerInfo", "Select a request to view detailed information");
    bundle.put("requestHandlerAssignEmp", "Assign Employee");
    bundle.put("requestHandlerResolve", "Resolve Request");
    bundle.put("requestHandlerCancel", "Cancel Request");
    bundle.put("requestHandlerStep1", "Step 1. Select a Service Request from the  Service Request table.");
    bundle.put("requestHandlerStep2", "Step 2. Select an available employee to be assigned to the request from the Employee table.");
    bundle.put("requestHandlerStep3", "Step 3. Click the Assign button to assign that  employee to the request. They won't be available  for other requests until it is completed.");
  }

  // Sets up the stage for testing
  @Start
  public void start(Stage stage) throws IOException {
    bundle.put("Sample", "Sample"); // todo load the necessary strings
    initializeBundle();
    Employee e1 = new Employee("1414", "name", "Wash", "password", true);
    Employee e2 = new Employee("855", "employee Name", "Wash", "password", true);
    Employee e3 = new Employee("", "", "", "password", true);
    Node n1 = new Node("RHVMNode", 1, 1, "1", "Main", "Test", "TestNode", "testnode");
    //can't add this service request for some reason
    List<Employee> list = Arrays.asList(e1, e2, e3);

    ServiceRequest req = new ServiceRequest("1555", "time", "RHVMNode", "Wash", "Unassigned",
        "Request Name", "0",
        "0", new SanitationRequestData("test", "test1"));
    ServiceRequest req2 = new ServiceRequest("576", "time", "RHVMNode", "Wash", "Unassigned",
        "Request Name", "0",
        "0", new SanitationRequestData("test,", "test2"));
    List<ServiceRequest> reqs = Arrays.asList(req, req2);
    when(database.exportServiceRequests()).thenReturn(reqs);
    when(database.exportEmployees()).thenReturn(list);
    val loader = new FXMLLoader();
    loader.setResources(bundle);
    loader.setControllerFactory(o -> viewModel);
    stage.setScene(new Scene(loader.load(Main.class
        .getResourceAsStream("views/admin/RequestHandler.fxml"))));
    stage.setAlwaysOnTop(true);
    stage.show();
  }

  @Test
  public void assignAnEmployeeValid() {
    when(loginDetails.getUsername()).thenReturn("staff");
    clickOn("1555");
    clickOn("855");
    clickOn("Assign Employee");
    clickOn("1555");
    assertEquals(viewModel.getSelectedRequest().getEmployeeAssigned(),
        "855");
  }

  @Test
  public void assignEmployeeInvalidAlreadyAssigned() {
    when(loginDetails.getUsername()).thenReturn("staff");
    clickOn("1555");
    clickOn("855");
    clickOn("Assign Employee");

    clickOn("1555");
    clickOn("1414");
    clickOn("Assign Employee");
    assertEquals(viewModel.getSelectedRequest().getEmployeeAssigned(),
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
    assertEquals(viewModel.getSelectedRequest().getEmployeeAssigned(),
        "0");
  }

  @Test
  public void assignEmployeeToACancelledRequest() {
    clickOn("1555");
    clickOn("Cancel Request");
    clickOn("1555");
    clickOn("855");
    clickOn("Assign Employee");

    clickOn("1555");
    assertEquals(viewModel.getSelectedRequest().getEmployeeAssigned(), "0");
  }

  @Test
  public void assignEmployeeToAResolvedRequest() {
    clickOn("1555");
    clickOn("Resolve Request");
    clickOn("1555");
    clickOn("855");
    clickOn("Assign Employee");

    clickOn("1555");
    assertEquals(viewModel.getSelectedRequest().getEmployeeAssigned(), "0");
  }

  @Test
  public void reassignEmployeeAfterResolve() {
    when(loginDetails.getUsername()).thenReturn("staff");

    clickOn("1555");
    clickOn("855");
    clickOn("Assign Employee");

    clickOn("1555");
    clickOn("Resolve Request");
    clickOn("1414");
    clickOn("855");
    clickOn("Assign Employee");

    clickOn("1414");
    assertEquals(viewModel.getSelectedRequest().getEmployeeAssigned(), "855");
  }

  @Test
  public void reassignEmployeeAfterCancel() {
    when(loginDetails.getUsername()).thenReturn("staff");

    clickOn("1555");
    clickOn("855");
    clickOn("Assign Employee");

    clickOn("1555");
    clickOn("Cancel Request");
    clickOn("1414");
    clickOn("855");
    clickOn("Assign Employee");

    clickOn("1414");
    assertEquals(viewModel.getSelectedRequest().getEmployeeAssigned(), "855");
  }

  @Test
  public void employeePersistenceInCancel() {
    when(loginDetails.getUsername()).thenReturn("staff");

    clickOn("1555");
    clickOn("855");
    clickOn("Assign Employee");

    clickOn("1555");
    clickOn("Cancel Request");

    clickOn("1555");
    assertEquals(viewModel.getSelectedRequest().getEmployeeAssigned(), "855");
  }

  @Test
  public void employeePersistenceInResolve() {
    when(loginDetails.getUsername()).thenReturn("staff");

    clickOn("1555");
    clickOn("855");
    clickOn("Assign Employee");

    clickOn("1555");
    clickOn("Resolve Request");

    clickOn("1555");
    assertEquals(viewModel.getSelectedRequest().getEmployeeAssigned(), "855");
  }
}

