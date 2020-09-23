package org.molgenis.mvl.converter;

import java.util.List;
import org.molgenis.mvl.converter.model.MvlTsvVariant;

public interface MvlTsvReader extends AutoCloseable {
  List<MvlTsvVariant> read();
}
