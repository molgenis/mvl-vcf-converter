[![Build Status](https://travis-ci.org/molgenis/mvl-vcf-converter.svg?branch=master)](https://travis-ci.org/molgenis/mvl-vcf-converter)
[![Quality Status](https://sonarcloud.io/api/project_badges/measure?project=molgenis_mvl-vcf-converter&metric=alert_status)](https://sonarcloud.io/dashboard?id=molgenis_mvl-vcf-converter)
# Managed Variant List to VCF converter
Command-line application to convert managed variant list exports from Alissa Interpret to VCF using a [HGVS translator](https://github.com/molgenis/data-transform-vkgl#hgvs-translator) service.

## Requirements
- Java 11

## Usage
```
usage: java -jar mvl-vcf-converter.jar -i <arg> -t <arg> [-o <arg>] [-f]
       [-l] [-p] [-d]
 -i,--input <arg>        Input MVL file (.txt).
 -t,--translator <arg>   HGVS translator service URL.
 -o,--output <arg>       Output VCF file (.vcf or .vcf.gz).
 -f,--force              Override the output file if it already exists.
 -d,--debug              Enable debug mode (additional logging).

usage: java -jar mvl-vcf-converter.jar -v
 -v,--version   Print version.
```

## Examples
```
java -jar mvl-vcf-converter.jar -i mvl.txt -t http://localhost:1234/h2v -o out.vcf
java -jar mvl-vcf-converter.jar -i mvl.txt -t http://localhost:1234/h2v -o out.vcf.gz -f -d
java -jar mvl-vcf-converter.jar -v
```

## Managed Variant List
Tab-separated file with one classified variant per record.

### Example
See `src/test/resources/mvl.txt`

## Output VCF
Variant classifications in the MVL field.

### Example
see `src/test/resources/mvl.vcf`