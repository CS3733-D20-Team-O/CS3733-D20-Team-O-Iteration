package edu.wpi.onyx_ouroboros;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Do not delete this test class
 */
public class MainTest {

  @Test
  public void testMainClass() throws ClassNotFoundException {
    assertEquals(App.class, Class.forName("edu.wpi.onyx_ouroboros.App"));
  }
}