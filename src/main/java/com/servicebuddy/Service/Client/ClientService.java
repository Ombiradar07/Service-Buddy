package com.servicebuddy.Service.Client;

import com.servicebuddy.DTO.AdDetailsForClientDto;
import com.servicebuddy.DTO.AdDto;
import com.servicebuddy.DTO.BookingDto;
import com.servicebuddy.DTO.ReviewDto;

import java.util.List;

public interface ClientService {

    List<AdDto> getAllAds();

    List<AdDto> searchAdsByName(String keyword);

    BookingDto bookService(BookingDto bookingDto);

    AdDetailsForClientDto getAdDetailsById(Long adId);

    List<BookingDto> getAllBookingsByUserId(Long userId);

    ReviewDto writeReview(ReviewDto reviewDto);
}
