package com.rensilver.smartphone_api.repository;

import com.rensilver.smartphone_api.entity.Smartphone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SmartphoneRepository extends JpaRepository<Smartphone, Long> {

    Optional<Smartphone> findByName(String name);
}
