package org.molgenis.mvl.converter.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MvlTsvVariant {
  @CsvBindByName(column = "Transcript", required = true)
  String transcript;

  @CsvBindByName(column = "cDNA", required = true)
  String cDna;

  @CsvCustomBindByName(
      column = "Classification",
      required = true,
      converter = TextToClassificationConverter.class)
  Classification classification;
}
