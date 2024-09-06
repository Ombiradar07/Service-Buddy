package com.servicebuddy.Service.Company;

import com.servicebuddy.DTO.AdDto;
import com.servicebuddy.DTO.BookingDto;
import com.servicebuddy.Entity.Ad;
import com.servicebuddy.Entity.Booking;
import com.servicebuddy.Entity.Users;
import com.servicebuddy.Enum.BookingStatus;
import com.servicebuddy.Exception.ImageProcessingException;
import com.servicebuddy.Exception.ResourceNotFoundException;
import com.servicebuddy.Repository.AdRepository;
import com.servicebuddy.Repository.BookingRepository;
import com.servicebuddy.Repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
    ModelMapper modelMapper;

    public AdDto postAd(Long userId, AdDto adDto) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found"));

        Ad newAd = modelMapper.map(adDto, Ad.class);

        try {
            if ((adDto.getImg() != null) && !adDto.getImg().isEmpty()) {
                newAd.setImg(adDto.getImg().getBytes());
            }
        } catch (IOException e) {
            throw new ImageProcessingException("Error uploading the Image");
        }
        newAd.setUser(user);
        AdDto newAdDto = modelMapper.map(adRepository.save(newAd), AdDto.class);
        newAdDto.setCompanyName(user.getName());
        newAdDto.setImgResponse(newAd.getImg());
        return newAdDto;
    }

    @Override
    public AdDto updateAd(AdDto adDto, Long adId) {
        Ad existingAd = adRepository.findById(adId).orElseThrow(() -> new ResourceNotFoundException("Ad not found"));

        try {
            if ((adDto.getImg() != null) && !adDto.getImg().isEmpty()) {
                existingAd.setImg(adDto.getImg().getBytes());
            }
        } catch (IOException e) {
            throw new ImageProcessingException("Error uploading the Image");
        }

        existingAd.setServiceName(adDto.getServiceName());
        existingAd.setDescription(adDto.getDescription());
        existingAd.setPrice(adDto.getPrice());
        AdDto updatedAdDto = modelMapper.map(adRepository.save(existingAd), AdDto.class);
        updatedAdDto.setCompanyName(existingAd.getUser().getName());
        updatedAdDto.setImgResponse(existingAd.getImg());
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
                adDto.setImgResponse(ad.getImg());
                return adDto;
            }).toList();
        }
    }

    @Override
    public AdDto getAdById(Long adId) {
        Ad ad = adRepository.findById(adId).orElseThrow(() -> new ResourceNotFoundException("Ad not found"));

        AdDto adDto = modelMapper.map(ad, AdDto.class);
        adDto.setImgResponse(ad.getImg());
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
        } else {
            booking.setBookingStatus(BookingStatus.REJECTED);
        }

        Booking uodatedBooking = bookingRepository.save(booking);
        return uodatedBooking.getBookingStatus();

    }


}

