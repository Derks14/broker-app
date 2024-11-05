package invoice.repo;

import invoice.models.Stock;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<Stock, String> {
}
