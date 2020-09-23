package org.molgenis.mvl.runner;

import static java.util.Objects.requireNonNull;

import org.molgenis.mvl.Settings;
import org.molgenis.mvl.converter.MvlConverter;
import org.molgenis.mvl.converter.MvlTsvReader;
import org.molgenis.mvl.converter.MvlVcfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class AppRunnerFactoryImpl implements AppRunnerFactory {

  private static final Logger LOGGER = LoggerFactory.getLogger(AppRunnerFactoryImpl.class);

  private final MvlTsvReaderFactory mvlTsvReaderFactory;
  private final MvlConverterFactory mvlConverterFactory;
  private final MvlVcfWriterFactory mvlVcfWriterFactory;

  AppRunnerFactoryImpl(
      MvlTsvReaderFactory mvlTsvReaderFactory,
      MvlConverterFactory mvlConverterFactory,
      MvlVcfWriterFactory mvlVcfWriterFactory) {
    this.mvlTsvReaderFactory = requireNonNull(mvlTsvReaderFactory);
    this.mvlConverterFactory = requireNonNull(mvlConverterFactory);
    this.mvlVcfWriterFactory = requireNonNull(mvlVcfWriterFactory);
  }

  // Suppress 'Resources should be closed'
  @SuppressWarnings("java:S2095")
  @Override
  public AppRunner create(Settings settings) {
    MvlTsvReader mvlTsvReader = mvlTsvReaderFactory.create(settings);
    try {
      MvlConverter mvlConverter = mvlConverterFactory.create(settings);
      MvlVcfWriter mvlVcfWriter = mvlVcfWriterFactory.create(settings);
      return new AppRunnerImpl(mvlConverter, mvlTsvReader, mvlVcfWriter);
    } catch (Exception e) {
      try {
        mvlTsvReader.close();
      } catch (Exception closeException) {
        LOGGER.warn("error closing vcf reader", closeException);
      }
      throw e;
    }
  }
}
