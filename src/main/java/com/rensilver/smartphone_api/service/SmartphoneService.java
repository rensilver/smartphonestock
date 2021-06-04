package com.rensilver.smartphone_api.service;

import com.rensilver.smartphone_api.dto.SmartphoneDTO;
import com.rensilver.smartphone_api.entity.Smartphone;
import com.rensilver.smartphone_api.exception.SmartphoneAlreadyRegisteredException;
import com.rensilver.smartphone_api.exception.SmartphoneNotFoundException;
import com.rensilver.smartphone_api.exception.SmartphoneStockExceededException;
import com.rensilver.smartphone_api.mapper.SmartphoneMapper;
import com.rensilver.smartphone_api.repository.SmartphoneRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SmartphoneService {

    private final SmartphoneRepository smartphoneRepository;

    private final SmartphoneMapper smartphoneMapper = SmartphoneMapper.INSTANCE;

    public SmartphoneDTO createSmartphone(SmartphoneDTO smartphoneDTO) throws SmartphoneAlreadyRegisteredException {
        verifyIfIsAlreadyRegistered(smartphoneDTO.getName());
        Smartphone smartphone = smartphoneMapper.toModel(smartphoneDTO);
        Smartphone savedSmartphone = smartphoneRepository.save(smartphone);
        return smartphoneMapper.toDTO(savedSmartphone);
    }

    public List<SmartphoneDTO> findAll() {
        return smartphoneRepository.findAll()
                .stream()
                .map(smartphoneMapper::toDTO)
                .collect(Collectors.toList());
    }

    public SmartphoneDTO findByName(String name) throws SmartphoneNotFoundException {
        Smartphone foundSmartphone = smartphoneRepository.findByName(name)
                .orElseThrow(() -> new SmartphoneNotFoundException(name));
        return smartphoneMapper.toDTO(foundSmartphone);
    }

    public void deleteById(Long id) throws SmartphoneNotFoundException {
        verifyIfExists(id);
        smartphoneRepository.deleteById(id);
    }

    private void verifyIfIsAlreadyRegistered(String name) throws SmartphoneAlreadyRegisteredException {
        Optional<Smartphone> maybeSmartphoneSaved = smartphoneRepository.findByName(name);
        if(maybeSmartphoneSaved.isPresent()) {
            throw new SmartphoneAlreadyRegisteredException(name);
        }
    }

    private Smartphone verifyIfExists(Long id) throws SmartphoneNotFoundException {
        return smartphoneRepository.findById(id)
                .orElseThrow(() -> new SmartphoneNotFoundException(id));
    }

    public SmartphoneDTO increment(Long id, int quantityToIncrement) throws SmartphoneNotFoundException, SmartphoneStockExceededException {
        Smartphone smartphoneToIncrementStock = verifyIfExists(id);
        int quantityAfterIncrement = quantityToIncrement + smartphoneToIncrementStock.getQuantity();
        if (quantityAfterIncrement <= smartphoneToIncrementStock.getMax()) {
            smartphoneToIncrementStock.setQuantity(smartphoneToIncrementStock.getQuantity() + quantityToIncrement);
            Smartphone incrementedSmartphoneStock = smartphoneRepository.save(smartphoneToIncrementStock);
            return smartphoneMapper.toDTO(incrementedSmartphoneStock);
        }
        throw new SmartphoneStockExceededException(id, quantityToIncrement);
    }
}
