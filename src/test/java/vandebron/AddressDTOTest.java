package vandebron;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;
import vandebron.api.AddressDTO;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by leobel on 5/17/17.
 */
public class AddressDTOTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();


    @Test
    public void test_SerializesToJSON_Ok() throws Exception {
        final AddressDTO address = getAddressDTO();
        final String expected = MAPPER.writeValueAsString(MAPPER.readValue(fixture("fixtures/address.json"), AddressDTO.class));

        assertThat(MAPPER.writeValueAsString(address)).isEqualTo(expected);
    }

    @Test
    public void test_DeserializesFromJSON_Ok() throws Exception {
        final AddressDTO address = getAddressDTO();

        assertThat(MAPPER.readValue(fixture("fixtures/address.json"), AddressDTO.class)).isEqualTo(address);
    }

    private AddressDTO getAddressDTO(){
        return new AddressDTO(1, "address", 1);
    }
}
