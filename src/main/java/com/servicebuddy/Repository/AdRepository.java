package com.servicebuddy.Repository;

import com.servicebuddy.Entity.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {

    List<Ad> findAllByUserId(Long userId);

    @Query("SELECT a FROM Ad a WHERE a.serviceName LIKE %:keyword% OR a.description LIKE %:keyword%")
    List<Ad> searchByKeyword(@Param("keyword") String keyword);
}