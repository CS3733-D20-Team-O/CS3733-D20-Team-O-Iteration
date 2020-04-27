package edu.wpi.cs3733.d20.teamO.view_model.kiosk.service_requests;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import edu.wpi.cs3733.d20.teamO.Main;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.datatypes.Node;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.InternalTransportationRequestData;
import edu.wpi.cs3733.d20.teamO.model.datatypes.requests_data.ServiceRequestData;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.val;
import org.greenrobot.eventbus.EventBus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith({MockitoExtension.class, ApplicationExtension.class})
public class InternalTransportationTest extends FxRobot {

  @Mock
  EventBus eventBus;
  @Mock
  DatabaseWrapper database;
  @InjectMocks
  InternalTransportationService ITS;

  @Start
  public void start(Stage stage) throws IOException {
    Node n1 = new Node("RHVMNode", 1, 1, 1, "Main", "Test", "TestNode", "testnode");
    LinkedList<Node> nodesList = new LinkedList<Node>();
    Map<String, Node> nodeMap = new HashMap<String, Node>();
    nodeMap.put("RHVMNode", n1);
    when(database.exportNodes()).thenReturn(nodeMap);
    when(database.addServiceRequest(anyString(), anyString(), anyString(), anyString(), any(
        ServiceRequestData.class))).thenAnswer(new Answer() {
      public Object answer(InvocationOnMock invocation) {
        Object[] args = invocation.getArguments();
        Object mock = invocation.getMock();
        System.out.println((String) args[0]);
        System.out.println((String) args[1]);
        System.out.println((String) args[2]);
        System.out.println((String) args[3]);
        System.out.println(((InternalTransportationRequestData) args[4]).getDisplayable());

        return "called with arguments: " + args;
      }
    });
    val loader = new FXMLLoader();
    loader.setControllerFactory(o -> ITS);
    //loader.setResources(bundle);
    stage.setScene(new Scene(loader.load(Main.class
        .getResourceAsStream("views/kiosk/service_requests/InternalTransportationService.fxml"))));
    stage.setAlwaysOnTop(true);
    stage.show();
  }

  @Test
  public void waitToSeeIfItWorks() {
    sleep(30000);
    assertTrue(true);
  }
}
