package org.molgenis.mvl.runner;

import org.molgenis.mvl.Settings;
import org.molgenis.mvl.converter.MvlConverter;

public interface MvlConverterFactory {

  MvlConverter create(Settings settings);
}
