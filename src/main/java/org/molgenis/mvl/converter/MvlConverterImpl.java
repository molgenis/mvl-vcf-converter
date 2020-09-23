package org.molgenis.mvl.converter;

import static java.util.Objects.requireNonNull;

import java.util.List;
import org.molgenis.mvl.converter.model.HgvsTranslation;
import org.molgenis.mvl.converter.model.MvlTsvVariant;
import org.molgenis.mvl.converter.model.MvlVcfVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MvlConverterImpl implements MvlConverter {

  private static final Logger LOGGER = LoggerFactory.getLogger(MvlConverterImpl.class);

  private final HgvsTranslator hgvsTranslator;
  private final MvlVcfVariantConverter mvlVcfVariantConverter;

  public MvlConverterImpl(
      HgvsTranslator hgvsTranslator, MvlVcfVariantConverter mvlVcfVariantConverter) {
    this.hgvsTranslator = requireNonNull(hgvsTranslator);
    this.mvlVcfVariantConverter = requireNonNull(mvlVcfVariantConverter);
  }

  @Override
  public void convert(MvlTsvReader mvlReader, MvlVcfWriter mvlWriter) {
    LOGGER.debug("reading managed variant list records...");
    List<MvlTsvVariant> mvlVariants = mvlReader.read();
    LOGGER.debug("done");

    LOGGER.debug("translating managed variant list records...");
    List<HgvsTranslation> hgvsTranslations = hgvsTranslator.translate(mvlVariants);
    List<MvlVcfVariant> mvlVcfVariants = mvlVcfVariantConverter.map(mvlVariants, hgvsTranslations);
    LOGGER.debug("done");

    LOGGER.debug("writing managed variant list records...");
    mvlWriter.writeHeader();
    mvlWriter.write(mvlVcfVariants);
    LOGGER.debug("done");
  }
}
