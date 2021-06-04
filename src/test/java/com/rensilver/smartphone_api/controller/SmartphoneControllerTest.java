package com.rensilver.smartphone_api.controller;

import com.rensilver.smartphone_api.builder.SmartphoneDTOBuilder;
import com.rensilver.smartphone_api.dto.QuantityDTO;
import com.rensilver.smartphone_api.dto.SmartphoneDTO;
import com.rensilver.smartphone_api.exception.SmartphoneNotFoundException;
import com.rensilver.smartphone_api.service.SmartphoneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;

import static com.rensilver.smartphone_api.utils.JsonConvertionUtils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class SmartphoneControllerTest {

    private static final String SMARTPHONE_API_URL_PATH = "/smartphones";
    private static final long VALID_SMARTPHONE_ID = 1L;
    private static final long INVALID_SMARTPHONE_ID = 2l;
    private static final String SMARTPHONE_API_SUBPATH_INCREMENT_URL = "/increment";
    private static final String SMARTPHONE_API_SUBPATH_DECREMENT_URL = "/decrement";

    private MockMvc mockMvc;

    @Mock
    private SmartphoneService smartphoneService;

    @InjectMocks
    private SmartphoneController smartphoneController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(smartphoneController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenASmartphoneIsCreated() throws Exception {
        // given
        SmartphoneDTO smartphoneDTO = SmartphoneDTOBuilder.builder().build().toSmartphoneDTO();

        // when
        when(smartphoneService.createSmartphone(smartphoneDTO)).thenReturn(smartphoneDTO);

        // then
        mockMvc.perform(post(SMARTPHONE_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(smartphoneDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(smartphoneDTO.getName())))
                .andExpect(jsonPath("$.ram", is(smartphoneDTO.getRam())))
                .andExpect(jsonPath("$.camQuality", is(smartphoneDTO.getCamQuality())))
                .andExpect(jsonPath("$.brand", is(smartphoneDTO.getBrand())))
                .andExpect(jsonPath("$.type", is(smartphoneDTO.getType().toString())));
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        SmartphoneDTO smartphoneDTO = SmartphoneDTOBuilder.builder().build().toSmartphoneDTO();
        smartphoneDTO.setBrand(null);

        // then
        mockMvc.perform(post(SMARTPHONE_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(smartphoneDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGETIsCalledWithValidNameThenOkStatusIsReturned() throws Exception {
        // given
        SmartphoneDTO smartphoneDTO = SmartphoneDTOBuilder.builder().build().toSmartphoneDTO();

        //when
        when(smartphoneService.findByName(smartphoneDTO.getName())).thenReturn(smartphoneDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(SMARTPHONE_API_URL_PATH + "/" + smartphoneDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(smartphoneDTO.getName())))
                .andExpect(jsonPath("$.ram", is(smartphoneDTO.getRam())))
                .andExpect(jsonPath("$.camQuality", is(smartphoneDTO.getCamQuality())))
                .andExpect(jsonPath("$.brand", is(smartphoneDTO.getBrand())))
                .andExpect(jsonPath("$.type", is(smartphoneDTO.getType().toString())));
    }

    @Test
    void whenGETIsCalledWithoutRegisteredNameThenNotFoundStatusIsReturned() throws Exception {
        // given
        SmartphoneDTO smartphoneDTO = SmartphoneDTOBuilder.builder().build().toSmartphoneDTO();

        //when
        when(smartphoneService.findByName(smartphoneDTO.getName())).thenThrow(SmartphoneNotFoundException.class);

        //then
        mockMvc.perform(MockMvcRequestBuilders.get(SMARTPHONE_API_URL_PATH + "/" + smartphoneDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGETListWithSmartphoneIsCalledThenOkStatusIsReturned() throws Exception {
        //given
        SmartphoneDTO smartphoneDTO = SmartphoneDTOBuilder.builder().build().toSmartphoneDTO();

        //when
        when(smartphoneService.findAll()).thenReturn(Collections.singletonList(smartphoneDTO));

        //then
        mockMvc.perform(MockMvcRequestBuilders.get(SMARTPHONE_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(smartphoneDTO.getName())))
                .andExpect(jsonPath("$[0].ram", is(smartphoneDTO.getRam())))
                .andExpect(jsonPath("$[0].camQuality", is(smartphoneDTO.getCamQuality())))
                .andExpect(jsonPath("$[0].brand", is(smartphoneDTO.getBrand())))
                .andExpect(jsonPath("$[0].type", is(smartphoneDTO.getType().toString())));
    }

    @Test
    void whenGETListWithoutSmartphonesIsCalledThenOkStatusIsReturned() throws Exception {
        //given
        SmartphoneDTO smartphoneDTO = SmartphoneDTOBuilder.builder().build().toSmartphoneDTO();

        //when
        when(smartphoneService.findAll()).thenReturn(Collections.singletonList(smartphoneDTO));

        //then
        mockMvc.perform(MockMvcRequestBuilders.get(SMARTPHONE_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenDELETEIsCalledWithValidIdThenNoContentStatusIsReturned() throws Exception {
        //given
        SmartphoneDTO smartphoneDTO = SmartphoneDTOBuilder.builder().build().toSmartphoneDTO();

        //when
        doNothing().when(smartphoneService).deleteById(smartphoneDTO.getId());

        //then
        mockMvc.perform(MockMvcRequestBuilders.get(SMARTPHONE_API_URL_PATH + "/" + smartphoneDTO.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDELETEIsCalledWithInvalidIdThenNotFoundStatusIsReturned() throws Exception {
        //given
        doThrow(SmartphoneNotFoundException.class).when(smartphoneService).deleteById(INVALID_SMARTPHONE_ID);

        //then
        mockMvc.perform(MockMvcRequestBuilders.get(SMARTPHONE_API_URL_PATH + "/" + INVALID_SMARTPHONE_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenPATCHIsCalledToIncrementDiscountThenOKstatusIsReturned() throws Exception {
        QuantityDTO quantityDTO = QuantityDTO.builder()
                .quantity(10)
                .build();

        SmartphoneDTO smartphoneDTO = SmartphoneDTOBuilder.builder().build().toSmartphoneDTO();
        smartphoneDTO.setQuantity(smartphoneDTO.getQuantity() + quantityDTO.getQuantity());

        when(smartphoneService.increment(VALID_SMARTPHONE_ID, quantityDTO.getQuantity())).thenReturn(smartphoneDTO);

        mockMvc.perform(MockMvcRequestBuilders.patch(SMARTPHONE_API_URL_PATH + "/" + VALID_SMARTPHONE_ID + SMARTPHONE_API_SUBPATH_INCREMENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(quantityDTO))).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(smartphoneDTO.getName())))
                .andExpect(jsonPath("$.ram", is(smartphoneDTO.getRam())))
                .andExpect(jsonPath("$.camQuality", is(smartphoneDTO.getCamQuality())))
                .andExpect(jsonPath("$.brand", is(smartphoneDTO.getBrand())))
                .andExpect(jsonPath("$.type", is(smartphoneDTO.getType().toString())))
                .andExpect(jsonPath("$.quantity", is(smartphoneDTO.getQuantity())));
    }
}
