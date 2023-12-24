package com.sky.exception;

public class DeletionNotAllowedException extends BaseException {

    public DeletionNotAllowedException(String msg) {
        super(msg); // 底层还是抛出：Runtime运行时异常，这里给这个异常定义了信息；
    }

}
