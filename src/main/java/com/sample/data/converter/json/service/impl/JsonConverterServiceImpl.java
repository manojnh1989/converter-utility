//package com.sample.data.converter.json.service.impl;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Service;
//import org.springframework.validation.annotation.Validated;
//
//import com.sample.data.converter.base.DataConverter;
//import com.sample.data.converter.base.DataConverterType;
//import com.sample.data.converter.json.JsonConfigurator;
//import com.sample.data.converter.json.service.JsonConverterService;
//
//@Service
//@Validated
//public class JsonConverterServiceImpl implements JsonConverterService {
//
//	private DataConverter jsonConverter;
//
//	@Autowired
//	public JsonConverterServiceImpl(@Qualifier(value = DataConverterType.JSON_CONVERTER) DataConverter jsonConverter) {
//		this.jsonConverter = jsonConverter;
//	}
//
//	@Override
//	public String convert(String srcJson) {
//		return jsonConverter.convert(srcJson);
//	}
//
//}
