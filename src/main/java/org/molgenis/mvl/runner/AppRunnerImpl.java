package org.molgenis.mvl.runner;

import static java.util.Objects.requireNonNull;

import org.molgenis.mvl.converter.MvlConverter;
import org.molgenis.mvl.converter.MvlTsvReader;
import org.molgenis.mvl.converter.MvlVcfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class AppRunnerImpl implements AppRunner {
  private static final Logger LOGGER = LoggerFactory.getLogger(AppRunnerImpl.class);

  private final MvlConverter mvlConverter;
  private final MvlTsvReader mvlTsvReader;
  private final MvlVcfWriter mvlVcfWriter;

  AppRunnerImpl(MvlConverter mvlConverter, MvlTsvReader mvlTsvReader, MvlVcfWriter mvlVcfWriter) {
    this.mvlConverter = requireNonNull(mvlConverter);
    this.mvlTsvReader = requireNonNull(mvlTsvReader);
    this.mvlVcfWriter = requireNonNull(mvlVcfWriter);
  }

  public void run() {
    LOGGER.info("converting managed variant list to vcf (this might take a while)...");
    mvlConverter.convert(mvlTsvReader, mvlVcfWriter);
    LOGGER.info("done");
  }

  @Override
  public void close() {
    try {
      mvlVcfWriter.close();
    } catch (Exception e) {
      LOGGER.error("error closing writer", e);
    }
    try {
      mvlTsvReader.close();
    } catch (Exception e) {
      LOGGER.error("error closing reader", e);
    }
  }
}
