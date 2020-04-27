package edu.wpi.cs3733.d20.teamO;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;

public class ResourceBundleMock extends ResourceBundle {

  private final Map<String, String> strings = new HashMap<>();

  public void put(String key, String value) {
    strings.put(key, value);
  }

  @Override
  protected Object handleGetObject(String s) {
    return strings.getOrDefault(s, "");
  }

  @Override
  public boolean containsKey(String key) {
    return true;
  }

  @Override
  public Enumeration<String> getKeys() {
    return new Vector<>(strings.keySet()).elements();
  }
}
