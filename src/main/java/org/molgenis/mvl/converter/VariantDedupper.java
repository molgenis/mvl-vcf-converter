package org.molgenis.mvl.converter;

import java.util.List;
import org.molgenis.mvl.converter.model.MvlVcfVariant;

public interface VariantDedupper {
  List<MvlVcfVariant> dedup(List<MvlVcfVariant> mvlVcfVariants);
}
