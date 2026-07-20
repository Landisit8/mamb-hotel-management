package com.mamb.hotel.payments;

import com.mamb.hotel.exception.BadRequestException;
import com.mamb.hotel.exception.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;

    @PostMapping(value = "/reservation/{reservationId}/create")
    public Payment create(@PathVariable final Long reservationId, @Valid @RequestBody final Payment req) {
        if (req.getId() != null && paymentRepository.existsById(req.getId()))
            throw new BadRequestException("Pagamento già esistente");
        return paymentService.create(reservationId, req);
    }

    @PostMapping("/{id}/update")
    public Payment update(@PathVariable final Long id, @Valid @RequestBody Payment req) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pagamento non presente"));
        req.setId(id);
        return paymentService.update(payment, req);
    }

    @PostMapping(value = "/{id}/delete")
    public void delete(@PathVariable final Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pagamento non presente"));
        paymentService.delete(payment);
    }

    @GetMapping(value = "/")
    public List<Payment> list() {
        return paymentService.list();
    }

    @GetMapping(value = "/reservation/{reservationId}")
    public List<Payment> listByReservation(@PathVariable final Long reservationId) {
        return paymentService.listByReservation(reservationId);
    }

}
