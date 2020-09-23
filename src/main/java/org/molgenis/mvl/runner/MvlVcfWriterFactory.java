package org.molgenis.mvl.runner;

import org.molgenis.mvl.Settings;
import org.molgenis.mvl.converter.MvlVcfWriter;

public interface MvlVcfWriterFactory {
  MvlVcfWriter create(Settings settings);
}
