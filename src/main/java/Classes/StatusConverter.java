package Classes;

import java.util.stream.Stream;


import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import Classes.Models.*;
@Converter(autoApply = true)
public class StatusConverter implements AttributeConverter<Status, String> {
  


	@Override
	public String convertToDatabaseColumn(Status attribute) {
		 if (attribute == null) {
	            return null;
	        }
	        return attribute.getCode();
	}

	@Override
	public Status convertToEntityAttribute(String code) {
		// TODO Auto-generated method stub
		 if (code == null) {
	            return null;
	        }
	 
	        return Stream.of(Status.values())
	          .filter(c -> c.getCode().equals(code))
	          .findFirst()
	          .orElseThrow(IllegalArgumentException::new);
	}
}
