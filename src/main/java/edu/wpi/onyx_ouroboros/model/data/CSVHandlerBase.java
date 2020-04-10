package edu.wpi.onyx_ouroboros.model.data;

import com.google.inject.Inject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Handles parsing and exporting PrototypeNode CSV files
 */
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public abstract class CSVHandlerBase implements ICSVHandler {

  /**
   * The database to import records into and export records from
   */
  @Getter
  private final IDatabaseWrapper database;
}
