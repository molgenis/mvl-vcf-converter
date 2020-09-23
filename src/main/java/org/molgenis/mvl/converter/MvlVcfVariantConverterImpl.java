package org.molgenis.mvl.converter;

import java.util.ArrayList;
import java.util.List;
import org.molgenis.mvl.converter.model.HgvsTranslation;
import org.molgenis.mvl.converter.model.MvlTsvVariant;
import org.molgenis.mvl.converter.model.MvlVcfVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MvlVcfVariantConverterImpl implements MvlVcfVariantConverter {

  private static final Logger LOGGER = LoggerFactory.getLogger(MvlVcfVariantConverterImpl.class);

  @Override
  public List<MvlVcfVariant> map(
      List<MvlTsvVariant> mvlVariants, List<HgvsTranslation> hgvsTranslations) {
    if (mvlVariants.isEmpty()) {
      return List.of();
    }

    List<MvlVcfVariant> mvlVcfVariants = new ArrayList<>(mvlVariants.size());
    for (int i = 0; i < mvlVariants.size(); ++i) {
      MvlTsvVariant mvlTsvVariant = mvlVariants.get(i);
      HgvsTranslation hgvsTranslation = hgvsTranslations.get(i);

      if (hgvsTranslation.getError() == null) {
        MvlVcfVariant mvlVcfVariant = convert(mvlTsvVariant, hgvsTranslation);
        mvlVcfVariants.add(mvlVcfVariant);
      } else {
        handleError(mvlTsvVariant, hgvsTranslation);
      }
    }
    return mvlVcfVariants;
  }

  private static MvlVcfVariant convert(
      MvlTsvVariant mvlTsvVariant, HgvsTranslation hgvsTranslation) {
    return MvlVcfVariant.builder()
        .chrom(hgvsTranslation.getChrom())
        .pos(Integer.parseInt(hgvsTranslation.getPos()))
        .ref(hgvsTranslation.getRef())
        .alt(hgvsTranslation.getAlt())
        .classification(mvlTsvVariant.getClassification())
        .build();
  }

  private static void handleError(MvlTsvVariant mvlTsvVariant, HgvsTranslation hgvsTranslation) {
    LOGGER.error(
        "skipping variant due to error '{}:{}': {}",
        mvlTsvVariant.getTranscript(),
        mvlTsvVariant.getCDna(),
        hgvsTranslation.getError());
  }
}
