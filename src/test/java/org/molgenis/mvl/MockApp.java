package org.molgenis.mvl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.molgenis.mvl.converter.HgvsTranslatorClient;
import org.molgenis.mvl.converter.model.HgvsTranslation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@SpringBootApplication
public class MockApp {
  public static void main(String[] args) {
    SpringApplication.run(MockApp.class, args);
  }

  @Bean
  @Primary
  public HgvsTranslatorClient hgvsTranslatorClient() throws URISyntaxException {
    HgvsTranslatorClient hgvsTranslatorClient = mock(HgvsTranslatorClient.class);

    when(hgvsTranslatorClient.get(
            new URI("http://mocked?keep_left_anchor=True"),
            List.of(
                "NM_000001.1:c.1A>G",
                "NM_000002.1:c.2A>G",
                "NM_000003.1:c.3A>G",
                "NM_000004.1:c.4A>G",
                "NM_000005.1:c.5A>G",
                "NM_000006.1:c.6A>G")))
        .thenReturn(
            List.of(
                HgvsTranslation.builder().chrom("X").pos("150000000").ref("A").alt("G").build(),
                HgvsTranslation.builder().chrom("12").pos("9250000").ref("A").alt("G").build(),
                HgvsTranslation.builder().chrom("1").pos("9000001").ref("A").alt("G").build(),
                HgvsTranslation.builder().chrom("1").pos("9000000").ref("A").alt("G").build(),
                HgvsTranslation.builder().error("my_error").build(),
                HgvsTranslation.builder().chrom("12").pos("9250000").ref("A").alt("G").build()));
    return hgvsTranslatorClient;
  }
}
