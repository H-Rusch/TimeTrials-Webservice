package com.hrusch.webservice.model.combination;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.hrusch.webservice.exception.EnumDeserializationException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Glider {
  SUPER_GLIDER("Super Glider"),
  CLOUD_GLIDER("Cloud Glider"),
  WARIO_WING("Wario Wing"),
  WADDLE_WING("Waddle Wing"),
  PEACH_PARASOL("Peach Parasol"),
  PARACHUTE("Parachute"),
  PARAFOIL("Parafoil"),
  FLOWER_GLIDER("Flower Glider"),
  BOWSER_KITE("Bowser Kite"),
  PLANE_GLIDER("Plane Glider"),
  MKTV_PARAFOIL("MKTV Parafoil"),
  GOLD_GLIDER("Gold Glider"),
  HYLIAN_KITE("Hylian Kite"),
  PARAGLIDER("Paraglider"),
  PAPER_GLIDER("Paper Glider");

  private static final Map<String, Glider> mapping;

  static {
    mapping = new HashMap<>();
    Arrays.stream(Glider.values())
        .forEach(
            it -> {
              mapping.put(String.valueOf(it).toLowerCase(), it);
              mapping.put(it.getValue().toLowerCase(), it);
            });
  }

  @JsonValue
  private final String value;

  @JsonCreator
  public static Glider forValue(String value) throws JsonProcessingException {
    return Optional.ofNullable(mapping.get(value.toLowerCase()))
        .orElseThrow(() -> new EnumDeserializationException(value, Glider.class));
  }
}