package vandebron.resources;

import com.google.inject.Inject;
import io.dropwizard.hibernate.UnitOfWork;
import ma.glasnost.orika.MapperFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vandebron.api.InvoiceDTO;
import vandebron.jdbi.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

/**
 * Created by leobel on 5/16/17.
 */

@Path("/invoices")
@Produces(MediaType.APPLICATION_JSON)
public class InvoiceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceController.class);

    private Validator validator;
    private InvoiceDAO invoiceDAO;
    private CustomerDAO customerDAO;
    private AddressDAO addressDAO;
    private  ClientDAO clientDao;
    private MapperFacade mapperFacade;



    @Inject
    public InvoiceController(Validator validator, InvoiceDAO invoiceDAO, CustomerDAO customerDAO, AddressDAO addressDAO, ClientDAO clientDAO,MapperFacade mapperFacade){
        this.validator = validator;
        this.invoiceDAO = invoiceDAO;
        this.customerDAO = customerDAO;
        this.addressDAO = addressDAO;
        this.clientDao = clientDAO;
        this.mapperFacade = mapperFacade;
    }

    /**
     * Method looks for an Invoice by her id.
     *
     * @param id the id of an invoice we are looking for.
     * @return the found invoice or HTTP Not Found
     * otherwise.
     */
    @GET
    @Path("/{id}")
    @UnitOfWork
    public Response findById(@PathParam("id") long id) {
        Optional<Invoice> invoice = invoiceDAO.findById(id);
        if(invoice.isPresent()){
            Invoice i = invoice.get();
            InvoiceDTO response = mapperFacade.map(i, InvoiceDTO.class);
            return Response.ok(response).build();
        }
        else{
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    /**
     * Method looks for invoices matching the query params.
     *
     * @param customerId the id of an customer's invoices.
     * @param addressId the id of an address' invoices.
     * @param month the month of the invoices.
     * @param filter the type of the invoice (shop or monthly basis).
     * @return List containing the found invoices or an empty List
     * otherwise.
     */
    @GET
    @UnitOfWork
    public Response findByQueryParams(@QueryParam("customerId") Optional<Long> customerId, @QueryParam("addressId") Optional<Long> addressId,
                                                          @QueryParam("month") Optional<Integer> month, @QueryParam("filter") Optional<String> filter){
        if(customerId.isPresent() && !addressId.isPresent() && !month.isPresent() && !filter.isPresent())
            return Response.ok(mapperFacade.mapAsList(invoiceDAO.findByQueryParams(customerId.get()), InvoiceDTO.class)).build();

        else if(customerId.isPresent() && addressId.isPresent() && !month.isPresent() && !filter.isPresent()){
            return Response.ok(mapperFacade.mapAsList(invoiceDAO.findByQueryParams(customerId.get(), addressId.get()), InvoiceDTO.class)).build();
        }
        else if(customerId.isPresent() && month.isPresent() && !addressId.isPresent() && !filter.isPresent()){
            return Response.ok(mapperFacade.mapAsList(invoiceDAO.findByQueryParams(customerId.get(), month.get()), InvoiceDTO.class)).build();
        }
        else if(customerId.isPresent() && month.isPresent() && filter.isPresent() && !addressId.isPresent()){
            return Response.ok(mapperFacade.mapAsList(invoiceDAO.findByQueryParams(customerId.get(), month.get(), filter.get()), InvoiceDTO.class)).build();
        }
        else if(!customerId.isPresent() && !addressId.isPresent() && !month.isPresent() && !filter.isPresent()){
            return Response.ok(mapperFacade.mapAsList(invoiceDAO.findAll(), InvoiceDTO.class)).build();
        }
        else{
            return Response.ok(new ArrayList<InvoiceDTO>()).build();
        }
    }

    /**
     * Method create an invoice.
     *
     * @param invoiceDTO the data for the new invoice.
     * @return The URL for the just created invoice or HTTP Not Found in case of either the customer or the address associated to the invoice isn't found or
     * HTTP Bad Request if the input data doesn't follow the rules expressed in InvoiceDTO
     * otherwise.
     */
    @POST
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createInvoice(InvoiceDTO invoiceDTO) throws URISyntaxException{
        Set<ConstraintViolation<InvoiceDTO>> violations = validator.validate(invoiceDTO);
        if (violations.size() > 0) {
            ArrayList<String> validationMessages = new ArrayList<String>();
            for (ConstraintViolation<InvoiceDTO> violation : violations) {
                validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(validationMessages).build();
        }

        Optional<Client> client = clientDao.findById(invoiceDTO.getCustomerId(), invoiceDTO.getAddressId());
        if(!client.isPresent()) return Response.status(Response.Status.BAD_REQUEST).build();
        Optional<Customer> customer = customerDAO.findById(invoiceDTO.getCustomerId());
        Optional<Address> address = addressDAO.findById(invoiceDTO.getAddressId());
        Invoice invoice = new Invoice(invoiceDTO.getInvoiceType(), invoiceDTO.getInvoiceDate(), invoiceDTO.getStartDate(), invoiceDTO.getEndDate(), customer.get(), address.get());

        Invoice i = invoiceDAO.upsert(invoice);
        return  Response.created(new URI("/invoices/" + i.getId())).build();
    }

}
