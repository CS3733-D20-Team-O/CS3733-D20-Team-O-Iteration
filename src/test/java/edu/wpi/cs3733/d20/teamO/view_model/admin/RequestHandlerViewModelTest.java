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

}

