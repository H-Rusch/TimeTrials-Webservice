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
public enum Driver {

  MARIO("Mario"),
  LUIGI("Luigi"),
  PEACH("Peach"),
  DAISY("Daisy"),
  ROSALINA("Rosalina"),
  TANOOKI_MARIO("Tanooki Mario"),
  CAT_PEACH("Cat Peach"),
  BIRDO("Birdo"),
  YOSHI("Yoshi"),
  TOAD("Toad"),
  KOOPA_TROOPA("Koopa Troopa"),
  SHY_GUY("Shy Guy"),
  LAKITU("Lakitu"),
  TOADETTE("Toadette"),
  KING_BOO("King Boo"),
  PETEY_PIRANHA("Petey Piranha"),
  BABY_MARIO("Baby Mario"),
  BABY_LUIGI("Baby Luigi"),
  BABY_PEACH("Baby Peach"),
  BABY_DAISY("Baby Daisy"),
  BABY_ROSALINA("Baby Rosalina"),
  METAL_MARIO("Metal Mario"),
  PINK_GOLD_PEACH("Pink Gold Peach"),
  WIGGLER("Wiggler"),
  WARIO("Wario"),
  WALUIGI("Waluigi"),
  DONKEY_KONG("Donkey Kong"),
  BOWSER("Bowser"),
  DRY_BONES("Dry Bones"),
  BOWSER_JR("Bowser Jr."),
  DRY_BOWSER("Dry Bowser"),
  KAMEK("Kamek"),
  LEMMY("Lemmy"),
  LARRY("Larry"),
  WENDY("Wendy"),
  LUDWIG("Ludwig"),
  IGGY("Iggy"),
  ROY("Roy"),
  MORTON("Morton"),
  PEACHETTE("Peachette"),
  INKLING_GIRL("Inkling Girl"),
  INKLING_BOY("Inkling Boy"),
  VILLAGER_MALE("Villager Male"),
  VILLAGER_FEMALE("Villager Female"),
  ISABELLE("Isabelle"),
  LINK("Link"),
  DIDDY_KONG("Diddy Kong"),
  FUNKY_KONG("Funky Kong"),
  PAULINE("Pauline"),
  MII_LIGHT("Mii Light"),
  MII_MEDIUM("Mii Medium"),
  MII_HEAVY("Mii Heavy");

  private static final Map<String, Driver> mapping;

  static {
    mapping = new HashMap<>();
    Arrays.stream(Driver.values())
        .forEach(
            it -> {
              mapping.put(String.valueOf(it).toLowerCase(), it);
              mapping.put(it.getName().toLowerCase(), it);
            });
  }

  @JsonValue
  private final String name;

  @JsonCreator
  public static Driver forValue(String value) throws JsonProcessingException {
    return Optional.ofNullable(mapping.get(value.toLowerCase()))
        .orElseThrow(() -> new EnumDeserializationException(value, Driver.class));
  }
}