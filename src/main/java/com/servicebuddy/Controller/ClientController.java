package com.servicebuddy.Controller;

import com.servicebuddy.DTO.*;
import com.servicebuddy.Service.Client.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/ads")
    public ResponseEntity<List<AdResponseDto>> getAllAds() {

        List<AdResponseDto> allAds = clientService.getAllAds();
        return new ResponseEntity<>(allAds, HttpStatus.OK);

    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<AdResponseDto>> searchAdsByName(@PathVariable String keyword) {
        List<AdResponseDto> searchResults = clientService.searchAdsByName(keyword);
        return new ResponseEntity<>(searchResults, HttpStatus.OK);
    }

    @PostMapping("/book-service")
    public ResponseEntity<BookingResponseDto> bookService(@Valid @RequestBody BookingRequestDto bookingRequestDto) {

        BookingResponseDto bookedService = clientService.bookService(bookingRequestDto);
        return new ResponseEntity<>(bookedService, HttpStatus.OK);
    }

    @GetMapping("/ad-details/{adId}")
    public ResponseEntity<AdDetailsForClientDto> getAdDetailsById(@PathVariable Long adId) {
        AdDetailsForClientDto adDetails = clientService.getAdDetailsById(adId);
        return new ResponseEntity<>(adDetails, HttpStatus.OK);
    }

    @GetMapping("/bookings/{userId}")
    public ResponseEntity<List<BookingResponseDto>> getAllBookingsByUserId(@PathVariable Long userId) {
        List<BookingResponseDto> bookings = clientService.getAllBookingsByUserId(userId);
        return new ResponseEntity<>(bookings, HttpStatus.OK);

    }

    @PostMapping("/add-review")
    public ResponseEntity<ReviewResponseDto> addReview(@Valid @RequestBody ReviewRequestDto reviewRequestDto) {
        ReviewResponseDto addedReview = clientService.writeReview(reviewRequestDto);
        return new ResponseEntity<>(addedReview, HttpStatus.CREATED);
    }

}
