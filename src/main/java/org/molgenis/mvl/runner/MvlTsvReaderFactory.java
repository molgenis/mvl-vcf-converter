package org.molgenis.mvl.runner;

import org.molgenis.mvl.Settings;
import org.molgenis.mvl.converter.MvlTsvReader;

public interface MvlTsvReaderFactory {
  MvlTsvReader create(Settings settings);
}
