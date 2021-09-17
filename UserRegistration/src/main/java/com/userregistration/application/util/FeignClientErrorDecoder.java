package com.userregistration.application.util;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;
import com.userregistration.application.exception.CartDetailsNotFoundException;
import com.userregistration.application.exception.InSufficientBalanceException;
import com.userregistration.application.exception.InavalidArgumentException;
import com.userregistration.application.exception.OutOfStockException;
import com.userregistration.application.exception.ResourceNotFoundException;
import com.userregistration.application.exception.SearchResultsNotFoundException;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class FeignClientErrorDecoder implements ErrorDecoder {
	private final ErrorDecoder errorDecoder = new Default();

	@Override
	public Exception decode(String s, Response response) {

		String message = null;
		String description = null;
		List<String> err = new ArrayList<>();
		Reader reader = null;

		try {
			reader = response.body().asReader();
			// Easy way to read the stream and get a String object
			String result = CharStreams.toString(reader);
			// use a Jackson ObjectMapper to convert the Json String into a
			// Pojo
			ObjectMapper mapper = new ObjectMapper();
			// just in case you missed an attribute in the Pojo
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			// init the Pojo
			ErrorMessage exceptionMessage = mapper.readValue(result, ErrorMessage.class);

			StringBuilder sb = new StringBuilder();

			exceptionMessage.getErrors().stream().forEach(e -> {
				sb.append(e);
			});

			String strMsg = sb.toString();
			message = "";
			message = message.concat(strMsg);
			description = exceptionMessage.description;

		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			// It is the responsibility of the caller to close the stream.
			try {

				if (reader != null)
					reader.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String strError="File no found";

		switch (response.status())
		{
		
		case 404:
			return new OutOfStockException(message == null ? strError : message);
		case 402:
			return new InSufficientBalanceException(message == null ? strError : message);
		case 403:
			return new ResourceNotFoundException(message == null ? strError : message);
		case 420:
			return new InavalidArgumentException(message == null ? strError : message);
		case 419:
			return new SearchResultsNotFoundException(message == null ? strError : message);
		case 418:
			return new CartDetailsNotFoundException(message == null ? strError : message);	
		

		}

		return errorDecoder.decode(s, response);
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	public static class ErrorMessage {

		private String timestamp;
		private int status;
		private List<String> errors;
		private String message;
		private String path;
		private String description;

	}
}