package cn.cookiemouse.detectiontool.data;

public class DetectionData {
    private int rowid = -1;
    private String name;
    private String address;
    private boolean checked;    //  是否勾选
    private int status;

    public DetectionData(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public DetectionData(int rowid, String name, String address) {
        this.rowid = rowid;
        this.name = name;
        this.address = address;
    }

    public int getRowid() {
        return rowid;
    }

    public void setRowid(int rowid) {
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

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
