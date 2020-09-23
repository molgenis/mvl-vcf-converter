package org.molgenis.mvl.converter.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MvlVcfVariant {
  String chrom;
  int pos;
  String ref;
  String alt;
  Classification classification;
}
