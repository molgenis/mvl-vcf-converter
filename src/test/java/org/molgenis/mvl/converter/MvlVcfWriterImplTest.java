package org.molgenis.mvl.converter;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.molgenis.mvl.converter.model.Classification.BENIGN;
import static org.molgenis.mvl.converter.model.Classification.PATHOGENIC;

import htsjdk.variant.variantcontext.Allele;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.variantcontext.writer.VariantContextWriter;
import htsjdk.variant.vcf.VCFHeader;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.molgenis.mvl.AppSettings;
import org.molgenis.mvl.converter.model.MvlVcfVariant;

@ExtendWith({MockitoExtension.class})
class MvlVcfWriterImplTest {
  @Mock VariantDedupper variantDedupper;
  @Mock VariantContextWriter vcfWriter;
  @Mock AppSettings appSettings;
  private MvlVcfWriterImpl mvlVcfWriter;

  @BeforeEach
  void setUp() {
    mvlVcfWriter = new MvlVcfWriterImpl(variantDedupper, vcfWriter, appSettings);
  }

  @AfterEach
  void tearDown() {
    mvlVcfWriter.close();
  }

  @Test
  void writeHeader() {
    // see integration test for additional tests
    mvlVcfWriter.writeHeader();
    verify(vcfWriter).writeHeader(any(VCFHeader.class));
  }

  @Test
  void write() {
    MvlVcfVariant mvlVcfVariant0 =
        MvlVcfVariant.builder()
            .chrom("16")
            .pos(8800000)
            .ref("A")
            .alt("G")
            .classification(BENIGN)
            .build();
    MvlVcfVariant mvlVcfVariant1 =
        MvlVcfVariant.builder()
            .chrom("1")
            .pos(9000000)
            .ref("A")
            .alt("G")
            .classification(PATHOGENIC)
            .build();
    List<MvlVcfVariant> mvlVcfVariants = List.of(mvlVcfVariant0, mvlVcfVariant1);

    when(variantDedupper.dedup(mvlVcfVariants)).thenReturn(mvlVcfVariants);

    mvlVcfWriter.write(mvlVcfVariants);

    ArgumentCaptor<VariantContext> variantContextCaptor =
        ArgumentCaptor.forClass(VariantContext.class);
    verify(vcfWriter, times(2)).add(variantContextCaptor.capture());

    List<VariantContext> variantContexts = variantContextCaptor.getAllValues();
    assertAll(
        () -> {
          VariantContext variantContext0 = variantContexts.get(0);
          assertAll(
              () -> assertEquals("1", variantContext0.getContig()),
              () -> assertEquals(9000000, variantContext0.getStart()),
              () -> assertEquals("A", variantContext0.getReference().getBaseString()),
              () ->
                  assertEquals(
                      List.of("G"),
                      variantContext0.getAlternateAlleles().stream()
                          .map(Allele::getBaseString)
                          .collect(toList())),
              () -> assertEquals(List.of("P"), variantContext0.getAttribute("MVL")));
        },
        () -> {
          VariantContext variantContext1 = variantContexts.get(1);
          assertAll(
              () -> assertEquals("16", variantContext1.getContig()),
              () -> assertEquals(8800000, variantContext1.getStart()),
              () -> assertEquals("A", variantContext1.getReference().getBaseString()),
              () ->
                  assertEquals(
                      List.of("G"),
                      variantContext1.getAlternateAlleles().stream()
                          .map(Allele::getBaseString)
                          .collect(toList())),
              () -> assertEquals(List.of("B"), variantContext1.getAttribute("MVL")));
        });
  }
}
