package com.servicebuddy.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ReviewDto {

    private Long id;

    private Date reviewDate;

    private String review;

    private Integer rating;

    private Long userId;

    private Long adId;

    private String userName;

    private String serviceName;

    private Long bookingId;
}
