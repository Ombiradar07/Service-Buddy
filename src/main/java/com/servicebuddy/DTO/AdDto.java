package com.servicebuddy.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class AdDto {

    private Long id;

    private String serviceName;

    private String description;

    private Double price;

    private MultipartFile img;

    private byte[] imgResponse;

    private Long userId;

    private String companyName;

}
