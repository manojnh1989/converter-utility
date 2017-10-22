package com.sample.data.converter.json.schema;

public class Employee {
	
	
	private int id;
	
	private String name;
	
	private Address[] addresses;
	
	public Address[] getAddresses() {
		return addresses;
	}
	
	public void setAddresses(Address[] addresses) {
		this.addresses = addresses;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	

}
