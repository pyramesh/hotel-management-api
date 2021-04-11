package com.booking.recruitment.hotel.controller;

import com.booking.recruitment.hotel.model.Hotel;
import com.booking.recruitment.hotel.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Ramesh.Yaleru on 4/11/2021
 */
@RestController
@RequestMapping("/search")
public class HotelSearchController {

    private final HotelService hotelService;

    @Autowired
    public HotelSearchController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping("/{cityId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Hotel> searchHotels(@PathVariable("cityId") Long cityId) {
        return hotelService.getHotelsByCityId(cityId);
    }
}
