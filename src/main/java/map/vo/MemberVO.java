package map.vo;

import java.io.Serializable;
import java.util.Date;

public class MemberVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer memId;      // PK
    private String memUid;      // 아이디 (이메일)
    private String memPw;       // 비밀번호
    private String memName;     // 닉네임
    private String memImg;      // 프로필 이미지 경로
    private Date createdAt;     // 가입일

    // Getter & Setter
    public Integer getMemId() { return memId; }
    public void setMemId(Integer memId) { this.memId = memId; }
    public String getMemUid() { return memUid; }
    public void setMemUid(String memUid) { this.memUid = memUid; }
    public String getMemPw() { return memPw; }
    public void setMemPw(String memPw) { this.memPw = memPw; }
    public String getMemName() { return memName; }
    public void setMemName(String memName) { this.memName = memName; }
    public String getMemImg() { return memImg; }
    public void setMemImg(String memImg) { this.memImg = memImg; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}