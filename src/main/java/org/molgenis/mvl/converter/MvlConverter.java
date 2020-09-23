package org.molgenis.mvl.converter;

public interface MvlConverter {

  void convert(MvlTsvReader mvlReader, MvlVcfWriter mvlWriter);
}
