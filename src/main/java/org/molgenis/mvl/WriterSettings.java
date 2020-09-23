package org.molgenis.mvl;

import java.nio.file.Path;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class WriterSettings {
  Path outputVcfPath;
  boolean overwriteOutput;
}
