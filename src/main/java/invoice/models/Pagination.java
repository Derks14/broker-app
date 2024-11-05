package invoice.models;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
@Slf4j
public record Pagination(Long totalRecords, Integer page, Integer totalPages, Boolean hasNext, Boolean hasPrevious,
                         Integer size) {
    public static Pagination page(Page pageable, Integer page, String sessionId) {
        Pagination pagination = new Pagination(
                pageable.getTotalElements(),
                page,
                pageable.getTotalPages(),
                pageable.hasNext(),
                pageable.hasPrevious(),
                pageable.getSize());
        log.info("[{}] Computed pagination for fetched data", sessionId);
        return pagination;
    }
}