package org.molgenis.mvl.converter.model;

import static java.lang.String.format;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

public class TextToClassificationConverter extends AbstractBeanField<Classification, Object> {

  @Override
  public Object convert(String value) throws CsvDataTypeMismatchException {
    Classification classification;
    switch (value) {
      case "Benign":
        classification = Classification.BENIGN;
        break;
      case "Likely benign":
        classification = Classification.LIKELY_BENIGN;
        break;
      case "VOUS":
        classification = Classification.VOUS;
        break;
      case "Likely pathogenic":
        classification = Classification.LIKELY_PATHOGENIC;
        break;
      case "Pathogenic":
        classification = Classification.PATHOGENIC;
        break;
      default:
        throw new CsvDataTypeMismatchException(format("invalid classification '%s'", value));
    }
    return classification;
  }
}
