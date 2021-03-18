package com.rensilver.smartphone_api.service;

import com.rensilver.smartphone_api.dto.SmartphoneDTO;
import com.rensilver.smartphone_api.entity.Smartphone;
import com.rensilver.smartphone_api.mapper.SmartphoneMapper;
import com.rensilver.smartphone_api.repository.SmartphoneRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SmartphoneService {

    private final SmartphoneRepository smartphoneRepository;

    private final SmartphoneMapper smartphoneMapper = SmartphoneMapper.INSTANCE;

    public List<SmartphoneDTO> findAll() {
        return smartphoneRepository.findAll()
                .stream()
                .map(smartphoneMapper::toDTO)
                .collect(Collectors.toList());
    }

    public SmartphoneDTO findById(Long id) {
        Smartphone foundSmartphone = smartphoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException());
        return smartphoneMapper.toDTO(foundSmartphone);
    }

    public SmartphoneDTO findByName(String name) {
        Smartphone foundSmartphone = smartphoneRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException());
        return smartphoneMapper.toDTO(foundSmartphone);
    }
}
