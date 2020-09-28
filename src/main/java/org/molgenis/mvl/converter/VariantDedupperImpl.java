package org.molgenis.mvl.converter;

import static com.google.common.collect.Maps.newHashMapWithExpectedSize;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.molgenis.mvl.converter.model.MvlVcfVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class VariantDedupperImpl implements VariantDedupper {
  private static final Logger LOGGER = LoggerFactory.getLogger(VariantDedupperImpl.class);

  @Override
  public List<MvlVcfVariant> dedup(List<MvlVcfVariant> mvlVcfVariants) {
    Map<String, MvlVcfVariant> dedupMap = newHashMapWithExpectedSize(mvlVcfVariants.size());
    Set<String> invalidDupMap = new LinkedHashSet<>();
    mvlVcfVariants.forEach(
        mvlVcfVariant -> {
          String key = getKey(mvlVcfVariant);
          MvlVcfVariant dupMvlVcfVariant = dedupMap.put(key, mvlVcfVariant);
          if (dupMvlVcfVariant != null
              && (mvlVcfVariant.getClassification() != dupMvlVcfVariant.getClassification())) {
            invalidDupMap.add(key);
          }
        });

    invalidDupMap.forEach(
        key -> {
          LOGGER.error(
              "skipping variant due to error '{}': duplicate variant with different classification",
              key);
          dedupMap.remove(key);
        });

    int nrDups = mvlVcfVariants.size() - dedupMap.size();
    if (nrDups > 0) {
      LOGGER.error("removed {} variant duplicate(s).", nrDups);
    }
    return new ArrayList<>(dedupMap.values());
  }

  private String getKey(MvlVcfVariant mvlVcfVariant) {
    return mvlVcfVariant.getChrom()
        + ':'
        + mvlVcfVariant.getPos()
        + " "
        + mvlVcfVariant.getRef()
        + " > "
        + mvlVcfVariant.getAlt();
  }
}
