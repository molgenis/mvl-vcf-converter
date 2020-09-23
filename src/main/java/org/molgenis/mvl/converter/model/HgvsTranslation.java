package org.molgenis.mvl.converter.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HgvsTranslation {
  String chrom;
  String pos;
  String ref;
  String alt;
  String error;
}
