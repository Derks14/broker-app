package invoice.repo;

import invoice.models.Stocks;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StocksRepository extends MongoRepository<Stocks, String> {

}
