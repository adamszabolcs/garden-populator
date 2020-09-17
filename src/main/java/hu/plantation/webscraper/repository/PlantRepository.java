package hu.plantation.webscraper.repository;

import hu.plantation.webscraper.model.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlantRepository extends JpaRepository<Plant, UUID> {

    Plant findBySimpleName(String simpleName);

}
