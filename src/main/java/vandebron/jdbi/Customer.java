package vandebron.jdbi;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "customers")
@NamedQueries({
    @NamedQuery(name = "vandebron.jdbi.Customer.findAll",
            query = "SELECT c FROM Customer c")
})
public class Customer {

	/**
     * Customer's unique identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    
    /**
     * Customer name.
     */
    @Column(name = "name")
    private String name;

    /**
     * Customer email.
     */
    @Column(name = "email")
    private String email;

    /**
     * Customer OneToMany relationship.
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = CascadeType.ALL)
    private Set<Address> addresses = new HashSet<Address>(0);

    /**
     * Customer OneToMany relationship.
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = CascadeType.ALL)
    private Set<Invoice> invoices = new HashSet<Invoice>(0);

    /**
     * A no-argument constructor.
     */
    public Customer() {
    }


    /**
     * A constructor to create customers. Id is not passed, cause it's
     * auto-generated by RDBMS.
     *
     */
    public Customer(String name, String email) {
        this.name = name;
        this.email = email;
        addresses = new HashSet<Address>();
        invoices = new HashSet<Invoice>();
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public Set<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    public Set<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(Set<Invoice> invoices) {
        this.invoices = invoices;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
