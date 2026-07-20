package com.mamb.hotel.services;

import com.mamb.hotel.exception.BadRequestException;
import com.mamb.hotel.exception.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/extra-services")
@RequiredArgsConstructor
public class ExtraServiceController {

    private final ExtraServiceService extraServiceService;
    private final ExtraServiceRepository extraServiceRepository;

    @PostMapping(value = "/create")
    public ExtraService create(@Valid @RequestBody final ExtraService req) {
        if (req.getId() != null && extraServiceRepository.existsById(req.getId()))
            throw new BadRequestException("Servizio già esistente");
        return extraServiceService.create(req);
    }

    @PostMapping("/{id}/update")
    public ExtraService update(@PathVariable final Long id, @Valid @RequestBody ExtraService req) {
        extraServiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Servizio non presente"));
        req.setId(id);
        return extraServiceService.update(req);
    }

    @PostMapping(value = "/{id}/delete")
    public void delete(@PathVariable final Long id) {
        ExtraService extraService = extraServiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Servizio non presente"));
        extraServiceService.delete(extraService);
    }

    @GetMapping(value = "/")
    public List<ExtraService> list() {
        return extraServiceService.list();
    }

}
