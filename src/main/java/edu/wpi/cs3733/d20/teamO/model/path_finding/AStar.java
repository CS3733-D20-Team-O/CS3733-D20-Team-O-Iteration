package edu.wpi.cs3733.d20.teamO.model.path_finding;

/**
 * Represents the AStar algorithm implementation and its dependencies
 */
class AStar extends CostBasedPathFinderTemplate {

  @Override
  protected double calculateWeight(double currentDistance, double distanceToGo) {
    return currentDistance + distanceToGo;
  }
}
