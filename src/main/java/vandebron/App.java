package vandebron;

import com.hubspot.dropwizard.guice.GuiceBundle;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.builtin.PassThroughConverter;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vandebron.api.AddressDTO;
import vandebron.api.CustomerDTO;
import vandebron.api.InvoiceDTO;
import vandebron.jdbi.*;
import vandebron.resources.CustomerController;
import vandebron.resources.InvoiceController;


public class App extends Application<AppConfiguration> {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    /**
     *  Guice module;
     */

    private AppModule module;


    public static void main(String[] args) throws Exception {
        new App().run(args);
    }


    @Override
    public void run(AppConfiguration configuration, Environment environment) throws Exception {
        LOGGER.info("Registering REST resources");
        environment.jersey().register(CustomerController.class);
        environment.jersey().register(InvoiceController.class);
    }

    @Override
    public void initialize(Bootstrap<AppConfiguration> bootstrap) {
        module = new AppModule();
        GuiceBundle<AppConfiguration> guiceBundle = GuiceBundle.<AppConfiguration>newBuilder()
                .addModule(module)
                .setConfigClass(AppConfiguration.class)
                .build();
        bootstrap.addBundle(module.getHibernateBundle());
        bootstrap.addBundle(guiceBundle);
    }
}

