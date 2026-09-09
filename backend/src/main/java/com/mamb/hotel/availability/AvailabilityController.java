package com.mamb.hotel.availability;


import com.mamb.hotel.rooms.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/availability")
@RequiredArgsConstructor
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    // [MAMB-02] Endpoint REST per interrogare le camere libere
    // non c'è il tab "Body" da compilare questa volta, passiamo le date direttamente nell'URL come parametri query
    @GetMapping("/rooms")
    public List<Room> getAvailableRooms(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate endDate) {
        return availabilityService.getAvailableRooms(startDate, endDate);
    }
}
