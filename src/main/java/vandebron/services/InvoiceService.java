package vandebron.services;

import vandebron.jdbi.Invoice;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Created by leobel on 5/29/17.
 */
public interface InvoiceService {
    public Optional<Invoice> findById(Serializable id);
    public List<Invoice> findAll();
    public List<Invoice> findByQueryParams(Long customerId);
    public List<Invoice> findByQueryParams(Long customerId, Long addressId);
    public List<Invoice> findByQueryParams(Long customerId, Integer month);
    public List<Invoice> findByQueryParams(Long customerId, Integer month, String filter);
    public Invoice upsert(Invoice invoice);
}
