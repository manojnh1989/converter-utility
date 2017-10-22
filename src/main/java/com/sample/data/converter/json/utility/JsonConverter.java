package com.sample.data.converter.json.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sample.data.converter.base.DataConverter;
import com.sample.data.converter.json.JsonConfigurator;
import com.sample.data.converter.json.schema.Address;
import com.sample.data.converter.json.schema.Employee;

@Component
public class JsonConverter implements DataConverter {

	private JsonConfigurator jsonConfigurator;

	@Autowired
	public JsonConverter(JsonConfigurator jsonConfigurator) {
		this.jsonConfigurator = jsonConfigurator;
	}

	@PostConstruct
	public void init() {

		Employee emp = new Employee();

		emp.setId(123);
		emp.setName("manoj");

		Address[] addresses = new Address[2];
		addresses[0] = new Address();
		addresses[0].setStName("usvd");
		addresses[0].setHouseNum(1234);

		addresses[1] = new Address();
		addresses[1].setStName("asfdasf");
		addresses[1].setHouseNum(123456);
		
		List<String> phoneNumbers = new ArrayList<>();
		phoneNumbers.add("+91-9*******");
		phoneNumbers.add("+91-95327467");
		
		emp.setPhoneNumbers(phoneNumbers);

		emp.setAddresses(addresses);

		JsonElement srcJson = new Gson().toJsonTree(emp);
		
		JsonObject jsonObject = new JsonObject();

		traverseSrcJson(srcJson, jsonObject,
				new JsonConverterMetaContext.Builder().index(0).srcFormat("").build());
		
		System.out.println("Print" + jsonObject.toString());
		
		

	}

	@Override
	public String convert(String srcDataStream) {
		return null;
	}

	// public static void main(String[] args) {
	//
	//
	//
	//
	// new JsonConverter().traverseSrcJson(srcJson, new JsonObject(),
	// new JsonConverterMetaContext.Builder().index(0).srcFormat("").build());
	//
	// }

	public void traverseSrcJson(JsonElement srcJson, JsonElement destJson, JsonConverterMetaContext context) {

		if (srcJson.isJsonObject()) {

			((JsonObject) srcJson).entrySet().forEach(entry -> {

				String jsonElemKey = context.getSrcFormat() + entry.getKey() + '.';

				JsonConverterMetaContext jsonContext = new JsonConverterMetaContext.Builder().index(context.getIndex())
						.srcFormat(jsonElemKey).build();

				JsonElement jsonElement = entry.getValue();

				traverseSrcJson(jsonElement, destJson, jsonContext);

			});
		} else if (srcJson.isJsonArray()) {

			JsonArray jsonArray = (JsonArray) srcJson;

			jsonArray.forEach(jsonArrElem -> {

				traverseSrcJson(jsonArrElem, destJson, context);

				context.incrementIndex();

			});

		} else if (srcJson.isJsonPrimitive()) {

			String srcFormat1 = StringUtils.chop(context.getSrcFormat());

			System.out.println(
					"Key -> " + srcFormat1 + " Value -> " + srcJson.toString() + " Index -> " + context.getIndex());

			JsonElement jsonElement = destJson;
			
			String[] strArray = StringUtils.split(jsonConfigurator.getConfiguratorMap().get(srcFormat1), ".");

			for (String strKey : Arrays.asList(strArray)) {

				if (StringUtils.endsWith(strKey, "#value") || StringUtils.endsWith(strKey, "#key"))

					jsonElement = formDestJson(jsonElement, new JsonConverterMetaContext.Builder().destFormat(strKey)
							.primitiveSrcJsonElem(srcJson).index(context.getIndex()).build());
				else
					jsonElement = formDestJson(jsonElement,
							new JsonConverterMetaContext.Builder().destFormat(strKey).index(context.getIndex()).build());

			}

		} else if (srcJson.isJsonNull()) {

		}

	}

