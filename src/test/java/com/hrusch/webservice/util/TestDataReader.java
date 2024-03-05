package com.hrusch.webservice.util;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.SneakyThrows;

public class TestDataReader {

  private static final String TESTDATA_ROOT = "testdata";

  @SneakyThrows
  public static String readFileToString(String directory, String filename) {
    URL url = TestDataReader.class.getResource(
        String.join("/", "", TESTDATA_ROOT, directory, filename));

    if (Objects.isNull(url)) {
      throw new IOException(filename);
    }

    return Files.readString(Path.of(url.getPath()));
  }

  @SneakyThrows
  public static List<String> readAllFilesInDirectory(String directory) {
    URL url = TestDataReader.class.getResource(
        String.join("/", "", TESTDATA_ROOT, directory));
    Path resourcePath = Paths.get(Objects.requireNonNull(url).toURI());

    try (Stream<Path> filesStream = Files.list(resourcePath)) {
      return filesStream.filter(Files::isRegularFile)
          .map(Path::getFileName)
          .map(filename -> readFileToString(directory, String.valueOf(filename)))
          .toList();
    }
  }
}
