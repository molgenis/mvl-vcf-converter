package org.molgenis.mvl.converter;

import java.util.List;
import org.molgenis.mvl.converter.model.HgvsTranslation;
import org.molgenis.mvl.converter.model.MvlTsvVariant;
import org.molgenis.mvl.converter.model.MvlVcfVariant;

public interface MvlVcfVariantConverter {
  List<MvlVcfVariant> convert(List<MvlTsvVariant> mvlVariants, List<HgvsTranslation> hgvsTranslations);
}
