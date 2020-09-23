package org.molgenis.mvl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.SpringApplication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.ResourceUtils;

@ActiveProfiles("test")
class AppIT {

  private static final Pattern HEADER_VERSION_PATTERN =
      Pattern.compile("^##MVL_convertCommand=.*?$", Pattern.MULTILINE);
  private static final Pattern HEADER_COMMAND_PATTERN =
      Pattern.compile("^##MVL_convertVersion=.*?$", Pattern.MULTILINE);

  @TempDir Path sharedTempDir;

  @Test
  void test() throws IOException {
    String inputFile = ResourceUtils.getFile("classpath:mvl.txt").toString();
    String translatorServiceUri = "http://mocked";
    String outputFile = sharedTempDir.resolve("mvl.vcf").toString();

    String[] args = {"-i", inputFile, "-t", translatorServiceUri, "-o", outputFile};
    SpringApplication springApplication = new SpringApplication(MockApp.class);
    springApplication.setAllowBeanDefinitionOverriding(true);
    springApplication.run(args);

    String outputVcf = Files.readString(Path.of(outputFile));

    // output differs every run (different tmp dir)
    outputVcf = HEADER_VERSION_PATTERN.matcher(outputVcf).replaceAll("##MVL_convertVersion=");
    outputVcf = HEADER_COMMAND_PATTERN.matcher(outputVcf).replaceAll("##MVL_convertCommand=");

    Path expectedOutputFile = ResourceUtils.getFile("classpath:mvl.vcf").toPath();
    String expectedOutputVcf = Files.readString(expectedOutputFile).replaceAll("\\R", "\n");

    assertEquals(expectedOutputVcf, outputVcf);
  }
}
