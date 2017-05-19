package vandebron.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class AddressDTO {

	@NotNull
	private long id;
	
	@NotNull
	private String name;

	@NotNull
	private long customerId;

	public AddressDTO(){
		
	}

	public AddressDTO(long id, String name, long customerId){
		this.id = id;
		this.name = name;
		this.customerId = customerId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setCustomerId(long customerId){
		this.customerId = customerId;
	}
	
	public long getCustomerId(){
		return customerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof AddressDTO){
			AddressDTO other = (AddressDTO) obj;
			return this.id == other.id && name.equals(other.name) && customerId == other.customerId;
		}
		else return false;
	}
}
