package com.parking.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.parking.demo.model.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    // You can add custom queries later if needed
	List<Booking> findByParkingAreaIdAndBookedUntilAfter(Long parkingAreaId, LocalDateTime time);

	
	@Query("SELECT b FROM Booking b WHERE b.parkingArea.id = :parkingAreaId AND b.slot = :slot AND b.bookedUntil > :now")
	List<Booking> findActiveBookingForSlot(@Param("parkingAreaId") Long parkingAreaId,
	                                       @Param("slot") String slot,
	                                       @Param("now") LocalDateTime now);

	List<Booking> findByUserId(Long userId);

	 long countByBookedUntilAfter(LocalDateTime currentDateTime);

}