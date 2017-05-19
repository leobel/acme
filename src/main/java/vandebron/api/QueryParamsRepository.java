package vandebron.api;

import vandebron.jdbi.Invoice;

import java.util.List;

/**
 * Created by leobel on 5/18/17.
 */
public interface QueryParamsRepository<T> extends Repository<T> {

    List<T> findByQueryParams(Long param);
    List<T> findByQueryParams(Long param1, Long param2);
    List<T> findByQueryParams(Long param1, Integer param2);
    List<T> findByQueryParams(Long param1, Integer param2, String param3);
}
