package org.molgenis.mvl.converter;

import java.net.URI;
import java.util.List;
import org.molgenis.mvl.converter.model.HgvsTranslation;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.HttpServerErrorException;

public interface HgvsTranslatorClient {
  @Retryable(
      value = HttpServerErrorException.class,
      maxAttempts = 10,
      backoff = @Backoff(delay = 5000))
  List<HgvsTranslation> get(URI uri, List<String> hgvsCDnaList);
}
