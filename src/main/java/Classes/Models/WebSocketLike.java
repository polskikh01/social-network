package Classes.Models;

public class WebSocketLike {
	private Long newsId;
	
	private Long authorId;
	
	private Short value;


	public Long getNewsId() {
		return newsId;
	}

	public void setNewsId(Long newsId) {
		this.newsId = newsId;
	}

	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	public Short getValue() {
		return value;
	}
}
