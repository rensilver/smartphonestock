package com.rensilver.smartphone_api.controller;

import com.rensilver.smartphone_api.builder.SmartphoneDTOBuilder;
import com.rensilver.smartphone_api.dto.SmartphoneDTO;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import static com.rensilver.smartphone_api.utils.JsonConvertionUtils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
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
}
