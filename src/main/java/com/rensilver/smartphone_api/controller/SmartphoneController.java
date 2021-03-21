package com.rensilver.smartphone_api.controller;

import com.rensilver.smartphone_api.dto.QuantityDTO;
import com.rensilver.smartphone_api.dto.SmartphoneDTO;
import com.rensilver.smartphone_api.exception.SmartphoneAlreadyRegisteredException;
import com.rensilver.smartphone_api.exception.SmartphoneNotFoundException;
import com.rensilver.smartphone_api.exception.SmartphoneStockExceededException;
import com.rensilver.smartphone_api.service.SmartphoneService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/smartphones")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SmartphoneController {

    private final SmartphoneService smartphoneService;

    @GetMapping
    public List<SmartphoneDTO> findAll(){
        return smartphoneService.findAll();
    }

    @GetMapping("/{id}")
    public SmartphoneDTO findById(@PathVariable Long id) throws SmartphoneNotFoundException {
        return smartphoneService.findById(id);
    }

    @GetMapping("/{name}")
    public SmartphoneDTO findByName(@PathVariable String name) throws SmartphoneNotFoundException {
        return smartphoneService.findByName(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SmartphoneDTO createSmartphone(@RequestBody @Valid SmartphoneDTO smartphoneDTO) throws SmartphoneAlreadyRegisteredException {
        return smartphoneService.createSmartphone(smartphoneDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) throws SmartphoneNotFoundException {
        smartphoneService.deleteById(id);
    }

    @PatchMapping("/{id}/increment")
    public SmartphoneDTO increment(@PathVariable Long id, @RequestBody @Valid QuantityDTO quantityDTO) throws SmartphoneStockExceededException, SmartphoneNotFoundException {
        return smartphoneService.increment(id, quantityDTO.getQuantity());
    }
}
