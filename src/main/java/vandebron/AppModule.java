package vandebron;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.ProvisionException;
import com.google.inject.Singleton;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Environment;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.builtin.PassThroughConverter;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import vandebron.api.AddressDTO;
import vandebron.api.CustomerDTO;
import vandebron.api.InvoiceDTO;
import vandebron.jdbi.Address;
import vandebron.jdbi.Client;
import vandebron.jdbi.Customer;
import vandebron.jdbi.Invoice;

import javax.validation.Validator;

/**
 * Created by leobel on 5/17/17.
 */
public class AppModule extends AbstractModule {

    /**
     * Hibernate bundle.
     */
    private HibernateBundle<AppConfiguration> hibernateBundle;


    public AppModule(){
        hibernateBundle = new HibernateBundle<AppConfiguration>(Customer.class, Address.class, Invoice.class, Client.class) {

            public DataSourceFactory getDataSourceFactory(AppConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        };
    }

    protected void configure() {

    }

    public HibernateBundle<AppConfiguration> getHibernateBundle(){
        return hibernateBundle;
    }

    @Provides
    public SessionFactory providesSessionFactory() {
        return hibernateBundle.getSessionFactory();
    }

    @Provides
    @Singleton
    public MapperFacade providesMapperFactory(){
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        mapperFactory.getConverterFactory().registerConverter(new PassThroughConverter(DateTime.class));

        mapperFactory.classMap(Invoice.class, InvoiceDTO.class)
                .fieldAToB("customer.id", "customerId")
                .fieldAToB("address.id", "addressId")
                .byDefault()
                .register();

        mapperFactory.classMap(Address.class, AddressDTO.class)
                .fieldAToB("customer.id", "customerId")
                .byDefault()
                .register();

        mapperFactory.classMap(Customer.class, CustomerDTO.class)
                .byDefault()
                .register();

        return mapperFactory.getMapperFacade();
    }

    @Provides
    public Validator providesValidator(Environment environment){
        return environment.getValidator();
    }
}
