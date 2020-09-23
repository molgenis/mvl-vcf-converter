package org.molgenis.mvl.converter;

import java.util.List;
import org.molgenis.mvl.converter.model.HgvsTranslation;
import org.molgenis.mvl.converter.model.MvlTsvVariant;

public interface HgvsTranslator {
  List<HgvsTranslation> translate(List<MvlTsvVariant> mvlVariants);
}
