package com.servicebuddy.DTO;

import com.servicebuddy.Enum.BookingStatus;
import com.servicebuddy.Enum.ReviewStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BookingDto {

    private Long id;

    private Date bookingDate;

    private Long userId;

    private String userName;

    private Long companyId;

    private String serviceName;

    private Long adId;

    private BookingStatus bookingStatus;

    private ReviewStatus reviewStatus;

}
