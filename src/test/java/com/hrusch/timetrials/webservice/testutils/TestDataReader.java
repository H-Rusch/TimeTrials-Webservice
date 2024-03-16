package com.hrusch.timetrials.webservice.testutils;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
public class TestDataReader {

  private final Path BASE_DIRECTORY;

  public TestDataReader(String... baseDirectory) {
    Path pathToGet = Paths.get("/", baseDirectory);
    URL url = getClass().getResource(pathToGet.toString());

    if (Objects.isNull(url)) {
      throw new RuntimeException("problems with the path " + pathToGet);
    }

    this.BASE_DIRECTORY = pathToGet;
  }

  @SneakyThrows
  public String readFileToString(String filename) {
    Path pathToGet = BASE_DIRECTORY.resolve(Path.of(filename));
    URL url = getClass().getResource(pathToGet.toString());

    if (Objects.isNull(url)) {
      throw new IOException(pathToGet.toString());
    }

    return readFileToString(Path.of(url.getPath()));
  }

  @SneakyThrows
  public List<String> readAllFilesInDirectory() {
    URL url = getClass().getResource(BASE_DIRECTORY.toString());

    if (Objects.isNull(url)) {
      throw new IOException(BASE_DIRECTORY.toString());
    }

    try (Stream<Path> filesStream = Files.list(Paths.get(url.toURI()))) {
      return filesStream
          .filter(Files::isRegularFile)
          .map(this::readFileToString)
          .toList();
    }
  }

  @SneakyThrows
  private String readFileToString(Path path) {
    return Files.readString(path);
  }
}
