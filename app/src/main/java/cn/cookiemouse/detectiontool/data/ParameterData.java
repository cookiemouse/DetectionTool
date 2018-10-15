package cn.cookiemouse.detectiontool.data;

public class ParameterData {
    private String key, value;

    public ParameterData() {
    }

    public ParameterData(String key, String value) {
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
}
