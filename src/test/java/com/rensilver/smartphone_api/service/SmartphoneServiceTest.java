package com.rensilver.smartphone_api.service;

import com.rensilver.smartphone_api.builder.SmartphoneDTOBuilder;
import com.rensilver.smartphone_api.dto.SmartphoneDTO;
import com.rensilver.smartphone_api.entity.Smartphone;
import com.rensilver.smartphone_api.exception.SmartphoneAlreadyRegisteredException;
import com.rensilver.smartphone_api.exception.SmartphoneNotFoundException;
import com.rensilver.smartphone_api.exception.SmartphoneStockExceededException;
import com.rensilver.smartphone_api.mapper.SmartphoneMapper;
import com.rensilver.smartphone_api.repository.SmartphoneRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SmartphoneServiceTest {

    private static final long INVALID_SMARTPHONE_ID = 1L;

    @Mock
    private SmartphoneRepository smartphoneRepository;

    private SmartphoneMapper smartphoneMapper = SmartphoneMapper.INSTANCE;

    @InjectMocks
    private SmartphoneService smartphoneService;

    @Test
    void whenSmartphoneInformedThenItShouldBeCreated() throws SmartphoneAlreadyRegisteredException {
        // given
        SmartphoneDTO expectedSmartphoneDTO = SmartphoneDTOBuilder.builder().build().toSmartphoneDTO();
        Smartphone expectedSavedSmartphone = smartphoneMapper.toModel(expectedSmartphoneDTO);

        // when
        when(smartphoneRepository.findByName(expectedSmartphoneDTO.getName())).thenReturn(Optional.empty());
        when(smartphoneRepository.save(expectedSavedSmartphone)).thenReturn(expectedSavedSmartphone);

        // then
        SmartphoneDTO createdSmartphoneDTO = smartphoneService.createSmartphone(expectedSmartphoneDTO);

        assertThat(createdSmartphoneDTO.getId(), is(equalTo(expectedSmartphoneDTO.getId())));
        assertThat(createdSmartphoneDTO.getName(), is(equalTo(expectedSmartphoneDTO.getName())));
        assertThat(createdSmartphoneDTO.getQuantity(), is(equalTo(expectedSmartphoneDTO.getQuantity())));
    }

    @Test
    void whenAlreadyRegisteredSmartphoneInformedThenAnExceptionShouldBeThrown() {
        // given
        SmartphoneDTO expectedSmartphoneDTO = SmartphoneDTOBuilder.builder().build().toSmartphoneDTO();
        Smartphone duplicatedSmartphone = smartphoneMapper.toModel(expectedSmartphoneDTO);

        // when
        when(smartphoneRepository.findByName(expectedSmartphoneDTO.getName())).thenReturn(Optional.of(duplicatedSmartphone));

        // then
        assertThrows(SmartphoneAlreadyRegisteredException.class, () -> smartphoneService.createSmartphone(expectedSmartphoneDTO));
    }

    @Test
    void whenValidSmartphoneNameIsGivenThenReturnASmartphone() throws SmartphoneNotFoundException {
        // given
        SmartphoneDTO expectedSmartphoneDTO = SmartphoneDTOBuilder.builder().build().toSmartphoneDTO();
        Smartphone expectedSmartphone = smartphoneMapper.toModel(expectedSmartphoneDTO);

        //when
        when(smartphoneRepository.findByName(expectedSmartphoneDTO.getName())).thenReturn(Optional.of(expectedSmartphone));

        //then
        SmartphoneDTO foundSmartphoneDTO = smartphoneService.findByName(expectedSmartphoneDTO.getName());

        assertThat(foundSmartphoneDTO, is(equalTo(expectedSmartphoneDTO)));
    }

    @Test
    void whenNotRegisteredSmartphoneNameIsGivenThenThrowAnException() {
        // given
        SmartphoneDTO expectedSmartphoneDTO = SmartphoneDTOBuilder.builder().build().toSmartphoneDTO();

        //when
        when(smartphoneRepository.findByName(expectedSmartphoneDTO.getName())).thenReturn(Optional.empty());

        //then
        assertThrows(SmartphoneNotFoundException.class, () -> smartphoneService.findByName(expectedSmartphoneDTO.getName()));
    }

    @Test
    void whenListSmartphoneIsCalledThenReturnAListOfSmartphone() {
        // given
        SmartphoneDTO expectedSmartphoneDTO = SmartphoneDTOBuilder.builder().build().toSmartphoneDTO();
        Smartphone expectedSmartphone = smartphoneMapper.toModel(expectedSmartphoneDTO);

        //when
        when(smartphoneRepository.findAll()).thenReturn(Collections.singletonList(expectedSmartphone));

        //then
        List<SmartphoneDTO> foundListSmartphonesDTO = smartphoneService.findAll();

        assertThat(foundListSmartphonesDTO, is(not(empty())));
        assertThat(foundListSmartphonesDTO.get(0), is(equalTo(expectedSmartphoneDTO)));
    }

    @Test
    void whenListSmartphoneIsCalledThenReturnAnEmptyListOfSmartphones() {
        //when
        when(smartphoneRepository.findAll()).thenReturn(Collections.EMPTY_LIST);

        //then
        List<SmartphoneDTO> foundListSmartphonesDTO = smartphoneService.findAll();

        assertThat(foundListSmartphonesDTO, is(empty()));
    }

    @Test
    void whenExclusionIsCalledWithValidIdThenASmartphoneShouldBeDeleted() throws SmartphoneNotFoundException {
        // given
        SmartphoneDTO expectedDeletedSmartphoneDTO = SmartphoneDTOBuilder.builder().build().toSmartphoneDTO();
        Smartphone expectedDeletedSmartphone = smartphoneMapper.toModel(expectedDeletedSmartphoneDTO);

        //when
        when(smartphoneRepository.findById(expectedDeletedSmartphoneDTO.getId())).thenReturn(Optional.of(expectedDeletedSmartphone));
        doNothing().when(smartphoneRepository).deleteById(expectedDeletedSmartphoneDTO.getId());

        //then
        smartphoneService.deleteById(expectedDeletedSmartphoneDTO.getId());

        verify(smartphoneRepository, times(1)).findById(expectedDeletedSmartphoneDTO.getId());
        verify(smartphoneRepository, times(1)).deleteById(expectedDeletedSmartphoneDTO.getId());
    }

    @Test
    void whenIncrementIsCalledThenIncrementSmartphoneStock() throws SmartphoneNotFoundException, SmartphoneStockExceededException {
        //given
        SmartphoneDTO expectedSmartphoneDTO = SmartphoneDTOBuilder.builder().build().toSmartphoneDTO();
        Smartphone expectedSmartphone = smartphoneMapper.toModel(expectedSmartphoneDTO);

        //when
        when(smartphoneRepository.findById(expectedSmartphoneDTO.getId())).thenReturn(Optional.of(expectedSmartphone));
        when(smartphoneRepository.save(expectedSmartphone)).thenReturn(expectedSmartphone);

        int quantityToIncrement = 10;
        int expectedQuantityAfterIncrement = expectedSmartphoneDTO.getQuantity() + quantityToIncrement;

        //then
        SmartphoneDTO incrementedSmartphoneDTO = smartphoneService.increment(expectedSmartphoneDTO.getId(), quantityToIncrement);

        assertThat(expectedQuantityAfterIncrement, equalTo(incrementedSmartphoneDTO.getQuantity()));
        assertThat(expectedQuantityAfterIncrement, lessThan(expectedSmartphoneDTO.getMax()));
    }

    @Test
    void whenIncrementIsGreatherThanMaxThenThrowException() {
        SmartphoneDTO expectedSmartphoneDTO = SmartphoneDTOBuilder.builder().build().toSmartphoneDTO();
        Smartphone expectedSmartphone = smartphoneMapper.toModel(expectedSmartphoneDTO);

        when(smartphoneRepository.findById(expectedSmartphoneDTO.getId())).thenReturn(Optional.of(expectedSmartphone));

        int quantityToIncrement = 80;
        assertThrows(SmartphoneStockExceededException.class, () -> smartphoneService.increment(expectedSmartphoneDTO.getId(), quantityToIncrement));
    }

    @Test
    void whenIncrementAfterSumIsGreatherThanMaxThenThrowException() {
        SmartphoneDTO expectedSmartphoneDTO = SmartphoneDTOBuilder.builder().build().toSmartphoneDTO();
        Smartphone expectedSmartphone = smartphoneMapper.toModel(expectedSmartphoneDTO);

        when(smartphoneRepository.findById(expectedSmartphoneDTO.getId())).thenReturn(Optional.of(expectedSmartphone));

        int quantityToIncrement = 45;
        assertThrows(SmartphoneStockExceededException.class, () -> smartphoneService.increment(expectedSmartphoneDTO.getId(), quantityToIncrement));
    }

    @Test
    void whenIncrementIsCalledWithInvalidIdThenThrowException() {
        int quantityToIncrement = 10;

        when(smartphoneRepository.findById(INVALID_SMARTPHONE_ID)).thenReturn(Optional.empty());

        assertThrows(SmartphoneNotFoundException.class, () -> smartphoneService.increment(INVALID_SMARTPHONE_ID, quantityToIncrement));
    }
}
