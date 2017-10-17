package vandebron.jdbi;

import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Created by leobel on 5/16/17.
 */
public class AddressDAO extends AbstractDAO<Address>{

    /**
     * Constructor.
     *
     * @param sessionFactory Hibernate session factory.
     */

    @Inject
    public AddressDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * Method returns all address stored in the database.
     *
     * @return list of all address stored in the database
     */
    public List<Address> findAll() {
        return list(namedQuery("vandebron.jdbi.Address.findAll"));
    }

    /**
     * Method looks for an address by her id.
     *
     * @param id the id of an address we are looking for.
     * @return Optional containing the found address or an empty Optional
     * otherwise.
     */
    public Optional<Address> findById(Serializable id) {
        return Optional.ofNullable(get(id));
    }

    public Address upsert(Address address){
        return persist(address);
    }
}
