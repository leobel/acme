package vandebron.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

import javax.validation.constraints.NotNull;

/**
 * Created by leobel on 5/16/17.
 */
public class InvoiceDTO {

    @NotNull
    private long id;

    @NotNull
    private long customerId;

    @NotNull
    private long addressId;

    @NotNull
    private String invoiceType;

    @NotNull
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "default")
    private DateTime invoiceDate;

    @NotNull
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "default")
    private DateTime startDate;

    @NotNull
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "default")
    private DateTime endDate;

    public InvoiceDTO(){

    }

    public InvoiceDTO(long id, long customerId, long addressId, String invoiceType, DateTime invoiceDate, DateTime startDate, DateTime endDate){
        this.id = id;
        this.customerId = customerId;
        this.addressId = addressId;
        this.invoiceType = invoiceType;
        this.invoiceDate = invoiceDate;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof InvoiceDTO){
            InvoiceDTO other = (InvoiceDTO)obj;
            return this.id == other.id && customerId == other.customerId
                    && addressId == other.addressId && invoiceType.equals(other.invoiceType)
                    && invoiceDate.equals(other.invoiceDate) && startDate.equals(other.startDate) && endDate.equals(other.endDate);
        }
        else return false;
    }

    public DateTime getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(DateTime invoiceDate) {
        this.invoiceDate = invoiceDate;
    }
}
