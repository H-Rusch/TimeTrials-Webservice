package com.hrusch.timetrials.webservice.model.combination;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.hrusch.timetrials.webservice.exception.EnumDeserializationException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Tires {

  STANDARD("Standard"),
  MONSTER("Monster"),
  ROLLER("Roller"),
  SLIM("Slim"),
  SLICK("Slick"),
  METAL("Metal"),
  BUTTON("Button"),
  OFF_ROAD("Off-Road"),
  SPONGE("Sponge"),
  WOOD("Wood"),
  CUSHION("Cushion"),
  BLUE_STANDARD("Blue Standard"),
  HOT_MONSTER("Hot Monster"),
  AZURE_ROLLER("Azure Roller"),
  CRIMSON_SLIM("Crimson Slim"),
  CYBER_SLICK("Cyber Slick"),
  RETRO_OFF_ROAD("Retro Off-Road"),
  GOLD_TIRES("Gold Tires"),
  GLA_TIRES("GLA Tires"),
  TRIFORCE_TIRES("Triforce Tires"),
  ANCIENT_TIRES("Ancient Tires"),
  LEAF_TIRES("Leaf Tires");

  private static final Map<String, Tires> mapping;

  static {
    mapping = new HashMap<>();
    Arrays.stream(Tires.values())
        .forEach(
            it -> {
              mapping.put(String.valueOf(it).toLowerCase(), it);
              mapping.put(it.getValue().toLowerCase(), it);
            });
  }

  @JsonValue
  private final String value;

  @JsonCreator
  public static Tires forValue(String value) throws JsonProcessingException {
    return Optional.ofNullable(mapping.get(value.toLowerCase()))
        .orElseThrow(() -> new EnumDeserializationException(value, Tires.class));
  }
}