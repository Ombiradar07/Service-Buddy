package com.servicebuddy.Service.Company;

import com.servicebuddy.DTO.AdResponseDto;
import com.servicebuddy.DTO.AdRequestDto;
import com.servicebuddy.DTO.BookingResponseDto;
import com.servicebuddy.Entity.Ad;
import com.servicebuddy.Entity.Booking;
import com.servicebuddy.Entity.Users;
import com.servicebuddy.Enum.BookingStatus;
import com.servicebuddy.Exception.ResourceNotFoundException;
import com.servicebuddy.Repository.AdRepository;
import com.servicebuddy.Repository.BookingRepository;
import com.servicebuddy.Repository.UserRepository;
import com.servicebuddy.Service.Utils.EmailService;
import com.servicebuddy.Service.Utils.RedisService;
import com.servicebuddy.Service.Utils.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EmailService emailService;

    private static final String ALL_ADS_CACHE_KEY = "ALL_ADS";

    @Override
    public AdResponseDto postAd(Long userId, AdRequestDto adRequestDto) {
        log.info("Posting a new ad for user with ID: {}", userId);

        Users user = userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with ID {} not found", userId);
            return new ResourceNotFoundException("user not found");
        });

        Ad newAd = modelMapper.map(adRequestDto, Ad.class);

        if ((adRequestDto.getImg() != null) && !adRequestDto.getImg().isEmpty()) {
            log.info("Uploading new image to S3");
            newAd.setImgUrl(s3Service.uploadFile(adRequestDto.getImg()));
        }

        newAd.setUser(user);
        Ad savedAd = adRepository.save(newAd);

        AdResponseDto newAdResponseDto = modelMapper.map(savedAd, AdResponseDto.class);
        newAdResponseDto.setCompanyName(user.getName());
        newAdResponseDto.setImgUrl(newAd.getImgUrl());

        List<String> allEmails = userRepository.findAllEmails();
        String subject = "Hey there, we have a new Service for you";
        String body = "Dear User,\n\n" +
                "A new service has been posted: " + savedAd.getServiceName() + ".\n" +
                "Description: " + savedAd.getDescription() + "\n" +
                "Thank you for using our platform!\n\n" +
                "Best regards,\n" +
                "The ServiceBuddy Team";

        allEmails.forEach(email -> emailService.sendEmail(email, subject, body));

        log.info("Ad successfully posted and emails sent");
        return newAdResponseDto;
    }

    @Override
    public AdResponseDto updateAd(AdRequestDto adRequestDto, Long adId) {
        log.info("Updating ad with ID: {}", adId);

        Ad existingAd = adRepository.findById(adId).orElseThrow(() -> {
            log.error("Ad with ID {} not found", adId);
            return new ResourceNotFoundException("Ad not found");
        });

        if ((adRequestDto.getImg() != null) && !adRequestDto.getImg().isEmpty()) {
            String oldImgUrl = existingAd.getImgUrl();
            String oldImgName = oldImgUrl.substring(oldImgUrl.lastIndexOf("/") + 1);
            log.info("Replacing image for ad ID: {}", adId);
            s3Service.deleteFile(oldImgName);
            existingAd.setImgUrl(s3Service.uploadFile(adRequestDto.getImg()));
        }

        existingAd.setServiceName(adRequestDto.getServiceName());
        existingAd.setDescription(adRequestDto.getDescription());
        existingAd.setPrice(adRequestDto.getPrice());

        AdResponseDto updatedAdResponseDto = modelMapper.map(adRepository.save(existingAd), AdResponseDto.class);
        updatedAdResponseDto.setCompanyName(existingAd.getUser().getName());
        updatedAdResponseDto.setImgUrl(existingAd.getImgUrl());

        // Update cache
        List<Ad> allAds = adRepository.findAll();
        List<AdResponseDto> adResponseDtoList = allAds.stream().map(a -> {
            AdResponseDto dto = modelMapper.map(a, AdResponseDto.class);
            dto.setCompanyName(a.getUser().getName());
            return dto;
        }).toList();

        redisService.setData(ALL_ADS_CACHE_KEY, adResponseDtoList, 5);
        log.info("Ad with ID: {} updated successfully", adId);

        return updatedAdResponseDto;
    }

    @Override
    public List<AdResponseDto> getAllAdsByUserId(Long userId) {
        log.info("Fetching all ads for user ID: {}", userId);

        Users user = userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with ID {} not found", userId);
            return new ResourceNotFoundException("user not found");
        });

        List<Ad> ads = adRepository.findAllByUserId(user.getId());

        if (ads.isEmpty()) {
            log.warn("No ads found for user ID: {}", userId);
            throw new ResourceNotFoundException("Sorry you don't have any ads !!!");
        }

        log.info("Fetched {} ads for user ID: {}", ads.size(), userId);
        return ads.stream().map(ad -> {
            AdResponseDto adResponseDto = modelMapper.map(ad, AdResponseDto.class);
            adResponseDto.setCompanyName(user.getName());
            adResponseDto.setImgUrl(ad.getImgUrl());
            return adResponseDto;
        }).toList();
    }

    @Override
    public AdResponseDto getAdById(Long adId) {
        log.info("Fetching ad with ID: {}", adId);

        Ad ad = adRepository.findById(adId).orElseThrow(() -> {
            log.error("Ad with ID {} not found", adId);
            return new ResourceNotFoundException("Ad not found");
        });

        AdResponseDto adResponseDto = modelMapper.map(ad, AdResponseDto.class);
        adResponseDto.setImgUrl(ad.getImgUrl());
        adResponseDto.setCompanyName(ad.getUser().getName());

        log.info("Ad with ID: {} fetched successfully", adId);
        return adResponseDto;
    }

    @Override
    public void deleteAd(Long adId) {
        log.info("Deleting ad with ID: {}", adId);
        adRepository.deleteById(adId);
        log.info("Ad with ID: {} deleted successfully", adId);
    }

    @Override
    public List<BookingResponseDto> getAllBookings(Long companyId) {
        log.info("Fetching all bookings for company ID: {}", companyId);

        Users company = userRepository.findById(companyId).orElseThrow(() -> {
            log.error("Company with ID {} not found", companyId);
            return new ResourceNotFoundException("Company not found");
        });
        List<Booking> bookings = bookingRepository.findAllByCompanyId(company.getId());

        if (bookings.isEmpty()) {
            log.warn("No bookings found for company ID: {}", companyId);
            throw new ResourceNotFoundException("Sorry you don't have any bookings !!!");
        }

        log.info("Fetched {} bookings for company ID: {}", bookings.size(), companyId);
        return bookings.stream().map(booking -> modelMapper.map(booking, BookingResponseDto.class)).toList();
    }

    @Override
    public BookingStatus changeBookingStatus(Long bookingId, String status) {
        log.info("Changing booking status for booking ID: {} to status: {}", bookingId, status);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        Users user = booking.getUser();
        Ad ad = booking.getAd();
        String userEmail = user.getEmail();

        if (status.equalsIgnoreCase("APPROVE")) {
            booking.setBookingStatus(BookingStatus.APPROVED);
            log.info("Booking ID: {} approved", bookingId);

            String subject = "Booking Approved";
            String body = "Dear " + user.getName() + ",\n\n" +
                    "Your booking for the service \"" + ad.getServiceName() + "\" has been approved.\n" +
                    "Our team will contact you soon to schedule the service.\n\n" +
                    "Thank you for choosing our service.\n\n" +
                    "Best regards,\n" +
                    "The ServiceBuddy Team";
            emailService.sendEmail(userEmail, subject, body);
        } else if (status.equalsIgnoreCase("COMPLETED")) {
            booking.setBookingStatus(BookingStatus.COMPLETED);
            log.info("Booking ID: {} marked as completed", bookingId);

            String subject = "Service Completed";
            String body = "Dear " + user.getName() + ",\n\n" +
                    "The service \"" + ad.getServiceName() + "\" has been successfully completed.\n" +
                    "We hope you had a great experience.\n\n" +
                    "Best regards,\n" +
                    "The ServiceBuddy Team";
            emailService.sendEmail(userEmail, subject, body);
        } else {
            booking.setBookingStatus(BookingStatus.REJECTED);
            log.warn("Booking ID: {} rejected", bookingId);

            String subject = "Booking Rejected";
            String body = "Dear " + user.getName() + ",\n\n" +
                    "Your booking for the service \"" + ad.getServiceName() + "\" has been rejected.\n" +
                    "We apologize for any inconvenience.\n\n" +
                    "Best regards,\n" +
                    "The ServiceBuddy Team";
            emailService.sendEmail(userEmail, subject, body);
        }

        Booking updatedBooking = bookingRepository.save(booking);
        log.info("Booking status for ID: {} updated to {}", bookingId, booking.getBookingStatus());

        return updatedBooking.getBookingStatus();
    }
}
