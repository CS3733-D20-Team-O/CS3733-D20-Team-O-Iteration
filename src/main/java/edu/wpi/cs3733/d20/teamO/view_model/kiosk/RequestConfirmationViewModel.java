package edu.wpi.cs3733.d20.teamO.view_model.kiosk;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.d20.teamO.model.database.DatabaseWrapper;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.ServiceRequestProperty;
import edu.wpi.cs3733.d20.teamO.model.database.db_model.Table;
import edu.wpi.cs3733.d20.teamO.model.datatypes.ServiceRequest;
import edu.wpi.cs3733.d20.teamO.model.material.Dialog.DialogViewModel;
import edu.wpi.cs3733.d20.teamO.model.material.SnackBar;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class RequestConfirmationViewModel extends DialogViewModel {

  private final SimpleStringProperty
      confirmationCode = new SimpleStringProperty(""),
      assignedTo = new SimpleStringProperty(""),
      location = new SimpleStringProperty(""),
      time = new SimpleStringProperty(""),
      type = new SimpleStringProperty(""),
      status = new SimpleStringProperty(""),
      data = new SimpleStringProperty("");

  private ServiceRequest serviceRequest;

  private final DatabaseWrapper database;
  private final SnackBar snackBar;

  public void setServiceRequest(String confirmationCode) {
    for (val serviceRequest : database.exportServiceRequests()) {
      if (serviceRequest.getRequestID().equals(confirmationCode)) {
        this.serviceRequest = serviceRequest;
        setConfirmationCode(confirmationCode);
        setLocation(serviceRequest.getRequestNode());
        setTime(serviceRequest.getRequestTime());
        setType(serviceRequest.getType());
        setStatus(serviceRequest.getStatus());
        setData(serviceRequest.getRequestData().getDisplayable());
        for (val employee : database.exportEmployees()) {
          if (serviceRequest.getEmployeeAssigned().equals(employee.getEmployeeID())) {
            setAssignedTo(employee.getName());
          }
        }
        return;
      }
    }
  }

  @FXML
  private void deleteAndClose() {
    val confirmationCode = getConfirmationCode();
    if (!confirmationCode.isBlank()) {
      database.deleteFromTable(Table.SERVICE_REQUESTS_TABLE,
          ServiceRequestProperty.REQUEST_ID, confirmationCode);
      val undoButton = new JFXButton(getString("serviceRequestDeleteSnackbarUndo"));
      undoButton.setStyle("-fx-text-fill: greenyellow");
      undoButton.setOnAction(e -> database.addServiceRequest(serviceRequest.getRequestID(),
          serviceRequest.getRequestTime(), serviceRequest.getRequestNode(),
          serviceRequest.getType(), serviceRequest.getStatus(),
          serviceRequest.getRequesterName(),
          serviceRequest.getWhoMarked(), serviceRequest.getEmployeeAssigned(),
          new Gson().toJson(serviceRequest.getRequestData())));
      snackBar.show(getString("serviceRequestDeleteSnackbar"), undoButton);
    }
    close();
  }

  public String getConfirmationCode() {
    return confirmationCode.get();
  }

  public SimpleStringProperty confirmationCodeProperty() {
    return confirmationCode;
  }

  public void setConfirmationCode(String confirmationCode) {
    this.confirmationCode.set(confirmationCode);
  }

  public String getAssignedTo() {
    return assignedTo.get();
  }

  public SimpleStringProperty assignedToProperty() {
    return assignedTo;
  }

  public void setAssignedTo(String assignedTo) {
    this.assignedTo.set(assignedTo);
  }

  public String getLocation() {
    return location.get();
  }

  public SimpleStringProperty locationProperty() {
    return location;
  }

  public void setLocation(String location) {
    this.location.set(location);
  }

  public String getTime() {
    return time.get();
  }

  public SimpleStringProperty timeProperty() {
    return time;
  }

  public void setTime(String time) {
    this.time.set(time);
  }

  public String getType() {
    return type.get();
  }

  public SimpleStringProperty typeProperty() {
    return type;
  }

  public void setType(String type) {
    this.type.set(type);
  }

  public String getStatus() {
    return status.get();
  }

  public SimpleStringProperty statusProperty() {
    return status;
  }

  public void setStatus(String status) {
    this.status.set(status);
  }

  public String getData() {
    return data.get();
  }

  public SimpleStringProperty dataProperty() {
    return data;
  }

  public void setData(String data) {
    this.data.set(data);
  }
}
