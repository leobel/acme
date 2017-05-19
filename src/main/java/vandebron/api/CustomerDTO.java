package vandebron.api;

import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import vandebron.jdbi.Customer;

public class CustomerDTO {
	
	@NotNull
	private long id;

	@NotBlank
	private String name;

	@Pattern(regexp=".+@.+\\.[a-z]+")
	private String email;
	
	private List<AddressDTO> addresses;

	private List<InvoiceDTO> invoices;
	
	public CustomerDTO(){
		
	}
	
	public CustomerDTO(Long id, String name, String email){
		this.id = id;
		this.name = name;
		this.email = email;
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

	public List<AddressDTO> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<AddressDTO> addresses) {
		this.addresses = addresses;
	}

	public List<InvoiceDTO> getInvoices() {
		return invoices;
	}

	public void setInvoices(List<InvoiceDTO> invoices) {
		this.invoices = invoices;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CustomerDTO){
			CustomerDTO other = (CustomerDTO)obj;
			return this.id ==other.id && name.equals(other.name) && email.equals(other.email);
		}
		else return false;
	}
}
