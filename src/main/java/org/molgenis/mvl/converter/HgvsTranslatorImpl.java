package org.molgenis.mvl.converter;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

import com.google.common.collect.Lists;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.molgenis.mvl.converter.model.HgvsTranslation;
import org.molgenis.mvl.converter.model.MvlTsvVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UriComponentsBuilder;

public class HgvsTranslatorImpl implements HgvsTranslator {
  private static final Logger LOGGER = LoggerFactory.getLogger(HgvsTranslatorImpl.class);

  private static final int BATCH_SIZE = 10;

  private final HgvsTranslatorClient hgvsTranslatorClient;
  private final URI uri;

  public HgvsTranslatorImpl(HgvsTranslatorClient hgvsTranslatorClient, URI uri) {
    this.hgvsTranslatorClient = requireNonNull(hgvsTranslatorClient);
    this.uri = configureUri(requireNonNull(uri));
  }

  @Override
  public List<HgvsTranslation> translate(List<MvlTsvVariant> mvlVariants) {
    if (mvlVariants.isEmpty()) {
      return List.of();
    }

    AtomicInteger count = new AtomicInteger(0);

    List<HgvsTranslation> hgvsTranslations = new ArrayList<>(mvlVariants.size());
    Lists.partition(mvlVariants, BATCH_SIZE)
        .forEach(
            batch -> {
              List<HgvsTranslation> batchRecords = convertBatch(batch);
              hgvsTranslations.addAll(batchRecords);

              count.getAndAdd(BATCH_SIZE);
              if (count.get() % (BATCH_SIZE * 10) == 0) {
                LOGGER.info("translated {} records", count.get());
              }
            });
    return hgvsTranslations;
  }

  private List<HgvsTranslation> convertBatch(List<MvlTsvVariant> mvlVariantBatch) {
    List<String> hgvsCDnaList =
        mvlVariantBatch.stream().map(HgvsTranslatorImpl::toHgvsCDna).collect(toList());
    return hgvsTranslatorClient.get(uri, hgvsCDnaList);
  }

  private static String toHgvsCDna(MvlTsvVariant mvlVariant) {
    return mvlVariant.getTranscript() + ':' + mvlVariant.getCDna();
  }

  private static URI configureUri(URI baseUri) {
    UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromUriString(baseUri.toString());
    // workaround for https://github.com/molgenis/data-transform-vkgl/issues/18
    urlBuilder.replaceQueryParam("keep_left_anchor", "True");
    return urlBuilder.build().toUri();
  }
}
