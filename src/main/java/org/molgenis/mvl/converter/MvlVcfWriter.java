package org.molgenis.mvl.converter;

import java.util.List;
import org.molgenis.mvl.converter.model.MvlVcfVariant;

public interface MvlVcfWriter extends AutoCloseable {
  void writeHeader();

  void write(List<MvlVcfVariant> mvlVcfVariants);
}
