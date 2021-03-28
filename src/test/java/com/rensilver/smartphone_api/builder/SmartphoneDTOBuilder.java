package com.rensilver.smartphone_api.builder;

import com.rensilver.smartphone_api.dto.SmartphoneDTO;
import com.rensilver.smartphone_api.enums.SmartphoneMarketType;
import lombok.Builder;

@Builder
public class SmartphoneDTOBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String name = "Iphone Xr";

    @Builder.Default
    private String ram = "16 GB";

    @Builder.Default
    private String camQuality = "32MP";

    @Builder.Default
    private String brand = "Apple";

    @Builder.Default
    private int max = 50;

    @Builder.Default
    private int quantity = 10;

    @Builder.Default
    private SmartphoneMarketType type = SmartphoneMarketType.PERFORMANCE;

    public SmartphoneDTO toSmartphoneDTO() {
        return new SmartphoneDTO(
                id,
                name,
                ram,
                camQuality,
                brand,
                max,
                quantity,
                type);
    }
}
