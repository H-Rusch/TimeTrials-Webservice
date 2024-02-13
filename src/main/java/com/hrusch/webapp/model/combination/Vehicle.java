package com.hrusch.webapp.model.combination;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.hrusch.webapp.exception.EnumDeserializationException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Vehicle {
  STANDARD_KART("Standard Kart"),
  PIPE_FRAME("Pipe Frame"),
  MACH_8("Mach 8"),
  STEEL_DRIVER("Steel Driver"),
  CAT_CRUISER("Cat Cruiser"),
  CIRCUIT_SPECIAL("Circuit Special"),
  TRI_SPEEDER("Tri-Speeder"),
  BADWAGON("Badwagon"),
  PRANCER("Prancer"),
  BIDDYBUGGY("Biddybuggy"),
  LANDSHIP("Landship"),
  SNEEKER("Sneeker"),
  SPORTS_COUPE("Sports Coupe"),
  GOLD_STANDARD("Gold Standard"),
  GLA("GLA"),
  W_25_SILVER_ARROW("W 25 Silver Arrow"),
  SL_ROADSTER("300 SL Roadster"),
  BLUE_FALCON("Blue Falcon"),
  TANOOKI_KART("Tanooki Kart"),
  B_DASHER("B Dasher"),
  STREETLE("Streetle"),
  P_WING("P-Wing"),
  KOOPA_CLOWN("Koopa Clown"),
  STANDARD_BIKE("Standard Bike"),
  THE_DUKE("The Duke"),
  FLAME_RIDER("Flame Rider"),
  VARMINT("Varmint"),
  MR_SCOOTY("Mr. Scooty"),
  MASTER_CYCLE_ZERO("Master Cycle Zero"),
  CITY_TRIPPER("City Tripper"),
  COMET("Comet"),
  SPORT_BIKE("Sport Bike"),
  JET_BIKE("Jet Bike"),
  YOSHI_BIKE("Yoshi Bike"),
  MASTER_CYCLE("Master Cycle"),
  STANDARD_ATV("Standard Atv"),
  WILD_WIGGLER("Wild Wiggler"),
  TEDDY_BUGGY("Teddy Buggy"),
  BONE_RATTLER("Bone Rattler"),
  SPLAY_BUGGY("Splay Buggy"),
  INKSTRIDER("Inkstrider");

  private static final Map<String, Vehicle> mapping;

  static {
    mapping = new HashMap<>();
    Arrays.stream(Vehicle.values()).forEach(it -> {
      mapping.put(String.valueOf(it).toLowerCase(), it);
      mapping.put(it.getValue().toLowerCase(), it);
    });
  }

  @JsonValue
  private final String value;

  @JsonCreator
  public static Vehicle forValue(String value) throws JsonProcessingException {
    return Optional.ofNullable(mapping.get(value.toLowerCase()))
        .orElseThrow(() -> new EnumDeserializationException(value, Vehicle.class));
  }
}