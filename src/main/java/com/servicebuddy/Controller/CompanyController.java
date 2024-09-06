package com.servicebuddy.Controller;

import com.servicebuddy.DTO.AdDto;
import com.servicebuddy.DTO.BookingDto;
import com.servicebuddy.Enum.BookingStatus;
import com.servicebuddy.Service.Company.CompanyService;
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
    public ResponseEntity<AdDto> postAd(@PathVariable long userId, @ModelAttribute AdDto adDto) {

        AdDto savedDto = companyService.postAd(userId, adDto);
        return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
    }

    @PutMapping("update-ad/{adId}")
    public ResponseEntity<AdDto> updateAd(@PathVariable Long adId, @ModelAttribute AdDto adDto) {
        AdDto updatedAd = companyService.updateAd(adDto, adId);
        return new ResponseEntity<>(updatedAd, HttpStatus.OK);
    }

    @GetMapping("get-all-ads/{userId}")
    public ResponseEntity<List<AdDto>> getAllAdsByUserId(@PathVariable Long userId) {
        List<AdDto> allAds = companyService.getAllAdsByUserId(userId);
        return new ResponseEntity<>(allAds, HttpStatus.OK);
    }

    @GetMapping("/get-ad/{adId}")
    public ResponseEntity<AdDto> getAdById(@PathVariable Long adId) {
        AdDto adDto = companyService.getAdById(adId);

        return new ResponseEntity<>(adDto, HttpStatus.OK);
    }

    @DeleteMapping("/delete-ad/{adId}")
    public ResponseEntity<String> deleteAd(@PathVariable Long adId) {
        companyService.deleteAd(adId);
        return new ResponseEntity<>("Ad deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/get-all-bookings/{companyId}")
    public ResponseEntity<List<BookingDto>> getAllBookings(@PathVariable Long companyId) {
        List<BookingDto> allBookings = companyService.getAllBookings(companyId);
        return new ResponseEntity<>(allBookings, HttpStatus.OK);
    }

    @GetMapping("/booking-status/{bookingId}/{status}")
    public ResponseEntity<BookingStatus> changeBookingStatus(@PathVariable Long bookingId, @PathVariable String status) {
        BookingStatus bookingStatus = companyService.changeBookingStatus(bookingId, status);
        return new ResponseEntity<>(bookingStatus, HttpStatus.OK);
    }


}