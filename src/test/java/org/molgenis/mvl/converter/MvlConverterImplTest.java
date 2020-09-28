package org.molgenis.mvl.converter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.molgenis.mvl.converter.model.HgvsTranslation;
import org.molgenis.mvl.converter.model.MvlTsvVariant;
import org.molgenis.mvl.converter.model.MvlVcfVariant;

@ExtendWith(MockitoExtension.class)
class MvlConverterImplTest {
  @Mock private HgvsTranslator hgvsTranslator;
  @Mock private MvlVcfVariantConverter mvlVcfVariantConverter;
  private MvlConverterImpl mvlConverter;

  @BeforeEach
  void setUp() {
    mvlConverter = new MvlConverterImpl(hgvsTranslator, mvlVcfVariantConverter);
  }

  @Test
  void convert() {
    MvlTsvReader mvlReader = mock(MvlTsvReader.class);

    MvlTsvVariant mvlTsvVariant = mock(MvlTsvVariant.class);
    List<MvlTsvVariant> mvlTsvVariants = List.of(mvlTsvVariant);
    when(mvlReader.read()).thenReturn(mvlTsvVariants);

    List<HgvsTranslation> hgvsTranslations =
        List.of(HgvsTranslation.builder().chrom("X").pos("150000000").ref("A").alt("G").build());
    when(hgvsTranslator.translate(mvlTsvVariants)).thenReturn(hgvsTranslations);

    MvlVcfVariant mvlVcfVariant = mock(MvlVcfVariant.class);
    List<MvlVcfVariant> mvlVcfVariants = List.of(mvlVcfVariant);
    when(mvlVcfVariantConverter.convert(mvlTsvVariants, hgvsTranslations)).thenReturn(mvlVcfVariants);

    MvlVcfWriter mvlWriter = mock(MvlVcfWriter.class);
    mvlConverter.convert(mvlReader, mvlWriter);
    verify(mvlWriter).write(mvlVcfVariants);
  }
}
