package com.org.bddsserver.exception;

/**
 * @author PY.Lu
 * @date 2025/10/15
 * @Description：响应码枚举类
 */

public enum ResultCode {
    SUCCESS(0,"success"),
    ERROR(1,"error"),

    BUSINESS_ERROR(2,"business error"),
    PARAM_ERROR(3,"param error"),
    SERVER_ERROR(4,"server error");






    private final int code;
    private final String message;

    ResultCode(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    //根据 code 获取枚举对象
    public static ResultCode getEnumByCode(int code){
        for(ResultCode item : ResultCode.values()){
            if(item.getCode() == code){
                return item;
            }
        }
        return null;
    }
}
