package vandebron;

import com.squarespace.jersey2.guice.JerseyGuiceUtils;
import io.dropwizard.testing.junit.ResourceTestRule;
import ma.glasnost.orika.MapperFacade;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import vandebron.api.InvoiceDTO;
import vandebron.jdbi.*;
import vandebron.resources.InvoiceController;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validator;
import javax.validation.metadata.ConstraintDescriptor;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import java.net.URI;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by leobel on 5/17/17.
 */
public class InvoiceControllerTest {

    static {
        JerseyGuiceUtils.reset();
    }
    private static final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withLocale(Locale.getDefault());
    private static final InvoiceDAO invoiceDAO = mock(InvoiceDAO.class);
    private static final CustomerDAO customerDAO = mock(CustomerDAO.class);
    private static final AddressDAO addressDAO = mock(AddressDAO.class);
    private static final ClientDAO clientDAO = mock(ClientDAO.class);
    private static final Validator validator = mock(Validator.class);
    private static final MapperFacade mapperFacade = mock(MapperFacade.class);

    @ClassRule
    public static final ResourceTestRule invoiceController = ResourceTestRule.builder()
            .addResource(new InvoiceController(validator, invoiceDAO, customerDAO, addressDAO, clientDAO, mapperFacade))
            .build();

    @Test
    public void testFindById_ReturnInvoice(){
        InvoiceDTO invoiceDTO = getInvoiceDTO();
        Invoice invoice = getInvoice();

        when(invoiceDAO.findById(eq(invoiceDTO.getId()))).thenReturn(Optional.of(invoice));
        when(mapperFacade.map(invoice, InvoiceDTO.class)).thenReturn(invoiceDTO);

        InvoiceDTO response = invoiceController.target("/invoices/1").request().get(Response.class).readEntity(InvoiceDTO.class);
        assertThat(response).isEqualTo(invoiceDTO);
        verify(invoiceDAO).findById(eq(invoiceDTO.getId()));
    }


    @Test
    public void testFindById_InvoiceNotFound(){
        long id = 2;
        when(invoiceDAO.findById(eq(id))).thenReturn(Optional.<Invoice>empty());

        Response response = invoiceController.target("/invoices/" + id).request().get(Response.class);
        assertThat(response.hasEntity()).isEqualTo(false);
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        verify(invoiceDAO).findById(eq(id));
    }

    @Test
    public void testFindByQueryParams_CustomerId_ReturnMatchingInvoices(){
        InvoiceDTO invoiceDTO = getInvoiceDTO();
        List<InvoiceDTO> invoicesDTO = new ArrayList<InvoiceDTO>(Arrays.asList(invoiceDTO));
        List<Invoice> invoices = new ArrayList<Invoice>(Arrays.asList(getInvoice()));

        when(invoiceDAO.findByQueryParams(eq(invoiceDTO.getCustomerId()))).thenReturn(invoices);
        when(mapperFacade.mapAsList(invoices, InvoiceDTO.class)).thenReturn(invoicesDTO);

        List<InvoiceDTO> response = invoiceController.target("/invoices").queryParam("customerId", invoiceDTO.getCustomerId()).request().get(Response.class).readEntity(new GenericType<List<InvoiceDTO>>(){});
        assertThat(response.get(0)).isEqualTo(invoiceDTO);
        verify(invoiceDAO).findByQueryParams(eq(invoiceDTO.getCustomerId()));
    }

    @Test
    public void testFindByQueryParams_CustomerIdAndAddressId_ReturnMatchingInvoices(){
        InvoiceDTO invoiceDTO = getInvoiceDTO();
        List<InvoiceDTO> invoicesDTO = new ArrayList<InvoiceDTO>(Arrays.asList(invoiceDTO));
        List<Invoice> invoices = new ArrayList<Invoice>(Arrays.asList(getInvoice()));

        when(invoiceDAO.findByQueryParams(eq(invoiceDTO.getCustomerId()), eq(invoiceDTO.getAddressId()))).thenReturn(invoices);
        when(mapperFacade.mapAsList(invoices, InvoiceDTO.class)).thenReturn(invoicesDTO);

        List<InvoiceDTO> response = invoiceController.target("/invoices").queryParam("customerId", invoiceDTO.getCustomerId()).queryParam("addressId", invoiceDTO.getAddressId()).request().get(Response.class).readEntity(new GenericType<List<InvoiceDTO>>(){});
        assertThat(response.get(0)).isEqualTo(invoiceDTO);
        verify(invoiceDAO).findByQueryParams(eq(invoiceDTO.getCustomerId()), eq(invoiceDTO.getAddressId()));
    }

