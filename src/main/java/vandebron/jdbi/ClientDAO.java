package vandebron.jdbi;

import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import vandebron.api.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Created by leobel on 5/18/17.
 */
public class ClientDAO extends AbstractDAO<Client> implements Repository<Client> {

    @Inject
    public ClientDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * Method looks for an client by her id.
     *
     * @param customerId the id of an client we are looking for.
     * @return Optional containing the found client or an empty Optional
     * otherwise.
     */
    public Optional<Client> findById(Long customerId, Long addressId) {
        return Optional.ofNullable(get(new Client.ClientPk(customerId, addressId)));
    }

    public Optional<Client> findById(Serializable id) {
        return null;
    }

    public List<Client> findAll() {
        return null;
    }

    public Client upsert(Client item) {
        return null;
    }
}
