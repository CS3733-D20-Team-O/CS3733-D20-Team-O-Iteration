package edu.wpi.cs3733.d20.teamO.view_model.admin;

import edu.wpi.cs3733.d20.teamO.view_model.ViewModelBase;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class RequestHandlerViewModel extends ViewModelBase {

  @FXML
  private TableView serviceTable;
  @FXML
  private TableColumn<String, String> colServiceRequest;
  @FXML
  private TableColumn<String, String> colDateRequest;
  @FXML
  private TableColumn<String, String> colAssigned;
  @FXML
  private TableView employeeTable;
  @FXML
  private TableColumn<String, String> colEmployeeName;
  @FXML
  private Button btnClose;
  @FXML
  private Button btnGetEmployees;
  @FXML
  private Button btnAssign;
  @FXML
  private RadioButton showUnavail;




}
