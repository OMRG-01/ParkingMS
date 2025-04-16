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

	 @Query("SELECT b FROM Booking b WHERE DATE(b.bookingTime) = CURRENT_DATE")
	 List<Booking> findTodayBookings();
	 
	 List<Booking> findByPaymentStatus(int paymentStatus);
	 
	 @Query("SELECT b FROM Booking b " +
		       "WHERE (:fromDate IS NULL OR b.bookingTime >= :fromDate) " +
		       "AND (:toDate IS NULL OR b.bookingTime <= :toDate) " +
		       "AND (:paymentStatus IS NULL OR b.paymentStatus = :paymentStatus) " +
		       "AND (:userEmail IS NULL OR LOWER(b.user.email) LIKE LOWER(CONCAT('%', :userEmail, '%'))) " +
		       "ORDER BY b.bookingTime DESC")
		List<Booking> findFilteredBookings(
		    @Param("fromDate") LocalDateTime fromDate,
		    @Param("toDate") LocalDateTime toDate,
		    @Param("paymentStatus") Integer paymentStatus,
		    @Param("userEmail") String userEmail
		);

	 List<Booking> findAllByOrderByBookingTimeDesc();

	 
	 @Query("SELECT b FROM Booking b ORDER BY b.bookingTime DESC")
	 List<Booking> findAllBookings();

}