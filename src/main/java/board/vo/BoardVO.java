package board.vo;

import java.sql.Timestamp;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;



public class BoardVO {

	private int boardId;

    @NotBlank(message = "제목을 입력하세요.")
    @Size(min = 2, max = 100, message = "제목은 2~100자 이내여야 합니다.")
	private String title;
	private int view ;
	 @NotBlank(message = "내용을 입력하세요.")
	 @Size(min = 5, max = 2000, message = "내용은 5~2000자 이내여야 합니다.")
	private String content ; 
	private Timestamp createTimestamp;
	private String createUser ;
	
	
	
	
	public int getBoardId() {
		return boardId;
	}
	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getView() {
		return view;
	}
	public void setView(int view) {
		this.view = view;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Timestamp getCreateTimestamp() {
		return createTimestamp;
	}
	public void setCreateTimestamp(Timestamp createTimestamp) {
		this.createTimestamp = createTimestamp;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	
}
