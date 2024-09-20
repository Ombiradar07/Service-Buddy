package com.servicebuddy.Controller;

import com.servicebuddy.DTO.AdResponseDto;
import com.servicebuddy.DTO.AdRequestDto;
import com.servicebuddy.DTO.BookingResponseDto;
import com.servicebuddy.Enum.BookingStatus;
import com.servicebuddy.Service.Company.CompanyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @PostMapping("/post-ad/{userId}")
    public ResponseEntity<AdResponseDto> postAd(@PathVariable Long userId, @Valid @ModelAttribute AdRequestDto request) {

        AdResponseDto savedDto = companyService.postAd(userId, request);
        return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
    }

    @PutMapping("update-ad/{adId}")
    public ResponseEntity<AdResponseDto> updateAd(@PathVariable Long adId, @Valid @ModelAttribute AdRequestDto adRequestDto) {
        AdResponseDto updatedAd = companyService.updateAd(adRequestDto, adId);
        return new ResponseEntity<>(updatedAd, HttpStatus.OK);
    }

    @GetMapping("get-all-ads/{userId}")
    public ResponseEntity<List<AdResponseDto>> getAllAdsByUserId(@PathVariable Long userId) {
        List<AdResponseDto> allAds = companyService.getAllAdsByUserId(userId);
        return new ResponseEntity<>(allAds, HttpStatus.OK);
    }

    @GetMapping("/get-ad/{adId}")
    public ResponseEntity<AdResponseDto> getAdById(@PathVariable Long adId) {
        AdResponseDto adResponseDto = companyService.getAdById(adId);

        return new ResponseEntity<>(adResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/delete-ad/{adId}")
    public ResponseEntity<String> deleteAd(@PathVariable Long adId) {
        companyService.deleteAd(adId);
        return new ResponseEntity<>("Ad deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/get-all-bookings/{companyId}")
    public ResponseEntity<List<BookingResponseDto>> getAllBookings(@PathVariable Long companyId) {
        List<BookingResponseDto> allBookings = companyService.getAllBookings(companyId);
        return new ResponseEntity<>(allBookings, HttpStatus.OK);
    }

    @GetMapping("/booking-status/{bookingId}/{status}")
    public ResponseEntity<BookingStatus> changeBookingStatus(@PathVariable Long bookingId, @PathVariable String status) {
        BookingStatus bookingStatus = companyService.changeBookingStatus(bookingId, status);
        return new ResponseEntity<>(bookingStatus, HttpStatus.OK);
    }

}