package org.molgenis.mvl.runner;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import htsjdk.variant.variantcontext.writer.VariantContextWriter;
import htsjdk.variant.variantcontext.writer.VariantContextWriterBuilder;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.molgenis.mvl.Settings;
import org.molgenis.mvl.WriterSettings;
import org.molgenis.mvl.converter.MvlVcfWriter;
import org.molgenis.mvl.converter.MvlVcfWriterImpl;
import org.molgenis.mvl.converter.VariantDedupper;
import org.springframework.stereotype.Component;

@Component
public class MvlVcfWriterFactoryImpl implements MvlVcfWriterFactory {

  private final VariantDedupper variantDedupper;

  MvlVcfWriterFactoryImpl(VariantDedupper variantDedupper) {
    this.variantDedupper = requireNonNull(variantDedupper);
  }

  @Override
  public MvlVcfWriter create(Settings settings) {
    VariantContextWriter vcfWriter = createVcfWriter(settings.getWriterSettings());
    return new MvlVcfWriterImpl(variantDedupper, vcfWriter, settings.getAppSettings());
  }

  private static VariantContextWriter createVcfWriter(WriterSettings settings) {
    Path outputVcfPath = settings.getOutputVcfPath();
    if (settings.isOverwriteOutput()) {
      try {
        Files.deleteIfExists(outputVcfPath);
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    } else if (Files.exists(outputVcfPath)) {
      throw new IllegalArgumentException(
          format("cannot create '%s' because it already exists.", outputVcfPath));
    }

    return new VariantContextWriterBuilder()
        .clearOptions()
        .setOutputFile(outputVcfPath.toFile())
        .build();
  }
}
