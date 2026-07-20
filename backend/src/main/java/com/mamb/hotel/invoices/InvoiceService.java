package com.mamb.hotel.invoices;

import com.mamb.hotel.reservations.Reservation;
import com.mamb.hotel.reservations.ReservationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ReservationRepository reservationRepository;

    public Invoice create(final Long reservationId, final Invoice req) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Prenotazione non presente"));
        req.setReservation(reservation);
        return invoiceRepository.save(req);
    }

    public Invoice update(final Invoice invoice, final Invoice req) {
        invoice.setInvoiceNumber(req.getInvoiceNumber());
        invoice.setInvoiceType(req.getInvoiceType());
        invoice.setStatus(req.getStatus());
        invoice.setIssueDate(req.getIssueDate());
        invoice.setCustomerName(req.getCustomerName());
        invoice.setCustomerTaxCode(req.getCustomerTaxCode());
        invoice.setBillingAddress(req.getBillingAddress());
        invoice.setTotalAmount(req.getTotalAmount());
        return invoiceRepository.save(invoice);
    }

    public void delete(final Invoice invoice) {
        invoiceRepository.delete(invoice);
    }

    @Transactional(readOnly = true)
    public List<Invoice> list() {
        return invoiceRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Invoice> listByReservation(final Long reservationId) {
        return invoiceRepository.findByReservation_Id(reservationId);
    }
}