    @Test
    public void testFindByQueryParams_CustomerIdAndMonth_ReturnMatchingInvoices(){
        InvoiceDTO invoiceDTO = getInvoiceDTO();
        List<InvoiceDTO> invoicesDTO = new ArrayList<InvoiceDTO>(Arrays.asList(invoiceDTO));
        List<Invoice> invoices = new ArrayList<Invoice>(Arrays.asList(getInvoice()));

        when(invoiceDAO.findByQueryParams(eq(invoiceDTO.getCustomerId()), eq(invoiceDTO.getStartDate().getMonthOfYear()))).thenReturn(invoices);
        when(mapperFacade.mapAsList(invoices, InvoiceDTO.class)).thenReturn(invoicesDTO);

        List<InvoiceDTO> response = invoiceController.target("/invoices").queryParam("customerId", invoiceDTO.getCustomerId()).queryParam("month", invoiceDTO.getStartDate().getMonthOfYear()).request().get(Response.class).readEntity(new GenericType<List<InvoiceDTO>>(){});
        assertThat(response.get(0)).isEqualTo(invoiceDTO);
        verify(invoiceDAO).findByQueryParams(eq(invoiceDTO.getCustomerId()), eq(invoiceDTO.getStartDate().getMonthOfYear()));
    }

    @Test
    public void testFindByQueryParams_CustomerIdAndMonthAndFilter_ReturnMatchingInvoices(){
        InvoiceDTO invoiceDTO = getInvoiceDTO();
        List<InvoiceDTO> invoicesDTO = new ArrayList<InvoiceDTO>(Arrays.asList(invoiceDTO));
        List<Invoice> invoices = new ArrayList<Invoice>(Arrays.asList(getInvoice()));
        String filter = "shop";

        when(invoiceDAO.findByQueryParams(eq(invoiceDTO.getCustomerId()), eq(invoiceDTO.getStartDate().getMonthOfYear()), eq(filter))).thenReturn(invoices);
        when(mapperFacade.mapAsList(invoices, InvoiceDTO.class)).thenReturn(invoicesDTO);

        List<InvoiceDTO> response = invoiceController.target("/invoices").queryParam("customerId", invoiceDTO.getCustomerId()).queryParam("month", invoiceDTO.getStartDate().getMonthOfYear()).queryParam("filter", filter).request().get(Response.class).readEntity(new GenericType<List<InvoiceDTO>>(){});
        assertThat(response.get(0)).isEqualTo(invoiceDTO);
        verify(invoiceDAO).findByQueryParams(eq(invoiceDTO.getCustomerId()), eq(invoiceDTO.getStartDate().getMonthOfYear()), eq(filter));
    }

    @Test
    public void testFindByQueryParams_NoParams_ReturnAllInvoices(){
        InvoiceDTO invoiceDTO = getInvoiceDTO();
        List<InvoiceDTO> invoicesDTO = new ArrayList<InvoiceDTO>(Arrays.asList(invoiceDTO));
        List<Invoice> invoices = new ArrayList<Invoice>(Arrays.asList(getInvoice()));

        when(invoiceDAO.findAll()).thenReturn(invoices);
        when(mapperFacade.mapAsList(invoices, InvoiceDTO.class)).thenReturn(invoicesDTO);

        List<InvoiceDTO> response = invoiceController.target("/invoices").request().get(Response.class).readEntity(new GenericType<List<InvoiceDTO>>(){});
        assertThat(response.get(0)).isEqualTo(invoiceDTO);
        verify(invoiceDAO).findAll();
    }

