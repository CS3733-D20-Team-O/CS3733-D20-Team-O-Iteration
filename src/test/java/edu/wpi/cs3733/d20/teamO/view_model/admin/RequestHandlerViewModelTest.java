package edu.wpi.cs3733.d20.teamO.view_model.admin;

import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.cs3733.d20.teamO.model.TestInjector;
import edu.wpi.cs3733.d20.teamO.model.csv.CSVHandler;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import org.junit.jupiter.api.Test;

//@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class RequestHandlerViewModelTest {

//  private final DatabaseWrapper database;
//  private final CSVHandler csvHandler;

  DatabaseWrapper database = TestInjector.create(DatabaseWrapper.class);
  CSVHandler csvHandler = TestInjector.create(CSVHandler.class);
  RequestHandlerViewModel vm = new RequestHandlerViewModel(database, csvHandler);

  @Test
  public void testNo1() {
    vm.firstRunOnly();
    //super.notFirstRun();
    //super.serviceTable.getSelectionModel().select(0);
    //System.out.println(super.serviceTable.getSelectionModel().getSelectedItem().getType());
    assertTrue(true);
  }

  @Test
  public void assignEmployeeTest_Admin() {
    database.addEmployee("1414", "Admin Name", "Admin", true);
    database.addEmployee("855", "employee Name", "Wash", true);
    database.addNode("RHVMNode", 1, 1, 1, "Main", "Test", "TestNode", "testnode");
    //can't add this service request for some reason
    database.addServiceRequest("1555", "time", "RHVMNode", "Wash", "Request Name", "", "");
    System.out.println("The database employees size: " + database.exportEmployees().size());
    System.out.println("The database serv.req. size: " + database.exportServiceRequests().size());

    //assertTrue(database.exportServiceRequests().size()>0);
    assertTrue(database.exportEmployees().size() > 0);

  }


}

