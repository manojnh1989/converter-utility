package com.sample.data.converter.base.factory;

import com.sample.data.converter.base.DataConverter;
import com.sample.data.converter.base.DataConverterType;

public interface DataConverterFactory {

	public DataConverter create(DataConverterType converterType);
}
