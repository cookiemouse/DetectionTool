package cn.cookiemouse.detectiontool.data;

public class ParameterData {
    private String key, value;
    private boolean typeAdd;

    public ParameterData() {
    }

    public ParameterData(String key, String value) {
        this.key = key;
        this.value = value;
        this.typeAdd = false;
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

    public boolean isTypeAdd() {
        return typeAdd;
    }

    public void setTypeAdd(boolean typeAdd) {
        this.typeAdd = typeAdd;
    }
}
