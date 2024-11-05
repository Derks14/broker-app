package invoice.controller;

import invoice.models.ApiResponse;
import invoice.models.Stock;
import invoice.models.requests.StockRequest;
import invoice.services.StockService;
import io.swagger.annotations.Api;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/stock")
@Slf4j
public class StockController {
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Stock>> create(@RequestBody @Valid StockRequest stockRequest,
                                                     HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        log.info("[{}] Request to add stock [stock={}]", sessionId, stockRequest);

        Stock content = stockService.add(stockRequest, sessionId);
        ApiResponse<Stock> response = new ApiResponse<Stock>().toBuilder().data(content).build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Stock>>> fetch(@RequestParam(defaultValue = "0", required = false) int page,
                                                          @RequestParam(defaultValue = "10", required = false) int size,
                                                          @RequestParam(required = false) String search,
                                                          HttpServletRequest request
                                                          ) {
        String sessionId = request.getSession().getId();
        log.info("[{}] Request to fetch stocks [ ]", sessionId);

        ApiResponse<List<Stock>> response = stockService.fetch(page, size,search, sessionId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Stock>> getStock(@PathVariable String id, HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        log.info("[{}] Request to get one stock [contentId={}]", sessionId, id);
        Stock stock = stockService.getStock(id, sessionId);
        ApiResponse<Stock> response = new ApiResponse<Stock>().toBuilder().data(stock).build();
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Stock>> update(@PathVariable String id,
                                                     @RequestBody @Valid StockRequest stockRequest,
                                                     HttpServletRequest request){
        String sessionId = request.getSession().getId();
        log.info("[{}] Request to update stock [stock={}]", sessionId, stockRequest);
        Stock updatedStock = stockService.updateStock(id, stockRequest, sessionId);
        ApiResponse<Stock> response = new ApiResponse<Stock>().toBuilder().data(updatedStock).build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Stock>> delete(@PathVariable String id,
                                                     @RequestBody @Valid StockRequest stockRequest,
                                                     HttpServletRequest servletRequest) {
        String sessionId = servletRequest.getSession().getId();
        log.info("[{}] Request to delete content. [contentId={}]", sessionId, id);

        Stock deletedStock = stockService.deleteStock(id, sessionId);
        ApiResponse<Stock> response = new ApiResponse<Stock>().toBuilder().data(deletedStock).build();
        return ResponseEntity.ok(response);
    }
}
