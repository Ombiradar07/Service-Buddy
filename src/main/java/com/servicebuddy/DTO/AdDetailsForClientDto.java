package com.servicebuddy.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdDetailsForClientDto {

    private AdResponseDto adResponseDto;

    private List<ReviewResponseDto> reviewResponseDtoList;
}
