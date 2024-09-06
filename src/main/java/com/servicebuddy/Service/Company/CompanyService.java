package com.servicebuddy.Service.Company;


import com.servicebuddy.DTO.AdDto;
import com.servicebuddy.DTO.BookingDto;
import com.servicebuddy.Enum.BookingStatus;

import java.util.List;

public interface CompanyService {

    AdDto postAd(Long userId, AdDto adDto);

    AdDto updateAd(AdDto adDto , Long adId);

    List<AdDto> getAllAdsByUserId(Long userId);

    AdDto getAdById(Long adId);

    void deleteAd(Long adId);

    List<BookingDto> getAllBookings(Long companyId);

    BookingStatus changeBookingStatus(Long bookingId, String status);


}
