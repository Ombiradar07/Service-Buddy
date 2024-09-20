package com.servicebuddy.Service.Client;

import com.servicebuddy.DTO.*;

import java.util.List;

public interface ClientService {

    List<AdResponseDto> getAllAds();

    List<AdResponseDto> searchAdsByName(String keyword);

    BookingResponseDto bookService(BookingRequestDto bookingRequestDto);

    AdDetailsForClientDto getAdDetailsById(Long adId);

    List<BookingResponseDto> getAllBookingsByUserId(Long userId);

    ReviewResponseDto writeReview(ReviewRequestDto reviewRequestDto);
}
