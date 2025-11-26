package map.vo;

import java.io.Serializable;
import java.util.Date;

public class MemberVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer memId;      
    private String memUid;      
    private String memPw;       
    private String memName;     
    private String memImg;      
    private Date createdAt;
	public Integer getMemId() {
		return memId;
	}
	public void setMemId(Integer memId) {
		this.memId = memId;
	}
	public String getMemUid() {
		return memUid;
	}
	public void setMemUid(String memUid) {
		this.memUid = memUid;
	}
	public String getMemPw() {
		return memPw;
	}
	public void setMemPw(String memPw) {
		this.memPw = memPw;
	}
	public String getMemName() {
		return memName;
	}
	public void setMemName(String memName) {
		this.memName = memName;
	}
	public String getMemImg() {
		return memImg;
	}
	public void setMemImg(String memImg) {
		this.memImg = memImg;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "MemberVO [memId=" + memId + ", memUid=" + memUid + ", memPw=" + memPw + ", memName=" + memName
				+ ", memImg=" + memImg + ", createdAt=" + createdAt + "]";
	}     

  
}