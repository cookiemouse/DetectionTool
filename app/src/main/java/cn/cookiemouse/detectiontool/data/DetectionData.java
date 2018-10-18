package cn.cookiemouse.detectiontool.data;

public class DetectionData {
    private long rowid = -1;
    private String name;
    private String address;
    private int status = Data.STATUS_INIT;

    public DetectionData(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public DetectionData(long rowid, String name, String address) {
        this.rowid = rowid;
        this.name = name;
        this.address = address;
    }

    public long getRowid() {
        return rowid;
    }

    public void setRowid(long rowid) {
        this.rowid = rowid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
