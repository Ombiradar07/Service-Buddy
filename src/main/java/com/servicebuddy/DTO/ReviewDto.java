package com.servicebuddy.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ReviewDto {

    private Long id;

    private Date reviewDate;

    @NotBlank( message = "Review cannot be blank" )
    private String review;

    @NotNull( message = "Rating cannot be blank")
    @Size( min = 1, max = 5, message = "Rating must be between 1 and 5")
    private Integer rating;

    private Long userId;

    private Long adId;

    private String userName;

    private String serviceName;

    private Long bookingId;
}
