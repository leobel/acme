package vandebron.jdbi;

import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by leobel on 5/16/17.
 */

@Entity
@Table(name = "invoices")
@NamedQueries({
        @NamedQuery(name = "vandebron.jdbi.Invoice.findAll",
                query = "select i from Invoice i"),

        @NamedQuery(name = "vandebron.jdbi.Invoice.findByCustomer",
            query = "select i from Invoice i where i.customer.id = :customerId"),

        @NamedQuery(name = "vandebron.jdbi.Invoice.findByCustomerAndAddress",
                query = "select i from Invoice i where i.customer.id = :customerId and i.address.id = :addressId"),

        @NamedQuery(name = "vandebron.jdbi.Invoice.findByCustomerAndMonth",
                query = "select i from Invoice i where i.customer.id = :customerId and MONTH(i.invoiceDate) = :month"),

        @NamedQuery(name = "vandebron.jdbi.Invoice.findByCustomerAndMonthAndFilter",
                query = "select i from Invoice i where i.customer.id = :customerId and MONTH(i.invoiceDate) = :month and i.invoiceType like :filter")
})
public class Invoice {

    /**
     * Invoice's unique identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Invoice invoice type.
     */
    @Column(name = "invoice_type")
    private String invoiceType;

    /**
     * Invoice invoice date.
     */
    @Column(name = "invoice_date")
    private DateTime invoiceDate;

    /**
     * Invoice invoice start date.
     */
    @Column(name = "start_date")
    private DateTime startDate;

    /**
     * Invoice invoice end date.
     */
    @Column(name = "end_date")
    private DateTime endDate;

    /**
     * Invoice ManyToOne relationship.
     */
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Customer.class)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    /**
     * Invoice ManyToOne relationship.
     */
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Address.class)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    /**
     * A no-argument constructor.
     */
    public Invoice() {
    }

    /**
     * A constructor to create invoices. Id is not passed, cause it's
     * auto-generated by RDBMS.
     *
     */
    public Invoice(String invoiceType, DateTime invoiceDate, DateTime startDate, DateTime endDate, Customer customer, Address address){
        this.invoiceType = invoiceType;
        this.invoiceDate = invoiceDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.customer = customer;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public DateTime getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(DateTime invoiceDate) {
        this.invoiceDate = invoiceDate;
    }
}
