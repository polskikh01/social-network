package Classes.Models;

public class WebSocketComment {

	private Long author;
	
	private Long myNew;
	
	private String Text;

	public Long getAuthor() {
		return author;
	}

	public void setAuthor(Long author) {
		this.author = author;
	}

	public Long getMyNew() {
		return myNew;
	}

	public void setMyNew(Long myNew) {
		this.myNew = myNew;
	}

	public String getText() {
		return Text;
	}

	public void setText(String text) {
		Text = text;
	}
	
	
}
