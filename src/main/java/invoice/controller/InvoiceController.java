package invoice.controller;

import invoice.InvoiceApplication;
import invoice.models.ApiResponse;
import invoice.models.Invoice;
import invoice.models.requests.InvoiceRequest;
import invoice.services.InvoiceService;
import io.swagger.annotations.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("api/v1/invoice")
public class InvoiceController {
    private final InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }


    @PostMapping
    public ResponseEntity<ApiResponse<Invoice>> create(@RequestBody @Valid InvoiceRequest invoiceBody, HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        log.info("[{}] Request to add invoice [invoice={}]", sessionId, invoiceBody);

        Invoice invoice = invoiceService.add(invoiceBody, sessionId);
        ApiResponse<Invoice> response = new ApiResponse<Invoice>().toBuilder().data(invoice).build();
        return ResponseEntity.ok(response);

    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Invoice>> update(@PathVariable String id,
                                                       @Valid @RequestBody InvoiceRequest requestData,
                                                       HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        log.info("[{}] Request to update invoice. [invoiceId={} invoice={}]", sessionId, id, requestData.toString());
        Invoice updatedInvoice = invoiceService.updateInvoice(id, requestData, sessionId);
        ApiResponse<Invoice> response = new ApiResponse<Invoice>().toBuilder().data(updatedInvoice).build();
        return ResponseEntity.ok(response);
    }

}
