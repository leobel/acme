package vandebron.jdbi;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by leobel on 5/18/17.
 */

@Entity
@Table(name = "customers_address")
@IdClass(Client.ClientPk.class)
public class Client {

    @Id
    @Column(name = "customer_id")
    private long customerId;

    @Id
    @Column(name = "address_id")
    private long addressId;

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

    static class ClientPk implements Serializable{

        private long customerId;

        private long addressId;


        public ClientPk() {}

        public ClientPk(long customerId, long addressId) {
            this.customerId = customerId;
            this.addressId = addressId;
        }
    }
}
