package edu.wpi.cs3733.d20.teamO.view_model.admin;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.ServiceRequestProperty;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.EmployeeProperty;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.util.Date;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class RequestHandlerViewModel extends ViewModelBase {

  @FXML
  private TableView serviceTable;
  @FXML
  private TableColumn<ServiceRequestProperty, String> colServiceRequest;
  @FXML
  private TableColumn<Date, String> colDateRequest;
  @FXML
  private TableColumn<EmployeeProperty, String> colAssigned;
  @FXML
  private TableView employeeTable;
  @FXML
  private TableColumn<String, String> colEmployeeName;

  private JFXButton btnAssign = new JFXButton("Assign");

  @FXML
  private RadioButton showUnavail;


  private void setupInterface(){
    btnAssign.getStyleClass().add("button-raised");
    colServiceRequest.setCellFactory(new PropertyValueFactory<ServiceRequestProperty, String>("Service Requested"));


  }


}
