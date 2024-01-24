package rpctest.upload;

import com.liubs.shadowrpc.base.config.ClientConfig;
import com.liubs.shadowrpc.base.module.ModulePool;
import com.liubs.shadowrpc.client.ClientModule;
import com.liubs.shadowrpc.client.connection.ShadowClient;
import com.liubs.shadowrpc.client.proxy.RemoteServerProxy;
import com.liubs.shadowrpc.protocol.serializer.SerializerStrategy;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

/**
 * @author Liubsyy
 * @date 2024/1/14
 **/
public class UploadClient {
    private static final Logger logger = LoggerFactory.getLogger(UploadClient.class);

    @Before
    public void init(){
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setSerializer(SerializerStrategy.KRYO.name());
        ModulePool.getModule(ClientModule.class).init(clientConfig);
    }

    @Test
    public void uploadFileTest() throws IOException {

        ShadowClient shadowClient = new ShadowClient("127.0.0.1",2023);
        shadowClient.init();

        IUploadService uploadService = shadowClient.createRemoteProxy(IUploadService.class,"shadowrpc://DefaultGroup/uploadService");

        byte[] bytes = Files.readAllBytes(Paths.get("/Users/liubs/IdeaProjects/ShadowRPC/protocol/src/test/resources/proto/MyMessage.proto"));
        boolean upload = uploadService.upload("upload_MyMessage.proto", bytes);
        Assert.assertTrue(upload);

        shadowClient.close();
    }

    @Test
    public void uploadManyFilesTest() throws IOException {
        ShadowClient shadowClient = new ShadowClient("127.0.0.1",2023);
        shadowClient.init();
        IUploadService uploadService = shadowClient.createRemoteProxy(IUploadService.class,"uploadService");

        File uploadDir = new File("target/tmp/");
        if(!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        //生成100个文件
        int n = 100;
        for(int i = 0;i<n;i++) {
            Files.write(Paths.get("target/tmp/"+(i+1)+".txt"),
                    UUID.randomUUID().toString().getBytes(),
                    StandardOpenOption.TRUNCATE_EXISTING,StandardOpenOption.CREATE,StandardOpenOption.WRITE);
        }

        for(int i=0;i<n;i++) {
            byte[] bytes = Files.readAllBytes(Paths.get("target/tmp/" + (i + 1) + ".txt"));
            boolean upload = uploadService.upload((i + 1) + ".txt", bytes);
            if(upload) {
                logger.info("上传{}成功",(i + 1) + ".txt");
            }else {
                logger.error("上传{}失败",(i + 1) + ".txt");
            }
        }


    }
}
