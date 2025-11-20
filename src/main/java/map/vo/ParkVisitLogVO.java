package map.vo;

import java.io.Serializable;
import java.util.Date;

public class ParkVisitLogVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer logId;      // log_id (PK)
    private Integer parkId;     // park_id (FK)
    private Integer memId;      // mem_id (비회원이면 null)
    private String visitIp;     // visit_ip (IP주소)
    private Date visitDt;       // visit_dt (방문일시)

    // Getter & Setter
    public Integer getLogId() { return logId; }
    public void setLogId(Integer logId) { this.logId = logId; }
    public Integer getParkId() { return parkId; }
    public void setParkId(Integer parkId) { this.parkId = parkId; }
    public Integer getMemId() { return memId; }
    public void setMemId(Integer memId) { this.memId = memId; }
    public String getVisitIp() { return visitIp; }
    public void setVisitIp(String visitIp) { this.visitIp = visitIp; }
    public Date getVisitDt() { return visitDt; }
    public void setVisitDt(Date visitDt) { this.visitDt = visitDt; }
}