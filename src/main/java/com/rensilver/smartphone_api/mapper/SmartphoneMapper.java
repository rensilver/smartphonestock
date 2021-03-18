package com.rensilver.smartphone_api.mapper;

import com.rensilver.smartphone_api.dto.SmartphoneDTO;
import com.rensilver.smartphone_api.entity.Smartphone;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SmartphoneMapper {

    SmartphoneMapper INSTANCE = Mappers.getMapper(SmartphoneMapper.class);

    Smartphone toModel(SmartphoneDTO smartphoneDTO);

    SmartphoneDTO toDTO(Smartphone smartphone);
}
