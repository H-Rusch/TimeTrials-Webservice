package com.hrusch.webapp.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.hrusch.webapp.exception.EnumDeserializationException;
import com.hrusch.webapp.model.combination.Tires;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
public enum Track {
    MARIO_KART_STADIUM("Mario Kart Stadium"),
    WATER_PARK("Water Park"),
    SWEET_SWEET_CANYON("Sweet Sweet Canyon"),
    THWOMP_RUINS("Thwomp Ruins"),
    MARIO_CIRCUIT("Mario Circuit"),
    TOAD_HARBOR("Toad Harbor"),
    TWISTED_MANSION("Twisted Mansion"),
    SHY_GUY_FALLS("Shy Guy Falls"),
    SUNSHINE_AIRPORT("Sunshine Airport"),
    DOLPHIN_SHOALS("Dolphin Shoals"),
    ELECTRODROME("Electrodrome"),
    MOUNT_WARIO("Mount Wario"),
    CLOUDTOP_CRUISE("Cloudtop Cruise"),
    BONEDRY_DUNES("Bone-Dry Dunes"),
    BOWSERS_CASTLE("Bowser's Castle"),
    RAINBOW_ROAD("Rainbow Road"),
    MOO_MOO_MEADOWS_WII("Wii Moo Moo Meadows"),
    MARIO_CIRCUIT_GBA("GBA Mario Circuit"),
    CHEEP_CHEEP_BEACH_DS("DS Cheep Cheep Beach"),
    TOADS_TURNPIKE_N64("N64 Toad's Turnpike"),
    DRY_DRY_DESERT_GCN("GCN Dry Dry Desert"),
    DONUT_PLAINS_3_SNES("SNES Donut Plains 3"),
    ROYAL_RACEWAY_N64("N64 Royal Raceway"),
    DK_JUNGLE_3DS("3DS DK Jungle"),
    WARIO_STADIUM_DS("DS Wario Stadium"),
    SHERBET_LAND_GCN("GCN Sherbet Land"),
    MUSIC_PARK_3DS("3DS Music Park"),
    YOSHI_VALLEY_N64("N64 Yoshi Valley"),
    TICKTOCK_CLOCK_DS("DS Tick-Tock Clock"),
    PIRANHA_PLANT_SLIDE_3DS("3DS Piranha Plant Slide"),
    GRUMBLE_VOLCANO_WII("Wii Grumble Volcano"),
    RAINBOW_ROAD_N64("N64 Rainbow Road"),
    YOSHI_CIRCUIT_GCN("GCN Yoshi Circuit"),
    EXCITEBIKE_ARENA("Excitebike Arena"),
    DRAGON_DRIFTWAY("Dragon Driftway"),
    MUTE_CITY("Mute City"),
    WARIOS_GOLD_MINE_WII("Wii Wario's Gold Mine"),
    RAINBOW_ROAD_SNES("SNES Rainbow Road"),
    ICE_ICE_OUTPOST("Ice Ice Outpost"),
    HYRULE_CIRCUIT("Hyrule Circuit"),
    BABY_PARK_GCN("GCN Baby Park"),
    CHEESE_LAND_GBA("GBA Cheese Land"),
    WILD_WOODS("Wild Woods"),
    ANIMAL_CROSSING("Animal Crossing"),
    NEO_BOWSER_CITY_3DS("3DS Neo Bowser City"),
    RIBBON_ROAD_GBA("GBA Ribbon Road"),
    SUPER_BELL_SUBWAY("Super Bell Subway"),
    BIG_BLUE("Big Blue"),
    PARIS_PROMENADE_TOUR("Tour Paris Promenade"),
    TOAD_CIRCUIT_3DS("3DS Toad Circuit"),
    CHOCO_MOUNTAIN_N64("N64 Choco Mountain"), WII_COCONUT_MALL("Wii Coconut Mall"),
    TOKYO_BLUR_TOUR("Tour Tokyo Blur"),
    SHROOM_RIDGE_DS("DS Shroom Ridge"),
    SKY_GARDEN_GBA("GBA Sky Garden"),
    NINJA_HIDEAWAY_TOUR("Tour Ninja Hideaway"),
    NEW_YORK_MINUTE_TOUR("Tour New York Minute"),
    MARIO_CIRCUIT_3_SNES("SNES Mario Circuit 3"),
    KALAMARI_DESERT_N64("N64 Kalamari Desert"),
    WALUIGI_PINBALL_DS("DS Waluigi Pinball"),
    SYDNEY_SPRINT_TOUR("Tour Sydney Sprint"),
    SNOW_LAND_GBA("GBA Snow Land"),
    MUSHROOM_GORGE_WII("Wii Mushroom Gorge"),
    SKYHIGH_SUNDAE("Sky-High Sundae"),
    LONDON_LOOP_TOUR("Tour London Loop"),
    BOO_LAKE_GBA("GBA Boo Lake"),
    ROCK_ROCK_MOUNTAIN_3DS("3DS Rock Rock Mountain"),
    MAPLE_TREEWAY_WII("Wii Maple Treeway"),
    BERLIN_BYWAYS_TOUR("Tour Berlin Byways"),
    PEACH_GARDENS_DS("DS Peach Gardens"),
    MERRY_MOUNTAIN_TOUR("Tour Merry Mountain"),
    RAINBOW_ROAD_3DS("3DS Rainbow Road"),
    AMSTERDAM_DRIFT_TOUR("Tour Amsterdam Drift"),
    RIVERSIDE_PARK_GBA("GBA Riverside Park"),
    DK_SUMMIT_WII("Wii DK Summit"),
    YOSHIS_ISLAND("Yoshi's Island"),
    BANGKOK_RUSH_TOUR("Tour Bangkok Rush"),
    MARIO_CIRCUIT_DS("DS Mario Circuit"),
    WALUIGI_STADIUM_GCN("GCN Waluigi Stadium"),
    SINGAPORE_SPEEDWAY_TOUR("Tour Singapore Speedway"),
    ATHENS_DASH_TOUR("Tour Athens Dash"),
    DAISY_CRUISER_GCN("GCN Daisy Cruiser"),
    MOONVIEW_HIGHWAY_WII("Wii Moonview Highway"),
    SQUEAKY_CLEAN_SPRINT("Squeaky Clean Sprint"),
    TOUR_LOS_ANGELES_LAPS_TOUR("Tour Los Angeles Laps"),
    SUNSET_WILDS_GBA("GBA Sunset Wilds"),
    KOOPA_CAPE_WII("Wii Koopa Cape"),
    VANCOUVER_VELOCITY_TOUR("Tour Vancouver Velocity"),
    ROME_AVANTI_TOUR("Tour Rome Avanti"),
    DK_MOUNTAIN_GCN("GCN DK Mountain"),
    DAISY_CIRCUIT_WII("Wii Daisy Circuit"),
    PIRANHA_PLANT_COVE("Piranha Plant Cove"),
    MADRID_DRIVE_TOUR("Tour Madrid Drive"),
    ROSALINAS_ICE_WORLD_3DS("3DS Rosalina's Ice World"),
    BOWER_CASTLE_3_SNES("SNES Bower Castle 3"),
    RAINBOW_ROAD_WII("Wii Rainbow Road");

    @JsonValue
    private final String name;

    private static final Map<String, Track> mapping;

    static {
        mapping = new HashMap<>();
        Arrays.stream(Track.values()).forEach(track -> {
            mapping.put(String.valueOf(track).toLowerCase(), track);
            mapping.put(track.getName().toLowerCase(), track);
        });
    }

    @JsonCreator
    public static Track forValue(String value) throws JsonProcessingException {
        return Optional.ofNullable(mapping.get(value.toLowerCase()))
            .orElseThrow(() -> new EnumDeserializationException(value, Track.class));
    }
}
