package com.servicebuddy.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class AdRequestDto {


    @NotBlank(message = "Service name is required")
    private String serviceName;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Price is required")
    private Double price;

    @NotNull(message = "Ad image is required")
    private MultipartFile img;

    private Long userId;

}