	private JsonElement formDestJson(JsonElement parentJsonElement, JsonConverterMetaContext converterMetaContext) {

		String keyFormat = converterMetaContext.getDestFormat();

		if (StringUtils.endsWith(keyFormat, "#value")) {

			// Json array
			((JsonArray) parentJsonElement).add(converterMetaContext.getPrimitiveSrcJsonElem());

			return converterMetaContext.getPrimitiveSrcJsonElem();

		} else if (StringUtils.endsWith(keyFormat, "#key")) {

			// Json primitive

			String propertyName = StringUtils.removeEnd(converterMetaContext.getDestFormat(), "#key");

			((JsonObject) parentJsonElement).add(propertyName, converterMetaContext.getPrimitiveSrcJsonElem());

			return converterMetaContext.getPrimitiveSrcJsonElem();

		} else if (StringUtils.endsWith(keyFormat, "#array")) {

			// Json array

			int index = converterMetaContext.getIndex();

			// Strip the property name with extra characters.
			String propertyName = StringUtils.removeEnd(converterMetaContext.getDestFormat(), "#array");

			if (parentJsonElement.isJsonArray()) {

				// Check if the object exists.
				JsonArray jsonArray = (JsonArray) parentJsonElement;

				if (index < jsonArray.size()) {
					// Item exists. Don't do anything.

					return jsonArray.get(index);

				} else {

					JsonArray array = new JsonArray();

					// Create the item..
					jsonArray.add(array);

					return array;
				}

			} else if (parentJsonElement.isJsonObject()) {

				// Check if the object exists.
				JsonObject parentObject = (JsonObject) parentJsonElement;

				JsonArray childObject = (JsonArray) parentObject.get(propertyName);

				if (childObject == null) {
					// If item doesn't exists. Create one.

					childObject = new JsonArray();

					parentObject.add(propertyName, childObject);

				}

				return childObject;
			}

		} else if (StringUtils.endsWith(keyFormat, "#object")) {

			// Json object.

			int index = converterMetaContext.getIndex();

			// Strip the property name with extra characters.
			String propertyName = StringUtils.removeEnd(converterMetaContext.getDestFormat(), "#object");

			if (parentJsonElement.isJsonObject()) {

				// Check if the object exists.
				JsonObject parentObject = (JsonObject) parentJsonElement;

				JsonObject childObject = (JsonObject) parentObject.get(propertyName);

				if (childObject == null) {
					// If item doesn't exists. Create one.

					childObject = new JsonObject();

					parentObject.add(propertyName, childObject);

				}

				return childObject;
			} else if (parentJsonElement.isJsonArray()) {

				// Check if the object exists.
				JsonArray parentObject = (JsonArray) parentJsonElement;

				if (index < parentObject.size()) {
					return parentObject.get(index);
				} else {

					JsonObject childObject = new JsonObject();

					parentObject.add(childObject);

					return childObject;
				}

			}
		}

		return null;

	}

	private static class JsonConverterMetaContext {

		private int index;

		private String srcFormat;

		private String destFormat;

		private JsonElement primitiveSrcJsonElem;

		private JsonConverterMetaContext(int index, String srcFormat, String destFormat,
				JsonElement primitiveSrcJsonElem) {
			super();
			this.index = index;
			this.srcFormat = srcFormat;
			this.destFormat = destFormat;
			this.primitiveSrcJsonElem = primitiveSrcJsonElem;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public String getSrcFormat() {
			return srcFormat;
		}

		public void setSrcFormat(String srcFormat) {
			this.srcFormat = srcFormat;
		}

		public String getDestFormat() {
			return destFormat;
		}

		public void setDestFormat(String destFormat) {
			this.destFormat = destFormat;
		}

		public void incrementIndex() {
			++this.index;
		}

		public void setPrimitiveSrcJsonElem(JsonElement primitiveSrcJsonElem) {
			this.primitiveSrcJsonElem = primitiveSrcJsonElem;
		}

		public JsonElement getPrimitiveSrcJsonElem() {
			return primitiveSrcJsonElem;
		}

		private static class Builder {

			private int index;

			private String srcFormat;

			private String destFormat;

			private JsonElement primitiveSrcJsonElem;

			private Builder() {
			}

			private Builder(int index, String srcFormat, String destFormat, JsonElement primitiveSrcJsonElem) {
				super();
				this.index = index;
				this.srcFormat = srcFormat;
				this.destFormat = destFormat;
				this.primitiveSrcJsonElem = primitiveSrcJsonElem;
			}

			private Builder index(int index) {
				this.index = index;
				return this;
			}

			public Builder srcFormat(String srcFormat) {
				this.srcFormat = srcFormat;
				return this;
			}

			public Builder destFormat(String destFormat) {
				this.destFormat = destFormat;
				return this;
			}

			public Builder primitiveSrcJsonElem(JsonElement primitiveSrcJsonElem) {
				this.primitiveSrcJsonElem = primitiveSrcJsonElem;
				return this;
			}

			private JsonConverterMetaContext build() {
				return new JsonConverterMetaContext(this.index, this.srcFormat, this.destFormat,
						this.primitiveSrcJsonElem);
			}
		}

	}

}
