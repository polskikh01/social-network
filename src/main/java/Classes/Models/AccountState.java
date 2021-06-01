package Classes.Models;

public enum AccountState {
	Blocked("B"), Active("A"), Created("C");
	
	private String code;
	
	private AccountState(String code) {
        this.code = code;
    }
 
    public String getCode() {
        return code;
    }
}
