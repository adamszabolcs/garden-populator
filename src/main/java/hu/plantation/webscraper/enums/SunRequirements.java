package hu.plantation.webscraper.enums;

import javax.persistence.Entity;
import java.util.HashMap;
import java.util.Map;

@Entity(name = "Sun Requirements")
public enum SunRequirements {
    FULL_SUN("Full Sun"),
    FULL_SUN_PARTIAL_SHADE("Full Sun to Partial Shade"),
    PARTIAL_DAPPLED_SHADE("Partial or Dappled Shade"),
    PARTIAL_FULL_SHADE("Partial Shade to Full Shade"),
    FULL_SHADE("Full Shade");

    String value;

    // Reverse-lookup map for getting a sunRequirement from its value
    private static final Map<String, SunRequirements> lookup = new HashMap<>();

    static {
        for (SunRequirements d : SunRequirements.values()) {
            lookup.put(d.getValue(), d);
        }
    }

    SunRequirements(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SunRequirements get(String value) {
        return lookup.get(value);
    }
}
