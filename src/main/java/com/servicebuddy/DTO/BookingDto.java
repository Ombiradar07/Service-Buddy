package com.servicebuddy.DTO;

import com.servicebuddy.Enum.BookingStatus;
import com.servicebuddy.Enum.ReviewStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class BookingDto {

    private Long id;

    @NotBlank( message = "Please enter a valid booking date.")
    @DateTimeFormat(pattern = "dd-mm-yyyy")
    private Date bookingDate;

    private Long userId;

    private String userName;

    private Long companyId;

    private String serviceName;

    private Long adId;

    private BookingStatus bookingStatus;

    private ReviewStatus reviewStatus;

}
