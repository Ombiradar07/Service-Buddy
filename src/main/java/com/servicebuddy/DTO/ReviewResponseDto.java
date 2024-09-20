package com.servicebuddy.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ReviewResponseDto {

    private Long id;

    private Date reviewDate;

    @NotBlank(message = "Review cannot be blank")
    private String review;

    @NotNull(message = "Rating cannot be null")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    private Long userId;

    private Long adId;

    private String userName;

    private String serviceName;

    private Long bookingId;
}
