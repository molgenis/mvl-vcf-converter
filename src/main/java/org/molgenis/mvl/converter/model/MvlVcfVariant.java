package org.molgenis.mvl.converter.model;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@Builder
@NonFinal
public class MvlVcfVariant {
  String chrom;
  int pos;
  String ref;
  String alt;
  Classification classification;
}
