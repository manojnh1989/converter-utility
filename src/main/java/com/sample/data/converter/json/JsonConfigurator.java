package com.sample.data.converter.json;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

@Component
public class JsonConfigurator {

	private Map<String,String> configuratorMap ;
	
	@PostConstruct
	public void initConfigurator() {
		
		configuratorMap = new HashMap<>();
		
		configuratorMap.put("id", "employeeDetails#object.id#key");
		configuratorMap.put("name", "employeeDetails#object.name#key");
		configuratorMap.put("addresses.stName", "employeeDetails#object.addresses#array.#object.stName#key");
		configuratorMap.put("addresses.houseNum", "employeeDetails#object.addresses#array.#object.houseNum#key");
	}
	
	public Map<String, String> getConfiguratorMap() {
		return configuratorMap;
	}
	
	
}
