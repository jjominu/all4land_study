package map.vo;

import java.io.Serializable;
import java.util.Date;

public class ParkBookmarkVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer bmId;       // PK
    private Integer memId;      // FK (누가)
    private Integer parkId;     // FK (어디를)
    private Date createdAt;

    // Getter & Setter
    public Integer getBmId() { return bmId; }
    public void setBmId(Integer bmId) { this.bmId = bmId; }
    public Integer getMemId() { return memId; }
    public void setMemId(Integer memId) { this.memId = memId; }
    public Integer getParkId() { return parkId; }
    public void setParkId(Integer parkId) { this.parkId = parkId; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}