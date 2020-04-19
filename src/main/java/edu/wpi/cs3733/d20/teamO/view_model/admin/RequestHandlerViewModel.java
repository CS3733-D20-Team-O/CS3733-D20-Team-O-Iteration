package edu.wpi.cs3733.d20.teamO.view_model.admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.EmployeeProperty;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.ServiceRequestProperty;
import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import java.util.Date;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

//import edu.wpi.cs3733.d20.teamO.model.database.db_model.EmployeeProperty;

public class RequestHandlerViewModel extends ViewModelBase {

  @FXML
  private TableView serviceTable;
  @FXML
  private TableColumn<ServiceRequestProperty, String> colServiceRequest;
  @FXML
  private TableColumn<Date, String> colDateRequest;
  private final JFXButton btnAssign = new JFXButton("Assign");
  @FXML
  private TableView employeeTable;
  @FXML
  private TableColumn<String, String> colEmployeeName;
  private final JFXCheckBox cbShowUnavail = new JFXCheckBox("Show Unavailable");
  @FXML
  private TableColumn<EmployeeProperty, String> colAssigned;

  private void setupInterface() {
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

  private void updateEmployeeTable() {
    //when the serviceTable row selected populate the employeeTable
    //get the serviceTable row serviceRequestProperty.type
    //populate the employeeTable with employees with the same type
    //check if employee is available
    //if not available don't show
  }

  private void assignEmployee() {
    //Get current serviceTable row
    //Get current employeeTable row
    //Get the employee
    //update the ServiceRequestProperty for assignedEmployee to the employee
    //if no employee selected this doesn't work
//    serviceTable.getItems().get(0);

  }

  private void setCbShowUnavail() {
    //when box is pressed
    //if checked=true
    //set to checked=false
    //update employeeTable with just service Type
  }


}
