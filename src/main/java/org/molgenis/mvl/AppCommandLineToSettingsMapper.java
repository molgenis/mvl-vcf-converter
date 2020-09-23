package org.molgenis.mvl;

import static java.util.Arrays.asList;
import static org.molgenis.mvl.AppCommandLineOptions.OPT_FORCE;
import static org.molgenis.mvl.AppCommandLineOptions.OPT_INPUT;
import static org.molgenis.mvl.AppCommandLineOptions.OPT_OUTPUT;
import static org.molgenis.mvl.AppCommandLineOptions.OPT_TRANSLATOR;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import org.apache.commons.cli.CommandLine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
class AppCommandLineToSettingsMapper {

  private final String appName;
  private final String appVersion;

  AppCommandLineToSettingsMapper(
      @Value("${app.name}") String appName, @Value("${app.version}") String appVersion) {
    this.appName = appName;
    this.appVersion = appVersion;
  }

  Settings map(CommandLine commandLine, String... args) {
    AppSettings appSettings = createAppSettings(args);
    Path inputPath = Path.of(commandLine.getOptionValue(OPT_INPUT));
    URI translatorUri = createTranslatorUri(commandLine);
    WriterSettings writerSettings = createWriterSettings(commandLine);
    return Settings.builder()
        .inputMvlPath(inputPath)
        .translatorUri(translatorUri)
        .appSettings(appSettings)
        .writerSettings(writerSettings)
        .build();
  }

  private URI createTranslatorUri(CommandLine commandLine) {
    URI translatorUrl;
    try {
      translatorUrl = new URI(commandLine.getOptionValue(OPT_TRANSLATOR));
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException(e);
    }
    return translatorUrl;
  }

  private AppSettings createAppSettings(String... args) {
    return AppSettings.builder().name(appName).version(appVersion).args(asList(args)).build();
  }

  private WriterSettings createWriterSettings(CommandLine commandLine) {
    Path outputPath;
    if (commandLine.hasOption(OPT_OUTPUT)) {
      outputPath = Path.of(commandLine.getOptionValue(OPT_OUTPUT));
    } else {
      outputPath = Path.of(commandLine.getOptionValue(OPT_INPUT).replace(".txt", ".vcf"));
    }

    boolean overwriteOutput = commandLine.hasOption(OPT_FORCE);

    return WriterSettings.builder()
        .outputVcfPath(outputPath)
        .overwriteOutput(overwriteOutput)
        .build();
  }
}
