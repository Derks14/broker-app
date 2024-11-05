package invoice.services;

import invoice.models.ApiResponse;
import invoice.models.Pagination;
import invoice.models.Stock;
import invoice.models.requests.StockRequest;
import invoice.models.requests.StocksRequest;
import invoice.repo.StockRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class StockService {
    private final StockRepository stockRepository;
    private final MongoTemplate mongoTemplate;

    public StockService(StockRepository repository, MongoTemplate mongoTemplate) {
        this.stockRepository = repository;
        this.mongoTemplate = mongoTemplate;
    }

    public Stock add(StockRequest stockRequest, String sessionId) {
        Stock stock = new Stock();
        stock.setName(stockRequest.getName());
        stock.setPrice(stockRequest.getPrice());
        stock.setGrowthRate(stock.getGrowthRate());

        Stock savedStock = stockRepository.save(stock);
        log.info("[{}] New stock saved ", sessionId);
        return savedStock;
    }

    public ApiResponse<List<Stock>> fetch(int page, int size,String search, String sessionId) {
        Pageable pageable = PageRequest.of(page, size);
        Query query = new Query().with(pageable);

        if (Objects.nonNull(search)) {
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where("name").regex("%s".formatted(search))
            ));
        }

        List<Stock> stocks = mongoTemplate.find(query, Stock.class);

        Page<Stock> pagedStock = PageableExecutionUtils.getPage(
                stocks, pageable, () -> mongoTemplate.count(new Query(), Stock.class));

        return new ApiResponse<List<Stock>>().toBuilder()
                .data(pagedStock.getContent())
                .pagination(Pagination.page(pagedStock, page, sessionId))
                .build();
    }

    public Stock getStock(String id, String sessionId) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stock not found"));
        log.info("[{}] Stock found. [id={}]", sessionId, id);
        return stock;
    }

    public Stock updateStock(String id, StockRequest request, String sessionId) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stock not found"));

        stock.setGrowthRate(request.getGrowthRate());
        stock.setPrice(request.getPrice());
        stock.setName(request.getName());

        Stock updatedStock = stockRepository.save(stock);
        log.info("[{}] Content updated", sessionId);
        return updatedStock;
    }

    public Stock deleteStock(String id, String sessionId) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stock not found"));

        stockRepository.deleteById(id);
        log.info("[{}] Stock deleted", sessionId);
        return stock;

    }
}
