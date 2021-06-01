package Classes.Models;


public enum Status {
	Contact("C"), Friends("F"), Request("R");
	
	private String code;
	
	private Status(String code) {
        this.code = code;
    }
 
    public String getCode() {
        return code;
    }
}