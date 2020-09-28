package org.molgenis.mvl.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.StringReader;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.molgenis.mvl.converter.model.Classification;
import org.molgenis.mvl.converter.model.MvlTsvVariant;

class MvlTsvReaderImplTest {

  @Test
  void read() {
    String tsv = "Transcript\tcDNA\tClassification\nNM_000001.1\tc.1A>G\tBenign";
    try (MvlTsvReaderImpl mvlTsvReaderImpl = new MvlTsvReaderImpl(new StringReader(tsv))) {
      assertEquals(
          List.of(
              MvlTsvVariant.builder()
                  .transcript("NM_000001.1")
                  .cDna("c.1A>G")
                  .classification(Classification.BENIGN)
                  .build()),
          mvlTsvReaderImpl.read());
    }
  }

  @Test
  void readEmptyTranscript() {
    String tsv = "Transcript\tcDNA\tClassification\n\tc.1A>G\tBenign";
    try (MvlTsvReaderImpl mvlTsvReaderImpl =
        new MvlTsvReaderImpl(new StringReader(tsv))) {
      assertThrows(RuntimeException.class, mvlTsvReaderImpl::read);
    }
  }

  @Test
  void readEmptyCdna() {
    String tsv = "Transcript\tcDNA\tClassification\nNM_000001.1\t\tBenign";
    try (MvlTsvReaderImpl mvlTsvReaderImpl =
        new MvlTsvReaderImpl(new StringReader(tsv))) {
      assertThrows(RuntimeException.class, mvlTsvReaderImpl::read);
    }
  }

  @Test
  void readEmptyClassification() {
    String tsv = "Transcript\tcDNA\tClassification\nNM_000001.1\tc.1A>G\t";
    try (MvlTsvReaderImpl mvlTsvReaderImpl =
        new MvlTsvReaderImpl(new StringReader(tsv))) {
      assertThrows(RuntimeException.class, mvlTsvReaderImpl::read);
    }
  }

  @Test
  void readInvalidClassification() {
    String tsv = "Transcript\tcDNA\tClassification\nNM_000001.1\tc.1A>G\tB";
    try (MvlTsvReaderImpl mvlTsvReaderImpl =
        new MvlTsvReaderImpl(new StringReader(tsv))) {
      assertThrows(RuntimeException.class, mvlTsvReaderImpl::read);
    }
  }

  @Test
  void readInvalidHeader() {
    try (MvlTsvReaderImpl mvlTsvReaderImpl =
        new MvlTsvReaderImpl(new StringReader("invalid_header"))) {
      assertThrows(RuntimeException.class, mvlTsvReaderImpl::read);
    }
  }
}
