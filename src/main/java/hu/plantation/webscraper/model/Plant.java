package hu.plantation.webscraper.model;

import hu.plantation.webscraper.enums.SunRequirements;
import hu.plantation.webscraper.enums.WaterRequirements;
import lombok.*;

import java.util.List;

@Builder
@Getter
public class Plant {

    private String simpleName;
    private String scientificName;
    private List<SunRequirements> sunReq;
    private List<WaterRequirements> watering;
    private String height;

    @Override
    public String toString() {
        return "Plant{" +
                "simpleName='" + simpleName + '\'' +
                ", scientificName='" + scientificName + '\'' +
                ", sunReq=" + sunReq +
                ", watering=" + watering +
                ", height='" + height + '\'' +
                '}';
    }
}
