package hu.plantation.webscraper.model;

import hu.plantation.webscraper.enums.SunRequirements;
import hu.plantation.webscraper.enums.WaterRequirements;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collection;
import java.util.UUID;

@Builder
@Getter
@Entity(name = "plant")
public class Plant {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "simplename")
    private String simpleName;

    @Column(name = "scientificname", unique = true)
    private String scientificName;

    @ElementCollection(targetClass = SunRequirements.class)
    @JoinTable(name = "plantsun", joinColumns = @JoinColumn(name = "plant_id"))
    @Column(name = "value")
    @Enumerated(EnumType.STRING)
    private Collection<SunRequirements> sun;

    @ElementCollection(targetClass = WaterRequirements.class)
    @JoinTable(name = "plantwater", joinColumns = @JoinColumn(name = "plant_id"))
    @Column(name = "value")
    @Enumerated(EnumType.STRING)
    private Collection<WaterRequirements> watering;

    @Column(name = "height")
    private String height;

    @Override
    public String toString() {
        return "Plant{" +
                "simpleName='" + simpleName + '\'' +
                ", scientificName='" + scientificName + '\'' +
                ", sunReq=" + sun +
                ", watering=" + watering +
                ", height='" + height + '\'' +
                '}';
    }
}
