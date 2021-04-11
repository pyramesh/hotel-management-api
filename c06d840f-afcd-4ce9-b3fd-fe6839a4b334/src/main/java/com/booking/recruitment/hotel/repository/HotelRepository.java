package com.booking.recruitment.hotel.repository;

import com.booking.recruitment.hotel.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    List<Hotel> findByCity_IdAndDeleted(Long cityId, Boolean deleteFlg);

    static String HAVERSINE_PART = "(6371 * acos(cos(radians(:latitude)) * cos(radians(c.latitude)) * cos(radians(c.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(c.latitude))))";

    @Query("SELECT c FROM Hotel c WHERE" + HAVERSINE_PART + " < :distance ORDER BY " + HAVERSINE_PART + " DESC")
    public List<Hotel> findHotelWithNearestLocation(@Param("latitude") double latitude,
                                                    @Param("longitude") double longitude, @Param("distance") double distance);
}
