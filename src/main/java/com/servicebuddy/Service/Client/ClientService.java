package com.servicebuddy.Service.Client;

import com.servicebuddy.DTO.*;

import java.util.List;

public interface ClientService {

    List<AdDto> getAllAds();

    List<AdDto> searchAdsByName(String keyword);

    BookingDto bookService(BookingDto bookingDto);

    AdDetailsForClientDto getAdDetailsById(Long adId);

    List<BookingDto> getAllBookingsByUserId(Long userId);

    ReviewDto writeReview(ReviewDto reviewDto);
}
