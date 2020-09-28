package org.molgenis.mvl.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.molgenis.mvl.converter.model.Classification.LIKELY_BENIGN;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.molgenis.mvl.converter.model.HgvsTranslation;
import org.molgenis.mvl.converter.model.MvlTsvVariant;
import org.molgenis.mvl.converter.model.MvlVcfVariant;

class MvlVcfVariantConverterImplTest {
  private MvlVcfVariantConverterImpl mvlVcfVariantConverter;

  @BeforeEach
  void setUp() {
    mvlVcfVariantConverter = new MvlVcfVariantConverterImpl();
  }

  @Test
  void convert() {
    MvlTsvVariant mvlTsvVariant =
        MvlTsvVariant.builder()
            .transcript("NM_000001.1")
            .cDna("c.1A>G")
            .classification(LIKELY_BENIGN)
            .build();
    HgvsTranslation hgvsTranslation =
        HgvsTranslation.builder().chrom("X").pos("150000000").ref("A").alt("G").build();
    assertEquals(
        List.of(
            MvlVcfVariant.builder()
                .chrom("X")
                .pos(150000000)
                .ref("A")
                .alt("G")
                .classification(LIKELY_BENIGN)
                .build()),
        mvlVcfVariantConverter.convert(List.of(mvlTsvVariant), List.of(hgvsTranslation)));
  }

  @Test
  void convertError() {
    MvlTsvVariant mvlTsvVariant =
        MvlTsvVariant.builder()
            .transcript("NM_000001.1")
            .cDna("c.1A<G")
            .classification(LIKELY_BENIGN)
            .build();
    HgvsTranslation hgvsTranslation = HgvsTranslation.builder().error("my_error").build();
    assertEquals(
        List.of(),
        mvlVcfVariantConverter.convert(List.of(mvlTsvVariant), List.of(hgvsTranslation)));
  }

  @Test
  void convertListSizeMismatch() {
    MvlTsvVariant mvlTsvVariant = mock(MvlTsvVariant.class);
    List<MvlTsvVariant> mvlVariants = List.of(mvlTsvVariant);
    List<HgvsTranslation> hgvsTranslations = List.of();
    assertThrows(
        IllegalArgumentException.class,
        () -> mvlVcfVariantConverter.convert(mvlVariants, hgvsTranslations));
  }

  @Test
  void convertListEmpty() {
    assertEquals(List.of(), mvlVcfVariantConverter.convert(List.of(), List.of()));
  }
}
