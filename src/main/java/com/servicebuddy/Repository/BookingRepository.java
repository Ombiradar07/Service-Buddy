package com.servicebuddy.Repository;

import com.servicebuddy.Entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Long> {

    List<Booking> findAllByCompanyId(Long companyId);

    List<Booking> findAllByUserId(Long userId);
}
