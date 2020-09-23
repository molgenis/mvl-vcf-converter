package org.molgenis.mvl.converter.model;

public enum Classification {
  BENIGN("B"),
  LIKELY_BENIGN("LB"),
  VOUS("VUS"),
  LIKELY_PATHOGENIC("LP"),
  PATHOGENIC("P");

  private final String id;

  Classification(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }
}
