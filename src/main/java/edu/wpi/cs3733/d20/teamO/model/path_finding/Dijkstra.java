package edu.wpi.cs3733.d20.teamO.model.path_finding;


class Dijkstra extends CostBasedPathFinderTemplate {

  @Override
  protected double calculateWeight(double currentDistance, double distanceToGo) {
    return currentDistance;
  }
}
