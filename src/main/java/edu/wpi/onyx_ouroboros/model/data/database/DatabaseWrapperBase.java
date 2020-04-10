package edu.wpi.onyx_ouroboros.model.data.database;


import com.google.inject.Inject;
import java.sql.Connection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The base of a wrapper for a database of PrototypeNodes
 */
@RequiredArgsConstructor(onConstructor_ = {@Inject})
abstract class DatabaseWrapperBase implements IDatabaseWrapper {

  /**
   * The connection to use in the DatabaseWrapper
   */
  @Getter
  private final Connection connection;
}