    @Test
    public void testCreateInvoice_Invoice_ReturnCreatedInvoiceLocation(){
        InvoiceDTO invoiceDTO = getInvoiceDTO();
        Customer customer = getCustomer();
        Address address = getAddress();
        Invoice invoice = getInvoice();
        invoice.setId(invoiceDTO.getId());

        when(validator.validate(invoiceDTO)).thenReturn(new HashSet<ConstraintViolation<InvoiceDTO>>());
        when(customerDAO.findById(eq(invoiceDTO.getCustomerId()))).thenReturn(Optional.of(customer));
        when(addressDAO.findById(eq(invoiceDTO.getAddressId()))).thenReturn(Optional.of(address));
        when(clientDAO.findById(eq(invoiceDTO.getCustomerId()), eq(invoiceDTO.getAddressId()))).thenReturn(Optional.of(getClient()));
        when(invoiceDAO.upsert(any(Invoice.class))).thenReturn(invoice);

        Response response = invoiceController.target("/invoices").request().post(Entity.entity(invoiceDTO, "application/json"));
        URI dir = response.getLocation();
        assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        assertThat(dir.getPath()).isEqualTo("/invoices/1");
        verify(customerDAO).findById(eq(invoiceDTO.getCustomerId()));
        verify(addressDAO).findById(eq(invoiceDTO.getAddressId()));
        verify(invoiceDAO).upsert(any(Invoice.class));
    }

    @Test
    public void testCreateInvoice_Invoice_ValidationBadRequest(){
        InvoiceDTO invoiceDTO = getInvoiceDTO();

        when(validator.validate(invoiceDTO)).thenReturn(new HashSet<ConstraintViolation<InvoiceDTO>>(new ArrayList<ConstraintViolation<InvoiceDTO>>(Arrays.asList(new ConstraintViolation<InvoiceDTO>() {
            public String getMessage() {
                return "bad request";
            }

            public String getMessageTemplate() {
                return null;
            }

            public InvoiceDTO getRootBean() {
                return null;
            }

            public Class<InvoiceDTO> getRootBeanClass() {
                return null;
            }

            public Object getLeafBean() {
                return null;
            }

            public Object[] getExecutableParameters() {
                return new Object[0];
            }

            public Object getExecutableReturnValue() {
                return null;
            }

            public Path getPropertyPath() {
                return new Path() {
                    public Iterator<Node> iterator() {
                        return null;
                    }
                };
            }

            public Object getInvalidValue() {
                return null;
            }

            public ConstraintDescriptor<?> getConstraintDescriptor() {
                return null;
            }

            public <U> U unwrap(Class<U> aClass) {
                return null;
            }
        }))));


        Response response = invoiceController.target("/invoices").request().post(Entity.entity(invoiceDTO, "application/json"));
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void testCreateInvoice_Invoice_ClientBadRequest(){
        InvoiceDTO invoiceDTO = getInvoiceDTO();

        when(clientDAO.findById(eq(invoiceDTO.getCustomerId()), eq(invoiceDTO.getAddressId()))).thenReturn(Optional.<Client>empty());

        Response response = invoiceController.target("/invoices").request().post(Entity.entity(invoiceDTO, "application/json"));
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        verify(clientDAO).findById(eq(invoiceDTO.getCustomerId()), eq(invoiceDTO.getAddressId()));
    }

    @After
    public void tearDown(){
        // we have to reset the mock after each test because of the
        // @ClassRule, or use a @Rule as mentioned below.
        reset(invoiceDAO);
        reset(customerDAO);
        reset(addressDAO);
        reset(clientDAO);
        reset(validator);
        reset(mapperFacade);

    }

    private InvoiceDTO getInvoiceDTO(){
        DateTime dateTime =  DateTime.parse("2017-05-09 00:00:00", formatter);
        return new InvoiceDTO(1L,1L, 2L, "ShopPurchase", dateTime, dateTime, dateTime);
    }

    private Invoice getInvoice(){
        DateTime dateTime =  DateTime.parse("2017-05-09 00:00:00", formatter);
        Customer customer = getCustomer();
        return new Invoice("ShopPurchase", dateTime, dateTime, dateTime, getCustomer(), getAddress());
    }

    private Customer getCustomer(){
        return new Customer("customer", "test@email.com");
    }

    private Address getAddress(){
        Customer customer = getCustomer();
        return new Address("address", customer);
    }

    private Client getClient(){
        return new Client();
    }
}
