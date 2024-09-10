package com.servicebuddy.Service.Company;

import com.servicebuddy.DTO.AdDto;
import com.servicebuddy.DTO.BookingDto;
import com.servicebuddy.Entity.Ad;
import com.servicebuddy.Entity.Booking;
import com.servicebuddy.Entity.Users;
import com.servicebuddy.Enum.BookingStatus;
import com.servicebuddy.Exception.ResourceNotFoundException;
import com.servicebuddy.Repository.AdRepository;
import com.servicebuddy.Repository.BookingRepository;
import com.servicebuddy.Repository.UserRepository;
import com.servicebuddy.Service.Utils.RedisService;
import com.servicebuddy.Service.Utils.S3Service;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private S3Service s3Service ;

    @Autowired
    RedisService redisService;
    private
    @Autowired
    ModelMapper modelMapper;

    private static final String ALL_ADS_CACHE_KEY="ALL_ADS";

    public AdDto postAd(Long userId, AdDto adDto) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found"));

        Ad newAd = modelMapper.map(adDto, Ad.class);

        if ((adDto.getImg() != null) && !adDto.getImg().isEmpty()) {
            newAd.setImgUrl(s3Service.uploadFile( adDto.getImg()));
        }
        newAd.setUser(user);
        AdDto newAdDto = modelMapper.map(adRepository.save(newAd), AdDto.class);
        newAdDto.setCompanyName(user.getName());
        newAdDto.setImgUrl(newAd.getImgUrl());
        return newAdDto;
    }

    @Override
    public AdDto updateAd(AdDto adDto, Long adId) {
        Ad existingAd = adRepository.findById(adId).orElseThrow(() -> new ResourceNotFoundException("Ad not found"));

        String oldImgUrl = existingAd.getImgUrl();
        String oldImgName =  oldImgUrl.substring(oldImgUrl.lastIndexOf("/") + 1);

        if ((adDto.getImg() != null) && !adDto.getImg().isEmpty()) {
            s3Service.deleteFile(oldImgName);
            existingAd.setImgUrl(s3Service.uploadFile(adDto.getImg()));
        }

        existingAd.setServiceName(adDto.getServiceName());
        existingAd.setDescription(adDto.getDescription());
        existingAd.setPrice(adDto.getPrice());

        AdDto updatedAdDto = modelMapper.map(adRepository.save(existingAd), AdDto.class);
        updatedAdDto.setCompanyName(existingAd.getUser().getName());
        updatedAdDto.setImgUrl(existingAd.getImgUrl());

        // Update the cache
        List<Ad> allAds = adRepository.findAll();
        List<AdDto> adDtoList = allAds.stream().map(a -> {
            AdDto dto = modelMapper.map(a, AdDto.class);
            dto.setCompanyName(a.getUser().getName());
            return dto;
        }).toList();

        redisService.setData(ALL_ADS_CACHE_KEY, adDtoList, 5);

        return updatedAdDto;
    }

    @Override
    public List<AdDto> getAllAdsByUserId(Long userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found"));
        List<Ad> ads = adRepository.findAllByUserId(user.getId());

        if (ads.isEmpty()) {
            throw new ResourceNotFoundException("Sorry you don't have any ads !!!");
        } else {
            return ads.stream().map(ad -> {
                AdDto adDto = modelMapper.map(ad, AdDto.class);
                adDto.setCompanyName(user.getName());
                adDto.setImgUrl(ad.getImgUrl());
                return adDto;
            }).toList();
        }
    }

    @Override
    public AdDto getAdById(Long adId) {
        Ad ad = adRepository.findById(adId).orElseThrow(() -> new ResourceNotFoundException("Ad not found"));

        AdDto adDto = modelMapper.map(ad, AdDto.class);
        adDto.setImgUrl(ad.getImgUrl());
        adDto.setCompanyName(ad.getUser().getName());
        return adDto;
    }

    @Override
    public void deleteAd(Long adId) {


        adRepository.deleteById(adId);
    }

    public List<BookingDto> getAllBookings(Long companyId) {

        Users company = userRepository.findById(companyId).orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        List<Booking> bookings = bookingRepository.findAllByCompanyId(company.getId());

        if (bookings.isEmpty()) {
            throw new ResourceNotFoundException("Sorry you don't have any bookings !!!");
        } else {
            return bookings.stream().map(booking -> modelMapper.map(booking, BookingDto.class)).toList();
        }

    }

    public BookingStatus changeBookingStatus(Long bookingId, String status) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        if (status.equalsIgnoreCase("APPROVE")) {
            booking.setBookingStatus(BookingStatus.APPROVED);

        } else if(status.equalsIgnoreCase("COMPLETED")) {
            //TODO: Implement otp system and verify from user the service is Completed or not ?

            booking.setBookingStatus(BookingStatus.COMPLETED);
        }
        else {
            booking.setBookingStatus(BookingStatus.REJECTED);
        }

        Booking updatedBooking = bookingRepository.save(booking);
        return updatedBooking.getBookingStatus();

    }


}

