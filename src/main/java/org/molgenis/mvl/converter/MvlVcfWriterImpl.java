package org.molgenis.mvl.converter;

import static java.lang.String.join;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

import htsjdk.variant.variantcontext.Allele;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.variantcontext.VariantContextBuilder;
import htsjdk.variant.variantcontext.writer.VariantContextWriter;
import htsjdk.variant.vcf.VCFContigHeaderLine;
import htsjdk.variant.vcf.VCFHeader;
import htsjdk.variant.vcf.VCFHeaderLine;
import htsjdk.variant.vcf.VCFHeaderLineCount;
import htsjdk.variant.vcf.VCFHeaderLineType;
import htsjdk.variant.vcf.VCFHeaderVersion;
import htsjdk.variant.vcf.VCFInfoHeaderLine;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import org.molgenis.mvl.AppSettings;
import org.molgenis.mvl.converter.model.MvlVcfVariant;

public class MvlVcfWriterImpl implements MvlVcfWriter {

  private static final String CONTIG_ID = "ID";
  private static final String CONTIG_LENGTH = "length";
  private static final String CONTIG_ASSEMBLY = "assembly";
  private static final String ASSEMBLY = "human_g1k_v37_phiX.fasta";
  private static final String REFERENCE = "file:///apps/data/1000G/phase1/human_g1k_v37_phiX.fasta";
  private static final String HEADER_MVL_VERSION = "MVL_convertVersion";
  private static final String HEADER_MVL_ARGS = "MVL_convertCommand";
  private static final String INFO_MVL = "MVL";
  private static final String INFO_MVL_DESC =
      "Clinical significance: LB (likely benign), VUS (unknown significance), LP (likely pathogenic)";

  private final VariantDedupper variantDedupper;
  private final VariantContextWriter vcfWriter;
  private final AppSettings appSettings;
  private final VCFHeader vcfHeader;

  public MvlVcfWriterImpl(
      VariantDedupper variantDedupper, VariantContextWriter vcfWriter, AppSettings appSettings) {
    this.variantDedupper = variantDedupper;
    this.vcfWriter = requireNonNull(vcfWriter);
    this.appSettings = requireNonNull(appSettings);
    this.vcfHeader = createVcfHeader();
  }

  @Override
  public void writeHeader() {
    vcfWriter.writeHeader(vcfHeader);
  }

  @Override
  public void write(List<MvlVcfVariant> mvlVcfVariants) {
    if (mvlVcfVariants.isEmpty()) {
      return;
    }

    mvlVcfVariants = variantDedupper.dedup(mvlVcfVariants);
    List<VariantContext> variantContexts = convert(mvlVcfVariants);
    sort(variantContexts);
    variantContexts.forEach(vcfWriter::add);
  }

  private List<VariantContext> convert(List<MvlVcfVariant> mvlVcfVariants) {
    return mvlVcfVariants.stream().map(this::convert).collect(toList());
  }

  private void sort(List<VariantContext> variantContexts) {
    variantContexts.sort(vcfHeader.getVCFRecordComparator());
  }

  private VariantContext convert(MvlVcfVariant mvlVcfVariant) {
    int start = mvlVcfVariant.getPos();

    Allele refAllele = Allele.create(mvlVcfVariant.getRef(), true);
    Allele altAllele = Allele.create(mvlVcfVariant.getAlt(), false);
    List<Allele> alleles = List.of(refAllele, altAllele);

    return new VariantContextBuilder()
        .chr(mvlVcfVariant.getChrom())
        .start(start)
        .computeEndFromAlleles(alleles, start)
        .alleles(alleles)
        .attribute(INFO_MVL, List.of(mvlVcfVariant.getClassification().getId()))
        .make();
  }

  private VCFHeader createVcfHeader() {
    VCFHeader aVcfHeader = new VCFHeader();
    aVcfHeader.setVCFHeaderVersion(VCFHeaderVersion.VCF4_2);

    aVcfHeader.addMetaDataLine(new VCFHeaderLine("reference", REFERENCE));
    int idx = 0;
    aVcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("1", 249250621), idx++));
    aVcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("2", 243199373), idx++));
    aVcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("3", 198022430), idx++));
    aVcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("4", 191154276), idx++));
    aVcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("5", 180915260), idx++));
    aVcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("6", 171115067), idx++));
    aVcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("7", 159138663), idx++));
    aVcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("8", 146364022), idx++));
    aVcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("9", 141213431), idx++));
    aVcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("10", 135534747), idx++));
    aVcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("11", 135006516), idx++));
    aVcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("12", 133851895), idx++));
    aVcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("13", 115169878), idx++));
    aVcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("14", 107349540), idx++));
    aVcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("15", 102531392), idx++));
    aVcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("16", 90354753), idx++));
    aVcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("17", 81195210), idx++));
    aVcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("18", 78077248), idx++));
    aVcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("19", 59128983), idx++));
    aVcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("20", 63025520), idx++));
    aVcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("21", 48129895), idx++));
    aVcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("22", 51304566), idx++));
    aVcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("X", 155270560), idx++));
    aVcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("Y", 59373566), idx++));
    aVcfHeader.addMetaDataLine(new VCFContigHeaderLine(createContigMap("MT", 16569), idx));

    aVcfHeader.addMetaDataLine(new VCFHeaderLine(HEADER_MVL_VERSION, appSettings.getVersion()));
    aVcfHeader.addMetaDataLine(
        new VCFHeaderLine(HEADER_MVL_ARGS, join(" ", appSettings.getArgs())));

    aVcfHeader.addMetaDataLine(
        new VCFInfoHeaderLine(
            INFO_MVL, VCFHeaderLineCount.A, VCFHeaderLineType.String, INFO_MVL_DESC));

    return aVcfHeader;
  }

  private LinkedHashMap<String, String> createContigMap(String chrom, int length) {
    LinkedHashMap<String, String> contigMap = new LinkedHashMap<>();
    contigMap.put(CONTIG_ID, chrom);
    contigMap.put(CONTIG_LENGTH, String.valueOf(length));
    contigMap.put(CONTIG_ASSEMBLY, ASSEMBLY);
    return contigMap;
  }

  @Override
  public void close() {
    vcfWriter.close();
  }

  private static class DedupMvlVcfVariant {
    private final MvlVcfVariant mvlVcfVariant;
    private final String chrom;
    private final int pos;
    private final String ref;
    private final String alt;

    DedupMvlVcfVariant(MvlVcfVariant mvlVcfVariant) {
      this.mvlVcfVariant = requireNonNull(mvlVcfVariant);
      chrom = mvlVcfVariant.getChrom();
      pos = mvlVcfVariant.getPos();
      ref = mvlVcfVariant.getRef();
      alt = mvlVcfVariant.getAlt();
    }

    public MvlVcfVariant getMvlVcfVariant() {
      return mvlVcfVariant;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      DedupMvlVcfVariant that = (DedupMvlVcfVariant) o;
      return pos == that.pos
          && chrom.equals(that.chrom)
          && ref.equals(that.ref)
          && alt.equals(that.alt);
    }

    @Override
    public int hashCode() {
      return Objects.hash(chrom, pos, ref, alt);
    }
  }
}
