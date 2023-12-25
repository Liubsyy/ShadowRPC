package com.liubs.shadowrpc.protocol.serializer.protobuf;

import com.google.protobuf.ByteString;
import com.google.protobuf.MessageLite;
import com.liubs.shadowrpc.protocol.entity.ShadowRPCRequestProto;
import com.liubs.shadowrpc.protocol.entity.ShadowRPCResponseProto;
import com.liubs.shadowrpc.protocol.model.RequestModel;
import com.liubs.shadowrpc.protocol.model.ResponseModel;
import com.liubs.shadowrpc.protocol.model.IModelParser;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Liubsyy
 * @date 2023/12/25
 */
public class ProtobufModelParser implements IModelParser<ShadowRPCRequestProto.ShadowRPCRequest, ShadowRPCResponseProto.ShadowRPCResponse> {

    @Override
    public RequestModel fromRequest(ShadowRPCRequestProto.ShadowRPCRequest request)  throws Exception {
        RequestModel requestModel = new RequestModel();
        requestModel.setTraceId(request.getTraceId());
        requestModel.setServiceName(request.getServiceName());
        requestModel.setMethodName(request.getMethodName());


        Class<?>[] paramTypes = new Class<?>[request.getParamTypesCount()];
        Object[] params = new Object[request.getParamsCount()];

        for(int i = 0,len=request.getParamsCount() ; i<len ;i++) {
            String serviceName = request.getParamTypes(i);
            ByteString bytes = request.getParams(i);

            MessageLite defaultInstance = ParserForType.getMessage(serviceName);
            Object paramObj;
            if(null == defaultInstance) {
                //没有注册，容错处理，反射扫描
                Class<?> aClass = Class.forName(serviceName);
                Method parseFrom = aClass.getDeclaredMethod("parseFrom", ByteString.class);
                paramObj = parseFrom.invoke(null, bytes);
            }else {
                paramObj = defaultInstance.getParserForType().parseFrom(bytes);
            }

            paramTypes[i]  = paramObj.getClass();
            params[i]  = paramObj;
        }

        requestModel.setParamTypes(paramTypes);
        requestModel.setParams(params);
        return requestModel;
    }

    @Override
    public ShadowRPCRequestProto.ShadowRPCRequest toRequest(RequestModel requestModel) {
        ShadowRPCRequestProto.ShadowRPCRequest request = ShadowRPCRequestProto.ShadowRPCRequest.newBuilder()
                .setTraceId(requestModel.getTraceId())
                .setServiceName(requestModel.getServiceName())
                .setMethodName(requestModel.getMethodName())
                .addAllParamTypes(Arrays.stream(requestModel.getParamTypes()).map(Class::getName).collect(Collectors.toList()))
                .addAllParams(Arrays.stream(requestModel.getParams()).map(c->((MessageLite)c).toByteString()).collect(Collectors.toList())).build();
        return request;
    }

    @Override
    public ResponseModel fromResponse(ShadowRPCResponseProto.ShadowRPCResponse response) throws Exception {


        String resultClass = response.getResultClass();
        Object resultObj = null;
        if(!resultClass.isEmpty()) {
            ByteString resultBytes = response.getResult();
            MessageLite defaultInstance = ParserForType.getMessage(resultClass);
            if(null == defaultInstance) {
                //没有注册，容错处理，反射扫描
                Class<?> aClass = Class.forName(resultClass);
                Method parseFrom = aClass.getDeclaredMethod("parseFrom", ByteString.class);
                resultObj = parseFrom.invoke(null, resultBytes);
            }else {
                resultObj = defaultInstance.getParserForType().parseFrom(resultBytes);
            }
        }

        ResponseModel responseModel = new ResponseModel();
        responseModel.setTraceId(response.getTraceId());
        responseModel.setCode(response.getCode());
        responseModel.setResult(resultObj);
        responseModel.setErrorMsg(response.getErrorMsg());
        return responseModel;
    }

    @Override
    public ShadowRPCResponseProto.ShadowRPCResponse toResponse(ResponseModel responseModel) throws Exception {
        ShadowRPCResponseProto.ShadowRPCResponse.Builder response = ShadowRPCResponseProto.ShadowRPCResponse.newBuilder();
        response.setTraceId(responseModel.getTraceId());
        response.setCode(responseModel.getCode());

        if(null != responseModel.getErrorMsg()) {
            response.setErrorMsg(responseModel.getErrorMsg());
        }

        if(null != responseModel.getResult()) {
            response.setResultClass(responseModel.getResult().getClass().getName());
            response.setResult(((MessageLite)responseModel.getResult()).toByteString());
        }
        return response.build();
    }
}
