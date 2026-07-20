package com.mamb.hotel.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ExtraServiceService {

    private final ExtraServiceRepository extraServiceRepository;

    public ExtraService create(final ExtraService req) {
        return extraServiceRepository.save(req);
    }

    public ExtraService update(final ExtraService req) {
        return extraServiceRepository.save(req);
    }

    public void delete(final ExtraService extraService) {
        extraServiceRepository.delete(extraService);
    }

    @Transactional(readOnly = true)
    public List<ExtraService> list() {
        return extraServiceRepository.findAll();
    }
}