package atlas.core;

import java.io.Serializable;

/**
 * ${DESCRIPTION}
 *
 * @author Ricky Fung
 */
public class ServerResponse<T> implements Serializable {

    private static final long serialVersionUID = -241195060101769515L;
    private int code;
    private String msg;
    private T data;

    /**0~999为系统预留code, 业务自定义code建议1000及以上**/
    public static final int SUCCESS = 0;    //业务处理成功
    public static final int SYS_ERROR = 10;   //系统出错
    public static final int SYS_MAINTENANCE = 11; //系统停服

    public ServerResponse(){
        this(SUCCESS, "", null);
    }
    public ServerResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ServerResponse<T> buildSuccessResponse(String msg){
        return new ServerResponse(SUCCESS, msg, null);
    }
    public static <T> ServerResponse<T> buildSuccessResponse(String msg, T data){
        return new ServerResponse(SUCCESS, msg, data);
    }

    public static <T> ServerResponse<T> buildFailureResponse(int code, String msg){
        return new ServerResponse(code, msg, null);
    }
    public static <T> ServerResponse<T> buildFailureResponse(int code, String msg, T data){
        return new ServerResponse(code, msg, data);
    }

    public static <T> ServerResponse<T> buildSysErrorResponse(String msg){
        return new ServerResponse(SYS_ERROR, msg, null);
    }
    public static <T> ServerResponse<T> buildSysErrorResponse(String msg, T data){
        return new ServerResponse(SYS_ERROR, msg, data);
    }

    public boolean isSuccessful() {
        return this.code == SUCCESS;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(256);
        sb.append("ServerResponse{code=").append(code)
                .append(", msg='").append(msg).append('\'')
                .append(", data=").append(data).append('}');
        return sb.toString();
    }
}
