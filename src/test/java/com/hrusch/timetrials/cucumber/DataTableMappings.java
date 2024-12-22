package com.hrusch.timetrials.cucumber;

import io.cucumber.java.DataTableType;
import java.util.Map;

public class DataTableMappings {

  @DataTableType
  public TrackTimeEntry mapToTrackTimeEntry(Map<String, String> row) {
    return new TrackTimeEntry(row.get("Track"), row.get("File"));
  }

  public record TrackTimeEntry(String track, String file) {

  }
}
