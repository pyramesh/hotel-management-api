package com.booking.recruitment.hotel.service.impl;

import com.booking.recruitment.hotel.util.HaversineAlgo;
import com.booking.recruitment.hotel.exception.BadRequestException;
import com.booking.recruitment.hotel.model.Hotel;
import com.booking.recruitment.hotel.repository.HotelRepository;
import com.booking.recruitment.hotel.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
class DefaultHotelService implements HotelService {
    private final HotelRepository hotelRepository;
    HaversineAlgo haversineAlgo = new HaversineAlgo();

    @Autowired
    DefaultHotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @Override
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    @Override
    public List<Hotel> getHotelsByCity(Long cityId) {
        return hotelRepository.findAll().stream()
                .filter((hotel) -> cityId.equals(hotel.getCity().getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Hotel> getHotelsByCityId(Long cityId) {
        List<Hotel> hotels = hotelRepository.findByCity_IdAndDeleted(cityId, Boolean.FALSE);
        return hotels.stream().map(entity -> mapToTarget(entity)).sorted(Comparator.comparingDouble(Hotel::getDistance))
                .limit(3)
                .collect(Collectors.toList());
    }

    private Hotel mapToTarget(Hotel hotel) {
        hotel.setDistance(haversineAlgo.calculateDistance(hotel.getLatitude(), hotel.getLongitude(), hotel.getCity().getCityCentreLatitude(), hotel.getCity().getCityCentreLatitude()));
        return hotel;
    }

    @Override
    public Hotel createNewHotel(Hotel hotel) {
        if (hotel.getId() != null) {
            throw new BadRequestException("The ID must not be provided when creating a new Hotel");
        }

        return hotelRepository.save(hotel);
    }

    @Override
    public Hotel getHotelsById(Long hotelId) {
        return hotelRepository.findById(hotelId).orElse(null);
    }

    @Override
    public void deleteHotelById(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId).orElse(null);
        if (hotel != null) {
            hotel.setDeleted(Boolean.TRUE);
            hotelRepository.save(hotel);
        }
    }
}
