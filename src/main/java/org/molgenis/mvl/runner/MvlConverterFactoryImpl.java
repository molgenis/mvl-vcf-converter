package org.molgenis.mvl.runner;

import static java.util.Objects.requireNonNull;

import org.molgenis.mvl.Settings;
import org.molgenis.mvl.converter.HgvsTranslatorClient;
import org.molgenis.mvl.converter.HgvsTranslatorImpl;
import org.molgenis.mvl.converter.MvlConverter;
import org.molgenis.mvl.converter.MvlConverterImpl;
import org.molgenis.mvl.converter.MvlVcfVariantConverter;
import org.springframework.stereotype.Component;

@Component
public class MvlConverterFactoryImpl implements MvlConverterFactory {

  private final HgvsTranslatorClient hgvsTranslatorClient;
  private final MvlVcfVariantConverter mvlVcfVariantConverter;

  MvlConverterFactoryImpl(
      HgvsTranslatorClient hgvsTranslatorClient, MvlVcfVariantConverter mvlVcfVariantConverter) {
    this.hgvsTranslatorClient = requireNonNull(hgvsTranslatorClient);
    this.mvlVcfVariantConverter = requireNonNull(mvlVcfVariantConverter);
  }

  @Override
  public MvlConverter create(Settings settings) {
    HgvsTranslatorImpl hgvsTranslator =
        new HgvsTranslatorImpl(hgvsTranslatorClient, settings.getTranslatorUri());
    return new MvlConverterImpl(hgvsTranslator, mvlVcfVariantConverter);
  }
}
