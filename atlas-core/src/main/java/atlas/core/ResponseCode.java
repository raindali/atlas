package atlas.core;

/**
 * ${DESCRIPTION}
 *
 * @author Ricky Fung
 */
public enum ResponseCode {
    OK(0, "OK"),
    FAILURE(400, "FAILURE"),
    NOT_AUTH(403, "没有权限"),
    SYS_ERR(500, "系统异常");

    private int code;
    private String desc;
    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
