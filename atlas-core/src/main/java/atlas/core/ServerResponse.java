package atlas.core;

import java.io.Serializable;

/**
 * ${DESCRIPTION}
 *
 * @author Ricky Fung
 */
public class ServerResponse<T> implements Serializable {

    private static final long serialVersionUID = -241195060101769515L;
    private final int code;
    private final String msg;
    private final T data;

    public ServerResponse(){
        this(0, "", null);
    }
    public ServerResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder<T> {
        private int code;
        private String msg;
        private T data;

        public Builder code(int code) {
            this.code = code;
            return this;
        }

        public Builder msg(String msg) {
            this.msg = msg;
            return this;
        }

        public Builder data(T data) {
            this.data = data;
            return this;
        }

        public ServerResponse<T> build() {
            return new ServerResponse(this.code, this.msg, this.data);
        }

    }
}
