package vandebron;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import vandebron.api.InvoiceDTO;
import org.junit.Test;
import java.util.Locale;

import static io.dropwizard.testing.FixtureHelpers.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by leobel on 5/17/17.
 */
public class InvoiceDTOTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    private static final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withLocale(Locale.getDefault());

    @Test
    public void test_SerializesToJSON_Ok() throws Exception {
        final InvoiceDTO invoice = getInvoiceDTO();

        final String expected = MAPPER.writeValueAsString(MAPPER.readValue(fixture("fixtures/invoice.json"), InvoiceDTO.class));

        assertThat(MAPPER.writeValueAsString(invoice)).isEqualTo(expected);
    }

    @Test
    public void test_DeserializesFromJSON_Ok() throws Exception {
        final InvoiceDTO invoice = getInvoiceDTO();

        assertThat(MAPPER.readValue(fixture("fixtures/invoice.json"), InvoiceDTO.class)).isEqualTo(invoice);
    }

    private InvoiceDTO getInvoiceDTO(){
        DateTime dateTime =  DateTime.parse("2017-05-09 00:00:00", formatter);
        return new InvoiceDTO(1L,1L, 2L, "ShopPurchase", dateTime, dateTime, dateTime);
    }
}
