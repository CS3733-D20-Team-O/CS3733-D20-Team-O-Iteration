package edu.wpi.cs3733.d20.teamO.view_model.admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.ServiceRequestProperty;
//import edu.wpi.cs3733.d20.teamO.model.database.db_model.EmployeeProperty;
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
//  @FXML
//  private TableColumn<EmployeeProperty, String> colAssigned;
  @FXML
  private TableView employeeTable;
  @FXML
  private TableColumn<String, String> colEmployeeName;

  private JFXButton btnAssign = new JFXButton("Assign");
  private JFXCheckBox cbShowUnavail = new JFXCheckBox("Show Unavailable");

  private void setupInterface(){
    btnAssign.getStyleClass().add("button-raised");
    cbShowUnavail.getStyleClass().add("custom-jfx-check-box");
    //Not certain where this goes
//    .custom-jfx-check-box{
//      -jfx-checked-color: #FFB970;
//      -jfx-unchecked-color: #58A5F0;
//    }

    PropertyValueFactory colServReq = new PropertyValueFactory("Service Requested");
    colServiceRequest.setCellFactory(colServReq);
    PropertyValueFactory colReqDate = new PropertyValueFactory("Date Requested");
    colServiceRequest.setCellFactory(colReqDate);
    PropertyValueFactory colEmpAssnd = new PropertyValueFactory("Assigned Employee");
    colServiceRequest.setCellFactory(colEmpAssnd);

    PropertyValueFactory colEmpName = new PropertyValueFactory("Employee Name");
    colServiceRequest.setCellFactory(colEmpName);

  }


}
