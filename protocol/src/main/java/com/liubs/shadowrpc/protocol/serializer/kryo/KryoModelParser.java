package com.liubs.shadowrpc.protocol.serializer.kryo;

import com.liubs.shadowrpc.protocol.entity.ShadowRPCRequest;
import com.liubs.shadowrpc.protocol.entity.ShadowRPCResponse;
import com.liubs.shadowrpc.protocol.model.RequestModel;
import com.liubs.shadowrpc.protocol.model.ResponseModel;
import com.liubs.shadowrpc.protocol.model.IModelParser;

/**
 * @author Liubsyy
 * @date 2023/12/25
 */
public class KryoModelParser implements IModelParser<ShadowRPCRequest, ShadowRPCResponse> {

    @Override
    public RequestModel fromRequest(ShadowRPCRequest request) {
        RequestModel requestModel = new RequestModel();
        requestModel.setTraceId(request.getTraceId());
        requestModel.setServiceName(request.getServiceName());
        requestModel.setMethodName(request.getMethodName());
        requestModel.setParamTypes(request.getParamTypes());
        requestModel.setParams(request.getParams());
        return requestModel;
    }

    @Override
    public ShadowRPCRequest toRequest(RequestModel requestModel) {
        ShadowRPCRequest request = new ShadowRPCRequest();
        request.setTraceId(requestModel.getTraceId());
        request.setServiceName(requestModel.getServiceName());
        request.setMethodName(requestModel.getMethodName());
        request.setParamTypes(requestModel.getParamTypes());
        request.setParams(requestModel.getParams());
        return request;
    }

    @Override
    public ResponseModel fromResponse(ShadowRPCResponse response) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setTraceId(response.getTraceId());
        responseModel.setCode(response.getCode());
        responseModel.setResult(response.getResult());
        responseModel.setErrorMsg(response.getErrorMsg());
        return responseModel;
    }

    @Override
    public ShadowRPCResponse toResponse(ResponseModel responseModel) {
        ShadowRPCResponse response = new ShadowRPCResponse();
        response.setTraceId(responseModel.getTraceId());
        response.setCode(responseModel.getCode());
        response.setResult(responseModel.getResult());
        response.setErrorMsg(responseModel.getErrorMsg());
        return response;
    }
}
