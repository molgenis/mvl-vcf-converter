package org.molgenis.mvl.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.POST;

import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.molgenis.mvl.converter.model.HgvsTranslation;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class HgvsTranslatorClientImplTest {
  @Mock private RestTemplate restTemplate;
  private HgvsTranslatorClientImpl hgvsTranslatorClient;

  @BeforeEach
  void setUp() {
    hgvsTranslatorClient = new HgvsTranslatorClientImpl(restTemplate);
  }

  @Test
  void get() throws URISyntaxException {
    List<String> hgvsCDnaList = List.of("NM_000001.1:c.1A>G", "NM_000002.1:c.2A>G");
    URI uri = new URI("https://mock.org");
    HttpEntity<List<String>> entity = new HttpEntity<>(hgvsCDnaList);
    @SuppressWarnings("unchecked")
    ResponseEntity<List<HgvsTranslation>> responseEntity = mock(ResponseEntity.class);
    when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
    List<HgvsTranslation> hgvsTranslations =
        List.of(
            HgvsTranslation.builder().chrom("X").pos("150000000").ref("A").alt("G").build(),
            HgvsTranslation.builder().chrom("12").pos("9250000").ref("A").alt("G").build());
    when(responseEntity.getBody()).thenReturn(hgvsTranslations);
    when(restTemplate.exchange(
            uri, POST, entity, new ParameterizedTypeReference<List<HgvsTranslation>>() {}))
        .thenReturn(responseEntity);
    assertEquals(hgvsTranslations, hgvsTranslatorClient.get(uri, hgvsCDnaList));
  }

  @Test
  void getError() throws URISyntaxException {
    List<String> hgvsCDnaList = List.of("NM_000001.1:c.1A>G", "NM_000002.1:c.2A>G");
    URI uri = new URI("https://mock.org");
    HttpEntity<List<String>> entity = new HttpEntity<>(hgvsCDnaList);
    @SuppressWarnings("unchecked")
    ResponseEntity<List<HgvsTranslation>> responseEntity = mock(ResponseEntity.class);
    when(responseEntity.getStatusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
    when(restTemplate.exchange(
            uri, POST, entity, new ParameterizedTypeReference<List<HgvsTranslation>>() {}))
        .thenReturn(responseEntity);
    assertThrows(UncheckedIOException.class, () -> hgvsTranslatorClient.get(uri, hgvsCDnaList));
  }
}
