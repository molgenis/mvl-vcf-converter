package org.molgenis.mvl;

import java.net.URL;
import java.nio.file.Path;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Settings {
  Path inputMvlPath;
  URL translatorUrl;
  AppSettings appSettings;
  WriterSettings writerSettings;
}
