package vandebron.resources;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;
import ma.glasnost.orika.MapperFacade;
import vandebron.api.AddressDTO;
import vandebron.api.CustomerDTO;
import vandebron.api.InvoiceDTO;
import vandebron.jdbi.Address;
import vandebron.jdbi.Customer;
import vandebron.jdbi.CustomerDAO;
import vandebron.jdbi.Invoice;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
public class CustomerController {
	
	private final Validator validator;
	private CustomerDAO customerDAO;
	private MapperFacade mapperFacade;

	@Inject
	public CustomerController(Validator validator, CustomerDAO customerDAO, MapperFacade mapperFacade){
		this.validator = validator;
		this.customerDAO = customerDAO;
		this.mapperFacade = mapperFacade;
	}

	/**
	 * Method looks for all customers.
	 *
	 * @return List containing all customers or an empty List
	 * otherwise.
	 */
	@GET
	@UnitOfWork
	public Response findAll(){
		return Response.ok(mapperFacade.mapAsList(customerDAO.findAll(), CustomerDTO.class)).build();
	}

	/**
     * Method looks for a customer by her id.
     *
     * @param id the id of the customer we are looking for.
     * @return The found customer or HTTP Not Found
     * otherwise.
     */
    @GET
    @Path("/{id}")
    @UnitOfWork
    public Response findById(@PathParam("id") LongParam id) {
        Optional<Customer> customer = customerDAO.findById(id.get());
		if(customer.isPresent()){
			Customer c = customer.get();
			CustomerDTO response = mapperFacade.map(c, CustomerDTO.class);
			return Response.ok(response).build();
		}
		else{
			return Response.status(Response.Status.NOT_FOUND).build();
		}
    }

    @POST
	@UnitOfWork
	public Response createCustomer(CustomerDTO customerDTO) throws URISyntaxException {
		Set<ConstraintViolation<CustomerDTO>> violations = validator.validate(customerDTO);
		if (violations.size() > 0) {
			ArrayList<String> validationMessages = new ArrayList<String>();
			for (ConstraintViolation<CustomerDTO> violation : violations) {
				validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
			}
			return Response.status(Response.Status.BAD_REQUEST).entity(validationMessages).build();
		}

		Customer customer = new Customer(customerDTO.getName(), customerDTO.getEmail());
		for(AddressDTO addressDTO: customerDTO.getAddresses()){
			Address address = new Address(addressDTO.getName(), customer);
			customer.getAddresses().add(address);
			for(InvoiceDTO invoiceDTO: customerDTO.getInvoices()){
				Invoice invoice = new Invoice(invoiceDTO.getInvoiceType(), invoiceDTO.getInvoiceDate(), invoiceDTO.getStartDate(), invoiceDTO.getEndDate(), customer, address);
				customer.getInvoices().add(invoice);
			}
		}
		customerDAO.upsert(customer);
		return  Response.created(new URI("/customers/" + customer.getId())).build();
	}
}	