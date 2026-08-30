package com.example.manage.utils;

import com.example.manage.bean.ResultBean;

public class ResultUtil {

    private static int ERROR_CODE = -1;
    private static int SUCCESS_CODE = 200;

    public static <T> ResultBean getResultBean(T data, String msg, int errorCode) {
        ResultBean resultBean = new ResultBean();
        resultBean.setErrCode(errorCode);
        resultBean.setErrMsg(msg);
        resultBean.setData(data);
        return resultBean;
    }

    public static <T> ResultBean getSuccessBean(T data, String msg) {
        return getResultBean(data, msg, SUCCESS_CODE);
    }

    public static ResultBean getResultBean(int count, String errMsg) {
        if (count > 0) {
            return getSuccessBean("");
        } else {
            return getFailBean(errMsg);
        }
    }

    public static <T> ResultBean getSuccessBean(T data) {
        return getSuccessBean(data, "操作成功");
    }

    public static <T> ResultBean getFailBean(String msg) {
        return getResultBean(null, msg, ERROR_CODE);
    }

    public static <T> ResultBean getFailBean() {
        return getFailBean("操作失败");
    }

}
