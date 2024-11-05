package invoice.services;

import invoice.models.ApiResponse;
import invoice.models.Invoice;
import invoice.models.Pagination;
import invoice.models.requests.InvoiceRequest;
import invoice.repo.InvoiceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Slf4j
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final MongoTemplate mongoTemplate;



    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository, MongoTemplate mongoTemplate) {
        this.invoiceRepository = invoiceRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public Invoice add(InvoiceRequest requestData, String sessionId) {
        log.info("[{}] Creating new invoice ", sessionId);

        Invoice invoice = new Invoice();
        invoice.setStocks(requestData.getStocks());
        invoice.setCost(requestData.getCost());

        Invoice savedInvoice = invoiceRepository.save(invoice);
        log.info("[{}] New invoice saved ", sessionId);
        return savedInvoice;
    }

    public ApiResponse<List<Invoice>> fetchInvoices(int page, int size, String sessionId) {
        Pageable pageable = PageRequest.of(page, size);

        Query query = new Query().with(pageable);


        List<Invoice> invoices = mongoTemplate.find(query, Invoice.class);

        Page<Invoice> pagedInvoice = PageableExecutionUtils.getPage(
                invoices, pageable, () -> mongoTemplate.count(query, Invoice.class));

        return new ApiResponse<List<Invoice>>().toBuilder()
                .data(pagedInvoice.getContent())
                .pagination(Pagination.page(pagedInvoice, page, sessionId))
                .build();
    }

    public Invoice getInvoice(String id, String sessionId) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invoice not found"));
        log.info("[{}] Invoice found. [id={}]", sessionId, id);
        return invoice;
    }

    public Invoice updateInvoice(String id, InvoiceRequest invoiceData, String sessionId) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice not found"));

        invoice.setStocks(invoiceData.getStocks());
        invoice.setCost(invoiceData.getCost());

        Invoice updatedInvoice = invoiceRepository.save(invoice);
        log.info("[{}] Invoice updated", sessionId);
        return updatedInvoice;
    }

    public Invoice deleteInvoice(String id, String sessionId){
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invoice not found") );

        invoiceRepository.deleteById(id);
        log.info("[{}] Invoice deleted", sessionId);
        return invoice;
    }


}
