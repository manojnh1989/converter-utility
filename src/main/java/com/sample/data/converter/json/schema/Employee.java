package com.sample.data.converter.json.schema;

import java.util.List;

public class Employee {

	private int id;

	private String name;

	private Address[] addresses;

	private List<String> phoneNumbers;

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

	public List<String> getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumbers(List<String> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

}
