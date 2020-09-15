package hu.plantation.webscraper.enums;

import javax.persistence.Entity;
import java.util.HashMap;
import java.util.Map;

@Entity(name = "Watering")
public enum WaterRequirements {
    IN_WATER("In Water"),
    WET("Wet"),
    WET_MESIC("Wet Mesic"),
    MESIC("Mesic"),
    DRY_MESIC("Dry Mesic"),
    DRY("Dry");

    String value;
    // Reverse-lookup map for getting a waterRequirement from its value
    private static final Map<String, WaterRequirements> lookup = new HashMap<>();

    static {
        for (WaterRequirements d : WaterRequirements.values()) {
            lookup.put(d.getValue(), d);
        }
    }

    WaterRequirements(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


    public static WaterRequirements get(String value) {
        return lookup.get(value);
    }

}
