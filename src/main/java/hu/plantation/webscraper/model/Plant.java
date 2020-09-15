package hu.plantation.webscraper.model;

import hu.plantation.webscraper.enums.SunRequirements;
import hu.plantation.webscraper.enums.WaterRequirements;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Builder
@Getter
@Entity(name = "Plant")
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
    @JoinTable(name = "Plant-Sun join", joinColumns = @JoinColumn(name = "plantid"))
    @Column(name = "sunid")
    @Enumerated(EnumType.ORDINAL)
    private Set<SunRequirements> sunReq;

    @ElementCollection(targetClass = WaterRequirements.class)
    @JoinTable(name = "Plant-Water join", joinColumns = @JoinColumn(name = "plantid"))
    @Column(name = "waterid")
    @Enumerated(EnumType.ORDINAL)
    private Set<WaterRequirements> watering;

    @Column(name = "height")
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
