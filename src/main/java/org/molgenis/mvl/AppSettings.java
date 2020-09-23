package org.molgenis.mvl;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AppSettings {
  String name;
  String version;
  List<String> args;
}
