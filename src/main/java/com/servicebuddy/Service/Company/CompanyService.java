package com.servicebuddy.Service.Company;


import com.servicebuddy.DTO.AdResponseDto;
import com.servicebuddy.DTO.AdRequestDto;
import com.servicebuddy.DTO.BookingResponseDto;
import com.servicebuddy.Enum.BookingStatus;

import java.util.List;

public interface CompanyService {

    AdResponseDto postAd(Long userId, AdRequestDto adRequestDto);

    AdResponseDto updateAd(AdRequestDto adRequestDto , Long adId);

    List<AdResponseDto> getAllAdsByUserId(Long userId);

    AdResponseDto getAdById(Long adId);

    void deleteAd(Long adId);

    List<BookingResponseDto> getAllBookings(Long companyId);

    BookingStatus changeBookingStatus(Long bookingId, String status);


}
