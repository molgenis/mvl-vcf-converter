package org.molgenis.mvl.converter;

import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.util.List;
import org.molgenis.mvl.converter.model.HgvsTranslation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HgvsTranslatorClientImpl implements HgvsTranslatorClient {
  private final RestTemplate restTemplate;

  // for testability
  HgvsTranslatorClientImpl(RestTemplate restTemplate) {
    this.restTemplate = requireNonNull(restTemplate);
  }

  @Autowired
  HgvsTranslatorClientImpl(RestTemplateBuilder restTemplateBuilder) {
    restTemplate = createRestTemplate(restTemplateBuilder);
  }

  @Override
  public List<HgvsTranslation> get(URI uri, List<String> hgvsCDnaList) {
    HttpEntity<List<String>> entity = new HttpEntity<>(hgvsCDnaList);

    // workaround for https://bugs.openjdk.java.net/browse/JDK-8212586
    @SuppressWarnings("Convert2Diamond")
    ResponseEntity<List<HgvsTranslation>> response =
        this.restTemplate.exchange(
            uri, POST, entity, new ParameterizedTypeReference<List<HgvsTranslation>>() {});

    if (response.getStatusCode() != OK) {
      throw new UncheckedIOException(
          new IOException(format("request failed with status code %s", response.getStatusCode())));
    }

    return response.getBody();
  }

  private RestTemplate createRestTemplate(RestTemplateBuilder restTemplateBuilder) {
    RestTemplate localRestTemplate =
        restTemplateBuilder
            .defaultHeader(CONTENT_TYPE, APPLICATION_JSON.toString())
            .defaultHeader(ACCEPT, APPLICATION_JSON.toString())
            .messageConverters(singletonList(new MappingJackson2HttpMessageConverter()))
            .build();

    // workaround for translator service with missing response content type header
    List<ClientHttpRequestInterceptor> interceptors = localRestTemplate.getInterceptors();
    interceptors.add(
        (request, body, execution) -> {
          ClientHttpResponse response = execution.execute(request, body);
          if (response.getStatusCode() == OK) {
            HttpHeaders headers = response.getHeaders();
            if (headers.getContentType() == null) {
              headers.setContentType(APPLICATION_JSON);
            }
          }
          return response;
        });
    localRestTemplate.setInterceptors(interceptors);
    return localRestTemplate;
  }
}
