package com.cleartraders.common.exception;

import com.cleartraders.common.define.CommonDefine;

public class NormalException extends Exception
{
    private static final long serialVersionUID = -3575899416535657154L;
    
    private int error_code=CommonDefine.ERROR_RESULT;
    private String error_msg="";

    public NormalException(String exceptionDescription,int errorCode)
    {
        super(exceptionDescription);
        this.error_msg = exceptionDescription;
        this.error_code = errorCode;
    }

    public int getError_code()
    {
        return error_code;
    }

    public void setError_code(int error_code)
    {
        this.error_code = error_code;
    }

    public String getError_msg()
    {
        return error_msg;
    }

    public void setError_msg(String error_msg)
    {
        this.error_msg = error_msg;
    }
    
}
