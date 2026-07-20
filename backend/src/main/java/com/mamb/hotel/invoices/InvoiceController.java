package com.mamb.hotel.invoices;

import com.mamb.hotel.exception.BadRequestException;
import com.mamb.hotel.exception.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final InvoiceRepository invoiceRepository;

    @PostMapping(value = "/reservation/{reservationId}/create")
    public Invoice create(@PathVariable final Long reservationId, @Valid @RequestBody final Invoice req) {
        if (req.getId() != null && invoiceRepository.existsById(req.getId()))
            throw new BadRequestException("Fattura già esistente");
        return invoiceService.create(reservationId, req);
    }

    @PostMapping("/{id}/update")
    public Invoice update(@PathVariable final Long id, @Valid @RequestBody Invoice req) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Fattura non presente"));
        req.setId(id);
        return invoiceService.update(invoice, req);
    }

    @PostMapping(value = "/{id}/delete")
    public void delete(@PathVariable final Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Fattura non presente"));
        invoiceService.delete(invoice);
    }

    @GetMapping(value = "/")
    public List<Invoice> list() {
        return invoiceService.list();
    }

    @GetMapping(value = "/reservation/{reservationId}")
    public List<Invoice> listByReservation(@PathVariable final Long reservationId) {
        return invoiceService.listByReservation(reservationId);
    }

}
