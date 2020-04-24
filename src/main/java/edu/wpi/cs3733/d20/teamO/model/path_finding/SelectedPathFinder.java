package edu.wpi.cs3733.d20.teamO.model.path_finding;

import com.google.inject.Singleton;
import lombok.Data;

/**
 * A class representing the path finder in use by the application
 */
@Data
@Singleton
public class SelectedPathFinder {

  /**
   * The current path finder in use by the application
   */
  private PathFinder currentPathFinder = new AStar();
}
