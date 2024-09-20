package com.servicebuddy.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BookingRequestDto {

    @NotNull(message = "Please enter a valid booking date.")
    private Date bookingDate;

    private Long userId;

    private Long adId;

}
