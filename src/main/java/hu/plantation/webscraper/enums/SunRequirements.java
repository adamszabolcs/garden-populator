package hu.plantation.webscraper.enums;

import java.util.HashMap;
import java.util.Map;

public enum SunRequirements {
    FULL_SUN("Full Sun"),
    FULL_SUN_PARTIAL_SHADE("Full Sun to Partial Shade"),
    PARTIAL_DAPPLED_SHADE("Partial or Dappled Shade"),
    PARTIAL_FULL_SHADE("Partial Shade to Full Shade"),
    FULL_SHADE("Full Shade");

    String value;

    // Reverse-lookup map for getting a day from an abbreviation
    private static final Map<String, SunRequirements> lookup = new HashMap<String, SunRequirements>();

    static {
        for (SunRequirements d : SunRequirements.values()) {
            lookup.put(d.getValue(), d);
        }
    }

    private SunRequirements(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SunRequirements get(String value) {
        return lookup.get(value);
    }
}
