package vandebron.jdbi;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import com.google.inject.Inject;
import org.hibernate.SessionFactory;
import io.dropwizard.hibernate.AbstractDAO;

public class CustomerDAO extends AbstractDAO<Customer>{

	/**
     * Constructor.
     *
     * @param sessionFactory Hibernate session factory.
     */

	@Inject
	public CustomerDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	
	/**
     * Method returns all customers stored in the database.
     *
     * @return list of all customers stored in the database
     */
    public List<Customer> findAll() {
        return list(namedQuery("vandebron.jdbi.Customer.findAll"));
    }
    
    /**
     * Method looks for an customer by her id.
     *
     * @param id the id of an customer we are looking for.
     * @return Optional containing the found customer or an empty Optional
     * otherwise.
     */
    public Optional<Customer> findById(Serializable id) {
    	 return Optional.ofNullable(get(id));

    }
    
    public Customer upsert(Customer customer){
    	return persist(customer);
    }

}
