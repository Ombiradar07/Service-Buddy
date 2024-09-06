package com.servicebuddy.Controller;

import com.servicebuddy.DTO.AdDetailsForClientDto;
import com.servicebuddy.DTO.AdDto;
import com.servicebuddy.DTO.BookingDto;
import com.servicebuddy.DTO.ReviewDto;
import com.servicebuddy.Service.Client.ClientService;
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
    public ResponseEntity<List<AdDto>> getAllAds() {

        List<AdDto> allAds = clientService.getAllAds();
        return new ResponseEntity<>(allAds, HttpStatus.OK);

    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<AdDto>> searchAdsByName(@PathVariable String keyword) {
        List<AdDto> searchResults = clientService.searchAdsByName(keyword);
        return new ResponseEntity<>(searchResults, HttpStatus.OK);
    }

    @PostMapping("/book-service")
    public ResponseEntity<BookingDto> bookService(@RequestBody BookingDto bookingDto) {

        BookingDto bookedService = clientService.bookService(bookingDto);
        return new ResponseEntity<>(bookedService, HttpStatus.OK);
    }

    @GetMapping("/ad-details/{adId}")
    public ResponseEntity<AdDetailsForClientDto> getAdDetailsById(@PathVariable Long adId) {
        AdDetailsForClientDto adDetails = clientService.getAdDetailsById(adId);
        return new ResponseEntity<>(adDetails, HttpStatus.OK);
    }

    @GetMapping("/bookings/{userId}")
    public ResponseEntity<List<BookingDto>> getAllBookingsByUserId(@PathVariable Long userId) {
        List<BookingDto> bookings = clientService.getAllBookingsByUserId(userId);
        return new ResponseEntity<>(bookings, HttpStatus.OK);

    }

     @PostMapping("/add-review")
    public ResponseEntity<ReviewDto> addReview(@RequestBody ReviewDto reviewDto) {
        ReviewDto addedReview = clientService.writeReview(reviewDto);
        return new ResponseEntity<>(addedReview, HttpStatus.CREATED);
    }

}
