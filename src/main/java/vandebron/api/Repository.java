package vandebron.api;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Created by leobel on 5/18/17.
 */
public interface Repository<T> {

    Optional<T> findById(Serializable id);

    List<T> findAll();

    T upsert(T item);
}
