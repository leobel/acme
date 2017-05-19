package vandebron;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.builtin.PassThroughConverter;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.joda.time.DateTime;
import org.junit.Test;
import vandebron.api.AddressDTO;
import vandebron.api.CustomerDTO;
import vandebron.api.InvoiceDTO;
import vandebron.jdbi.Address;
import vandebron.jdbi.Customer;
import vandebron.jdbi.Invoice;

import java.util.ArrayList;
import java.util.List;

import static io.dropwizard.testing.FixtureHelpers.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by leobel on 5/17/17.
 */
public class CustomerDTOTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();


    @Test
    public void testSerializesToJSON_Ok() throws Exception {
        final CustomerDTO customer = getCustomerDTO();

        final String expected = MAPPER.writeValueAsString(MAPPER.readValue(fixture("fixtures/customer.json"), CustomerDTO.class));

        assertThat(MAPPER.writeValueAsString(customer)).isEqualTo(expected);
    }

    @Test
    public void testDeserializesFromJSON_Ok() throws Exception {
        final CustomerDTO customer = getCustomerDTO();

        assertThat(MAPPER.readValue(fixture("fixtures/customer.json"), CustomerDTO.class)).isEqualTo(customer);
    }


    private CustomerDTO getCustomerDTO(){
        return new CustomerDTO(1L, "customer", "test@email.com");
    }

}
