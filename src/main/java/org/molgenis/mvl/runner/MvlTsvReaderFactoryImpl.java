package org.molgenis.mvl.runner;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import org.molgenis.mvl.Settings;
import org.molgenis.mvl.converter.MvlTsvReader;
import org.molgenis.mvl.converter.MvlTsvReaderImpl;
import org.springframework.stereotype.Component;

@Component
public class MvlTsvReaderFactoryImpl implements MvlTsvReaderFactory {

  @Override
  public MvlTsvReader create(Settings settings) {
    try {
      return new MvlTsvReaderImpl(Files.newBufferedReader(settings.getInputMvlPath(), UTF_8));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
