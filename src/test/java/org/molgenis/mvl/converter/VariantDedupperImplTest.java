package org.molgenis.mvl.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.molgenis.mvl.converter.model.Classification.BENIGN;
import static org.molgenis.mvl.converter.model.Classification.PATHOGENIC;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.molgenis.mvl.converter.model.MvlVcfVariant;

class VariantDedupperImplTest {
  private VariantDedupperImpl variantDedupperImpl;

  @BeforeEach
  void setUp() {
    variantDedupperImpl = new VariantDedupperImpl();
  }

  @Test
  void dedup() {
    MvlVcfVariant mvlVcfVariant0 =
        MvlVcfVariant.builder()
            .chrom("X")
            .pos(150000000)
            .ref("A")
            .alt("G")
            .classification(BENIGN)
            .build();
    MvlVcfVariant mvlVcfVariant1 =
        MvlVcfVariant.builder()
            .chrom("X")
            .pos(150000000)
            .ref("A")
            .alt("G")
            .classification(BENIGN)
            .build();
    MvlVcfVariant mvlVcfVariant2 =
        MvlVcfVariant.builder().chrom("X").pos(150000000).ref("C").alt("T").build();
    MvlVcfVariant mvlVcfVariant3 =
        MvlVcfVariant.builder()
            .chrom("X")
            .pos(150000001)
            .ref("A")
            .alt("G")
            .classification(BENIGN)
            .build();
    MvlVcfVariant mvlVcfVariant4 =
        MvlVcfVariant.builder()
            .chrom("X")
            .pos(150000001)
            .ref("A")
            .alt("G")
            .classification(PATHOGENIC)
            .build();
    assertEquals(
        List.of(mvlVcfVariant0, mvlVcfVariant2),
        variantDedupperImpl.dedup(
            List.of(
                mvlVcfVariant0, mvlVcfVariant1, mvlVcfVariant2, mvlVcfVariant3, mvlVcfVariant4)));
  }
}
