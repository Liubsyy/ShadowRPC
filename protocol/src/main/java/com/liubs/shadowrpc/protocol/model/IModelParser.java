package com.liubs.shadowrpc.protocol.model;


/**
 * 从协议中抽象出来的model, 用model去处理应用层面的逻辑
 * request,response和model的互相转换
 * @author Liubsyy
 * @date 2023/12/25
 */
public interface IModelParser<R,S> {
    /**
     * 协议转换成RequestModel
     */
    RequestModel fromRequest(R request) throws Exception;

    /**
     * RequestModel转换成协议
     */
    R toRequest(RequestModel requestModel) throws Exception;

    /**
     * 协议转换成ResponseModel
     */
    ResponseModel fromResponse(S response) throws Exception;

    /**
     * ResponseModel转换成协议
     */
    S toResponse(ResponseModel responseModel) throws Exception;
}
