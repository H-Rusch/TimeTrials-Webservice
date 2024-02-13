package com.hrusch.webapp.util;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import lombok.SneakyThrows;

public class FileReader {

  private static final String TESTDATA_ROOT = "/testdata/";

  @SneakyThrows
  public String readFileToString(String filepath) {
    URL url = getClass().getResource(TESTDATA_ROOT + filepath);

    if (Objects.isNull(url)) {
      throw new IOException(filepath);
    }

    return Files.readString(Path.of(url.getPath()));
  }
}
