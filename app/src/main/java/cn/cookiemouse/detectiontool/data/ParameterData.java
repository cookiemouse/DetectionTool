package cn.cookiemouse.detectiontool.data;

public class ParameterData {
    private long rowidDetection;
    private long rowidParameter;
    private String key, value;

    public ParameterData() {
        rowidParameter = Data.DATA_INIT_ROWID;
    }

    public ParameterData(long rowidDetection, String key, String value) {
        this(rowidDetection, Data.DATA_INIT_ROWID, key, value);
    }

    public ParameterData(long rowidDetection, long rowidParameter, String key, String value) {
        this.rowidDetection = rowidDetection;
        this.rowidParameter = rowidParameter;
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getRowidDetection() {
        return rowidDetection;
    }

    public void setRowidDetection(long rowidDetection) {
        this.rowidDetection = rowidDetection;
    }

    public long getRowidParameter() {
        return rowidParameter;
    }

    public void setRowidParameter(long rowidParameter) {
        this.rowidParameter = rowidParameter;
    }
}
