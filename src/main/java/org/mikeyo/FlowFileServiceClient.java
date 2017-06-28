package org.mikeyo;

import com.google.protobuf.ByteString;

import org.apache.nifi.processors.grpc.FlowFileReply;
import org.apache.nifi.processors.grpc.FlowFileRequest;
import org.apache.nifi.processors.grpc.FlowFileServiceGrpc;

import java.util.Random;

import io.grpc.CompressorRegistry;
import io.grpc.DecompressorRegistry;
import io.grpc.netty.NettyChannelBuilder;

public class FlowFileServiceClient {
    private static final String HOST = "localhost";
    private static final int PORT = 50051;
    private static Random random = new Random(System.currentTimeMillis());

    public static void main(String[] args) {
        final NettyChannelBuilder channelBuilder = NettyChannelBuilder.forAddress(HOST, PORT)
                .directExecutor()
                .compressorRegistry(CompressorRegistry.getDefaultInstance())
                .decompressorRegistry(DecompressorRegistry.getDefaultInstance())
                .usePlaintext(true)
                .userAgent("testAgent");

        final FlowFileServiceGrpc.FlowFileServiceBlockingStub stub = FlowFileServiceGrpc.newBlockingStub(channelBuilder.build());

        final FlowFileRequest flowFileRequest = FlowFileRequest.newBuilder()
                .setId(random.nextInt())
                .setContent(ByteString.copyFrom("HELLO WORLD".getBytes()))
                .build();
        final FlowFileReply reply = stub.send(flowFileRequest);
        System.out.println(reply);
    }
}
