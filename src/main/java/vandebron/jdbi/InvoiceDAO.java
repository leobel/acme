package vandebron.jdbi;

import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vandebron.api.InvoiceDTO;
import vandebron.api.QueryParamsRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by leobel on 5/16/17.
 */
public class InvoiceDAO extends AbstractDAO<Invoice> implements QueryParamsRepository<Invoice> {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceDAO.class);
    /**
     * Constructor.
     *
     * @param sessionFactory Hibernate session factory.
     */

    @Inject
    public InvoiceDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * Method looks for an invoice by her id.
     *
     * @param id the id of an invoice we are looking for.
     * @return Optional containing the found invoice or an empty Optional
     * otherwise.
     */
    public Optional<Invoice> findById(Serializable id) {
        return Optional.ofNullable(get(id));
    }


    /**
     * Method returns all invoices belongs to a customer.
     *
     * @return list of all invoices filters by customer.
     */
    public List<Invoice> findAll() {
        return list(namedQuery("vandebron.jdbi.Invoice.findAll"));
    }

    /**
     * Method returns all invoices belongs to a customer.
     *
     * @return list of all invoices filters by customer.
     */
    public List<Invoice> findByQueryParams(Long customerId) {
        return list(namedQuery("vandebron.jdbi.Invoice.findByCustomer")
                    .setParameter("customerId", customerId));
    }

    /**
     * Method returns all invoices belongs to a customer with specific address.
     *
     * @return list of all invoices filters by customer and address
     */
    public List<Invoice> findByQueryParams(Long customerId, Long addressId) {
        return list(namedQuery("vandebron.jdbi.Invoice.findByCustomerAndAddress")
                .setParameter("customerId", customerId)
                .setParameter("addressId", addressId));
    }

    /**
     * Method returns all invoices belongs to a customer with specific address.
     *
     * @return list of all invoices filters by customer and address
     */
    public List<Invoice> findByQueryParams(Long customerId, Integer month) {
        return list(namedQuery("vandebron.jdbi.Invoice.findByCustomerAndMonth")
                .setParameter("customerId", customerId)
                .setParameter("month", month));
    }

    /**
     * Method returns all invoices belongs to a customer with specific address.
     *
     * @return list of all invoices filters by customer and address
     */
    public List<Invoice> findByQueryParams(Long customerId, Integer month, String filter) {
        StringBuilder builder = new StringBuilder("%");
        builder.append(filter).append("%");
        return list(namedQuery("vandebron.jdbi.Invoice.findByCustomerAndMonthAndFilter")
                .setParameter("customerId", customerId)
                .setParameter("month", month)
                .setParameter("filter", builder.toString()));
    }


    /**
     * Method returns all invoices belongs to a invoice with specific address.
     *
     * @return list of all invoices filters by invoice and address
     */
    public Invoice upsert(Invoice invoice){
        return persist(invoice);
    }

}
