package com.rensilver.smartphone_api.service;

import com.rensilver.smartphone_api.builder.SmartphoneDTOBuilder;
import com.rensilver.smartphone_api.dto.SmartphoneDTO;
import com.rensilver.smartphone_api.entity.Smartphone;
import com.rensilver.smartphone_api.exception.SmartphoneAlreadyRegisteredException;
import com.rensilver.smartphone_api.mapper.SmartphoneMapper;
import com.rensilver.smartphone_api.repository.SmartphoneRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

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
}
