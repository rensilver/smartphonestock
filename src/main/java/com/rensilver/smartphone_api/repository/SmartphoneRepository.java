package com.rensilver.smartphone_api.repository;

import com.rensilver.smartphone_api.entity.Smartphone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SmartphoneRepository extends JpaRepository<Smartphone, Long> {

    Optional<Smartphone> findByName(String name);
}
