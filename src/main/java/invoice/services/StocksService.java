package invoice.services;

import com.mongodb.client.MongoClient;
import invoice.models.ApiResponse;
import invoice.models.Pagination;
import invoice.models.Stock;
import invoice.models.Stocks;
import invoice.models.requests.StocksRequest;
import invoice.repo.StockRepository;
import invoice.repo.StocksRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class StocksService {

    private final StocksRepository stocksRepository;
    private final MongoTemplate mongoTemplate;
    private final StockRepository stockRepository;
    private final MongoClient mongo;

    @Autowired
    public StocksService(StocksRepository stocksRepository, MongoTemplate mongoTemplate, StockRepository stockRepository, MongoClient mongo) {
        this.stocksRepository = stocksRepository;
        this.mongoTemplate = mongoTemplate;
        this.stockRepository = stockRepository;
        this.mongo = mongo;
    }

    public List<Stocks> add( List<StocksRequest> stocksRequest, String sessionId) {
        List<Stocks> stocksArray = new ArrayList<Stocks>();
        stocksRequest.forEach(
                stocksRequest1 -> {
                    Stock stock = stockRepository.findById(stocksRequest1.getStockId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stock not found"));
                    Stocks stocks = new Stocks(stock, stocksRequest1.getQuantity());
                    Stocks savedStocks = stocksRepository.save(stocks);
                    log.info("[{}] New stock saved ", sessionId);
                    stocksArray.add(savedStocks);
                }
        );
        return stocksArray;
    }

    public ApiResponse<List<Stocks>> fetch(int page, int size, String search, String sessionId) {
        Pageable pageable = PageRequest.of(page, size);
        Query query = new Query().with(pageable);

        if (Objects.nonNull(search)) {
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where("stock.name").regex("%s".formatted(search))
            ));
        }

        List<Stocks> stocks = mongoTemplate.find(query, Stocks.class);

        Page<Stocks> pagedStocks = PageableExecutionUtils.getPage(
                stocks, pageable, () -> mongoTemplate.count(new Query(), Stocks.class)
        );

        return new ApiResponse<List<Stocks>>().toBuilder().data(pagedStocks.getContent())
                .pagination(Pagination.page(pagedStocks, page, sessionId)).build();

    }

    public Stocks get(String id, String sessionId) {
        Stocks stocks = stocksRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Stocks not found"));
        log.info("[{}] Content found. [id={}]", sessionId, id);
        return  stocks;
    }

    public Stocks update(String id, StocksRequest request, String sessionId) {
        Stocks stocks = stocksRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stocks not found"));

        stocks.setQuantity(request.getQuantity());

        Stocks updatedStocks = stocksRepository.save(stocks);
        log.info("[{}] Stocks updated", sessionId);
        return updatedStocks;
    }

    public Stocks delete(String id, String sessionId) {
        Stocks stocks = stocksRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Stocks not found"));
        stockRepository.deleteById(id);
        return stocks;
    }

}
