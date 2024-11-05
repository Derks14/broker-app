package invoice.controller;

import invoice.models.ApiResponse;
import invoice.models.Stocks;
import invoice.models.requests.StocksRequest;
import invoice.services.StocksService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stocks")
@Slf4j
public class StocksController {
    private final StocksService stocksService;

    public StocksController(StocksService stocksService) {
        this.stocksService = stocksService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<List<Stocks>>> create(@RequestBody @Valid List<StocksRequest> requestBody, HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        log.info("[{}] Request to add content [content={}]", sessionId, requestBody);

        List<Stocks> stocks = stocksService.add(requestBody, sessionId);
        ApiResponse<List<Stocks>> response = new ApiResponse<List<Stocks>>().toBuilder().data(stocks).build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public  ResponseEntity<ApiResponse<Stocks>> update(@PathVariable String id,
                                                       @Valid @RequestBody StocksRequest requestData,
                                                       HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        log.info("[{}] Request to update stocks. [stocksId={} stock={}]", sessionId, id, requestData.toString());

        Stocks updatedStocks = stocksService.update(id, requestData, sessionId);
        ApiResponse<Stocks> response = new ApiResponse<Stocks>().toBuilder().data(updatedStocks).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Stocks>>> fetchAll(@RequestParam(defaultValue = "0", required = false) int page,
                                                                 @RequestParam(defaultValue = "10", required = false) int size,
                                                                 @RequestParam(required = false) String search,
                                                                 HttpServletRequest request
                                                                 ) {
        String sessionId = request.getSession().getId();
        log.info("[{}] Request to fetch contents [ ]", sessionId);

        ApiResponse<List<Stocks>> response = stocksService.fetch(page, size, search, sessionId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Stocks>> fetchStocks(@PathVariable String id,
                                                           @Valid @RequestBody StocksRequest stocksRequest,
                                                           HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        log.info("[{}] Request to get stocks. [stocksId={}]", sessionId, id);

        Stocks stocks = stocksService.get(id, sessionId);
        ApiResponse<Stocks> response = new ApiResponse<Stocks>()
                .toBuilder().data(stocks).build();
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Stocks>> delete(@PathVariable String id, HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        log.info("[{}] Request to delete stocks. [stocksId={}]", sessionId, id);

        Stocks deletedStocks = stocksService.delete(id, sessionId);
        ApiResponse<Stocks> response = new ApiResponse<Stocks>().toBuilder().data(deletedStocks).build();
        return ResponseEntity.ok(response);
    }
}
