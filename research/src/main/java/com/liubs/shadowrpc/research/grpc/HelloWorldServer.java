package com.liubs.shadowrpc.research.grpc;


import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;

/**
 * @author Liubsyy
 * @date 2023/12/20
 */

public class HelloWorldServer {
    private Server server;

    private void start() throws IOException {
        int port = 50051;
        server = ServerBuilder.forPort(port)
                .addService(new GreeterImpl())
                .build()
                .start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            HelloWorldServer.this.stop();
            System.err.println("*** server shut down");
        }));
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        final HelloWorldServer server = new HelloWorldServer();
        server.start();
        server.blockUntilShutdown();
    }
}
