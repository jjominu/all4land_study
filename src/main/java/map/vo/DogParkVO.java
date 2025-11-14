package map.vo;

public class DogParkVO {

    private Integer id;
    private String sdNm;
    private String sggNm;
    private String parkNm;
    private String operTm;
    private String hldy;
    private String fcs;
    private String fcar;
    private String operInst;
    private String telno;
    private String addr;
    private String useAmt;

    /**
     * PostGIS geometry(MultiPolygon, 3857)
     * WKT 포맷으로 주고 받기 (예: MULTIPOLYGON(((...))))
     */
    private String geomWkt;

    // ===== Getter / Setter =====

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSdNm() {
        return sdNm;
    }

    public void setSdNm(String sdNm) {
        this.sdNm = sdNm;
    }

    public String getSggNm() {
        return sggNm;
    }

    public void setSggNm(String sggNm) {
        this.sggNm = sggNm;
    }

    public String getParkNm() {
        return parkNm;
    }

    public void setParkNm(String parkNm) {
        this.parkNm = parkNm;
    }

    public String getOperTm() {
        return operTm;
    }

    public void setOperTm(String operTm) {
        this.operTm = operTm;
    }

    public String getHldy() {
        return hldy;
    }

    public void setHldy(String hldy) {
        this.hldy = hldy;
    }

    public String getFcs() {
        return fcs;
    }

    public void setFcs(String fcs) {
        this.fcs = fcs;
    }

    public String getFcar() {
        return fcar;
    }

    public void setFcar(String fcar) {
        this.fcar = fcar;
    }

    public String getOperInst() {
        return operInst;
    }

    public void setOperInst(String operInst) {
        this.operInst = operInst;
    }

    public String getTelno() {
        return telno;
    }

    public void setTelno(String telno) {
        this.telno = telno;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getUseAmt() {
        return useAmt;
    }

    public void setUseAmt(String useAmt) {
        this.useAmt = useAmt;
    }

    public String getGeomWkt() {
        return geomWkt;
    }

    public void setGeomWkt(String geomWkt) {
        this.geomWkt = geomWkt;
    }

    @Override
    public String toString() {
        return "DogParkVO{" +
                "id=" + id +
                ", sdNm='" + sdNm + '\'' +
                ", sggNm='" + sggNm + '\'' +
                ", parkNm='" + parkNm + '\'' +
                ", operTm='" + operTm + '\'' +
                ", hldy='" + hldy + '\'' +
                ", fcs='" + fcs + '\'' +
                ", fcar='" + fcar + '\'' +
                ", operInst='" + operInst + '\'' +
                ", telno='" + telno + '\'' +
                ", addr='" + addr + '\'' +
                ", useAmt='" + useAmt + '\'' +
                ", geomWkt='" + geomWkt + '\'' +
                '}';
    }
}
