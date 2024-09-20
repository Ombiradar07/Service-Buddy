package com.servicebuddy.Service.Client;

import com.servicebuddy.DTO.*;
import com.servicebuddy.Entity.Ad;
import com.servicebuddy.Entity.Booking;
import com.servicebuddy.Entity.Review;
import com.servicebuddy.Entity.Users;
import com.servicebuddy.Enum.BookingStatus;
import com.servicebuddy.Enum.ReviewStatus;
import com.servicebuddy.Exception.ResourceNotFoundException;
import com.servicebuddy.Exception.UnauthorizedAccessException;
import com.servicebuddy.Repository.AdRepository;
import com.servicebuddy.Repository.BookingRepository;
import com.servicebuddy.Repository.ReviewRepository;
import com.servicebuddy.Repository.UserRepository;
import com.servicebuddy.Service.Utils.EmailService;
import com.servicebuddy.Service.Utils.RedisService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ReviewRepository reviewRepository;

    private final RedisService redisService;

    @Autowired
    public ClientServiceImpl(RedisService redisService) {
        this.redisService = redisService;
    }

    private static final String ALL_ADS_CACHE_KEY = "ALL_ADS";

    @Autowired
    private EmailService emailService;

    @Override
    public List<AdResponseDto> getAllAds() {
        log.info("Fetching all ads.");
        List<AdResponseDto> cachedAds = (List<AdResponseDto>) redisService.getData(ALL_ADS_CACHE_KEY);

        if (cachedAds != null) {
            log.info("Returning cached ads.");
            return cachedAds;
        } else {
            log.info("Fetching ads from the database.");
            List<Ad> allAds = adRepository.findAll();
            List<AdResponseDto> adResponseDtoList = allAds.stream().map(ad -> {
                AdResponseDto adResponseDto = modelMapper.map(ad, AdResponseDto.class);
                adResponseDto.setCompanyName(ad.getUser().getName());
                return adResponseDto;
            }).collect(Collectors.toList());

            // Cache the result
            redisService.setData(ALL_ADS_CACHE_KEY, adResponseDtoList, 60);
            log.info("Ads fetched from database and cached.");
            return adResponseDtoList;
        }
    }

    @Override
    public List<AdResponseDto> searchAdsByName(String keyword) {
        log.info("Searching ads by keyword: {}", keyword);
        return adRepository.searchByKeyword(keyword).stream().map(ad -> {
            AdResponseDto adResponseDto = modelMapper.map(ad, AdResponseDto.class);
            adResponseDto.setImgUrl(ad.getImgUrl());
            adResponseDto.setCompanyName(ad.getUser().getName());
            return adResponseDto;
        }).toList();
    }

    @Override
    public BookingResponseDto bookService(BookingRequestDto bookingRequestDto) {
        log.info("Booking service for userId: {} and adId: {}", bookingRequestDto.getUserId(), bookingRequestDto.getAdId());
        Users user = userRepository.findById(bookingRequestDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found !!!"));
        Ad ad = adRepository.findById(bookingRequestDto.getAdId())
                .orElseThrow(() -> new ResourceNotFoundException("Ad not found !!!"));

        Booking booking = new Booking();
        booking.setBookingDate(bookingRequestDto.getBookingDate());
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setReviewStatus(ReviewStatus.FALSE);
        booking.setAd(ad);
        booking.setUser(user);
        booking.setCompany(ad.getUser());

        BookingResponseDto dto = modelMapper.map(bookingRepository.save(booking), BookingResponseDto.class);
        dto.setServiceName(ad.getServiceName());

        emailService.sendEmail(
                booking.getUser().getEmail(),
                "Your Service has been Successfully Booked",
                String.format("Dear %s,\n\n\nYour service, %s, has been successfully booked by %s on %s.\n\nBest regards,\nThe ServiceBuddy Team",
                        booking.getUser().getName(), ad.getServiceName(), user.getName(), booking.getBookingDate())
        );

        log.info("Service booked successfully for userId: {}", bookingRequestDto.getUserId());
        return dto;
    }

    @Override
    public AdDetailsForClientDto getAdDetailsById(Long adId) {
        log.info("Fetching ad details for adId: {}", adId);
        Ad ad = adRepository.findById(adId).orElseThrow(() -> new ResourceNotFoundException("Ad not found !!!"));

        AdResponseDto adResponseDto = modelMapper.map(ad, AdResponseDto.class);
        adResponseDto.setImgUrl(ad.getImgUrl());
        adResponseDto.setCompanyName(ad.getUser().getName());

        AdDetailsForClientDto dto = new AdDetailsForClientDto();
        dto.setAdResponseDto(adResponseDto);

        // Adding Reviews to the ads
        List<Review> reviewList = reviewRepository.findByAdId(adId);
        dto.setReviewResponseDtoList(reviewList.stream().map(review -> {
            ReviewResponseDto sampleDto = modelMapper.map(review, ReviewResponseDto.class);
            sampleDto.setServiceName(ad.getServiceName());
            sampleDto.setBookingId(sampleDto.getBookingId());
            return sampleDto;
        }).toList());

        log.info("Ad details fetched for adId: {}", adId);
        return dto;
    }

    @Override
    public List<BookingResponseDto> getAllBookingsByUserId(Long userId) {
        log.info("Fetching all bookings for userId: {}", userId);
        return bookingRepository.findAllByUserId(userId).stream().map(booking -> {
            BookingResponseDto bookingResponseDto = modelMapper.map(booking, BookingResponseDto.class);
            bookingResponseDto.setServiceName(booking.getAd().getServiceName());
            return bookingResponseDto;
        }).toList();
    }

    @Transactional
    @Override
    public ReviewResponseDto writeReview(ReviewRequestDto reviewRequestDto) {
        log.info("Writing review for bookingId: {}", reviewRequestDto.getBookingId());

        Booking booking = bookingRepository.findById(reviewRequestDto.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found!"));
        Users user = userRepository.findById(reviewRequestDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        if (!booking.getUser().getId().equals(user.getId()) && !booking.getBookingStatus().equals(BookingStatus.COMPLETED)) {
            throw new UnauthorizedAccessException("User not authorized to add review!");
        }

        Review review = new Review();
        review.setReviewDate(new Date());
        review.setUser(user);
        review.setAd(booking.getAd());
        review.setReview(reviewRequestDto.getReview());
        review.setRating(reviewRequestDto.getRating());
        review.setBooking(booking);

        review = reviewRepository.save(review);

        booking.setReviewStatus(ReviewStatus.TRUE);
        bookingRepository.save(booking);

        log.info("Review added successfully for bookingId: {}", reviewRequestDto.getBookingId());

        ReviewResponseDto dto = modelMapper.map(review, ReviewResponseDto.class);
        dto.setServiceName(booking.getAd().getServiceName());
        dto.setBookingId(booking.getId());

        return dto;
    }
}
