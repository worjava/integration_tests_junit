package net.testing.test.repository;

import net.testing.test.entity.DeveloperEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeveloperRepository extends JpaRepository<DeveloperEntity, Integer> {

    DeveloperEntity findByEmail(String email);

    @Query("SELECT de from DeveloperEntity  de where de.status= 'ACTIVE' and de.specialty=?1")
    List<DeveloperEntity> findByAllActiveBySpecialty(String specialty);

}
