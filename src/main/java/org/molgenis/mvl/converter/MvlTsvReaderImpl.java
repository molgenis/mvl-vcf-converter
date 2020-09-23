package org.molgenis.mvl.converter;

import static java.util.Objects.requireNonNull;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import java.io.Reader;
import java.util.List;
import org.molgenis.mvl.converter.model.MvlTsvVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MvlTsvReaderImpl implements MvlTsvReader {
  private static final Logger LOGGER = LoggerFactory.getLogger(MvlTsvReaderImpl.class);

  private final Reader reader;

  public MvlTsvReaderImpl(Reader reader) {
    this.reader = requireNonNull(reader);
  }

  @Override
  public List<MvlTsvVariant> read() {
    CsvToBean<MvlTsvVariant> csvToBean =
        new CsvToBeanBuilder<MvlTsvVariant>(reader)
            .withSeparator('\t')
            .withType(MvlTsvVariant.class)
            .withThrowExceptions(false)
            .build();
    List<MvlTsvVariant> mvlTsvVariants = csvToBean.parse();
    List<CsvException> csvExceptions = csvToBean.getCapturedExceptions();

    csvExceptions.forEach(
        csvException ->
            LOGGER.error(
                "skipping variant due to error '{}:{}': line #{} {}",
                csvException.getLine()[2],
                csvException.getLine()[3],
                csvException.getLineNumber(),
                csvException.getMessage()));
    return mvlTsvVariants;
  }

  @Override
  public void close() throws Exception {
    reader.close();
  }
}
