package com.servicebuddy.Service.Client;

import com.servicebuddy.DTO.AdDetailsForClientDto;
import com.servicebuddy.DTO.AdDto;
import com.servicebuddy.DTO.BookingDto;
import com.servicebuddy.DTO.ReviewDto;
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
import com.servicebuddy.Service.Utils.RedisService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    private static String ALL_ADS_CACHE_KEY = "ALL_ADS";


    public List<AdDto> getAllAds() {

        List<AdDto> cachedAds = (List<AdDto>) redisService.getData(ALL_ADS_CACHE_KEY);

        if (cachedAds != null) {
            // If cached, return the cached ads
            return cachedAds;
        } else {
            // If cache is empty, fetch ads from the database
            List<Ad> allAds = adRepository.findAll();
            List<AdDto> adDtoList = allAds.stream().map(ad -> {
                AdDto adDto = modelMapper.map(ad, AdDto.class);
                adDto.setCompanyName(ad.getUser().getName());
                return adDto;
            }).collect(Collectors.toList());

            // Cache the result in Redis with an expiration time (e.g., 60 minutes)
            redisService.setData(ALL_ADS_CACHE_KEY, adDtoList, 60);

            return adDtoList;
        }
    }

    public List<AdDto> searchAdsByName(String keyword) {

        return adRepository.searchByKeyword(keyword).stream().map(ad -> {
            AdDto adDto = modelMapper.map(ad, AdDto.class);
            adDto.setImgUrl(ad.getImgUrl());
            adDto.setCompanyName(ad.getUser().getName());
            return adDto;
        }).toList();
    }

    public BookingDto bookService(BookingDto bookingDto) {
        Users user = userRepository.findById(bookingDto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found !!!"));
        Ad ad = adRepository.findById(bookingDto.getAdId()).orElseThrow(() -> new ResourceNotFoundException("Ad not found !!!"));

        Booking booking = modelMapper.map(bookingDto, Booking.class);

        booking.setBookingDate(bookingDto.getBookingDate());
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setReviewStatus(ReviewStatus.FALSE);
        booking.setAd(ad);
        booking.setUser(user);
        booking.setCompany(ad.getUser());


        BookingDto dto = modelMapper.map(bookingRepository.save(booking), BookingDto.class);
        dto.setServiceName(ad.getServiceName());
        return dto;

    }

    public AdDetailsForClientDto getAdDetailsById(Long adId) {
        Ad ad = adRepository.findById(adId).orElseThrow(() -> new ResourceNotFoundException("Ad not found !!!"));

        AdDto adDto = modelMapper.map(ad, AdDto.class);
        adDto.setImgUrl(ad.getImgUrl());
        adDto.setCompanyName(ad.getUser().getName());

        AdDetailsForClientDto dto = new AdDetailsForClientDto();
        dto.setAdDto(adDto);

        // Adding Reviews to the ads
        List<Review> reviewList = reviewRepository.findByAdId(adId);

        dto.setReviewDtoList(reviewList.stream().map(review -> {
            ReviewDto sampleDto = modelMapper.map(review, ReviewDto.class);
            sampleDto.setServiceName(ad.getServiceName());
            sampleDto.setBookingId(sampleDto.getBookingId());
            return sampleDto;
        }).toList());
        return dto;
    }

    public List<BookingDto> getAllBookingsByUserId(Long userId) {
        return bookingRepository.findAllByUserId(userId).stream().map(booking -> {
            BookingDto bookingDto = modelMapper.map(booking, BookingDto.class);
            bookingDto.setServiceName(booking.getAd().getServiceName());
            return bookingDto;
        }).toList();
    }

    @Transactional
    public ReviewDto writeReview(ReviewDto reviewDto) {

        Booking booking = bookingRepository.findById(reviewDto.getBookingId()).orElseThrow(() -> new ResourceNotFoundException("Booking not found!"));

        Users user = userRepository.findById(reviewDto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        // Check if the user is authorized to add a review
        if (!booking.getUser().getId().equals(user.getId()) && !booking.getBookingStatus().equals(BookingStatus.COMPLETED)) {
            throw new UnauthorizedAccessException("User not authorized to add review!");
        }

        Review review = modelMapper.map(reviewDto, Review.class);

        review.setReviewDate(new Date());
        review.setUser(user);
        review.setAd(booking.getAd());

        review = reviewRepository.save(review);

        booking.setReviewStatus(ReviewStatus.TRUE);
        bookingRepository.save(booking);

        ReviewDto dto = modelMapper.map(review, ReviewDto.class);
        dto.setServiceName(booking.getAd().getServiceName());
        dto.setBookingId(booking.getId());
        return dto;
    }


}

