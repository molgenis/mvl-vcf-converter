package org.molgenis.mvl;

import java.net.URI;
import java.nio.file.Path;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Settings {
  Path inputMvlPath;
  URI translatorUri;
  AppSettings appSettings;
  WriterSettings writerSettings;
}
