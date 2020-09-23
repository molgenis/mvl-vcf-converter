package org.molgenis.mvl.runner;

import org.molgenis.mvl.Settings;

public interface AppRunnerFactory {

  AppRunner create(Settings settings);
}
