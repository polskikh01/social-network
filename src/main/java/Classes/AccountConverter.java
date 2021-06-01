package Classes;

import java.util.stream.Stream;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import Classes.Models.AccountState;
import Classes.Models.Status;

@Converter(autoApply = true)
public class AccountConverter implements AttributeConverter<AccountState, String> {
  


	@Override
	public String convertToDatabaseColumn(AccountState attribute) {
		 if (attribute == null) {
	            return null;
	        }
	        return attribute.getCode();
	}

	@Override
	public AccountState convertToEntityAttribute(String code) {
		// TODO Auto-generated method stub
		 if (code == null) {
	            return null;
	        }
	 
	        return Stream.of(AccountState.values())
	          .filter(c -> c.getCode().equals(code))
	          .findFirst()
	          .orElseThrow(IllegalArgumentException::new);
	}
}