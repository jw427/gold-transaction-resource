package com.wanted.gold.client;

import com.wanted.gold.grpc.AuthRequest;
import com.wanted.gold.grpc.AuthResponse;
import com.wanted.gold.grpc.AuthServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import org.springframework.stereotype.Component;

@Component
public class AuthGrpcClient {
    private final AuthServiceGrpc.AuthServiceBlockingStub blockingStub;

    public AuthGrpcClient() {
        ManagedChannel channel = NettyChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();
        blockingStub = AuthServiceGrpc.newBlockingStub(channel);
    }

    public String getUserId(String accessToken) {
        AuthRequest request = AuthRequest.newBuilder()
                .setAccessToken(accessToken)
                .build();
        try {
            AuthResponse response = blockingStub.authCall(request);
            return response.getUserId();
        } catch (StatusRuntimeException e) {
            throw new RuntimeException(e.getStatus().getDescription(), e);
        }
    }
}
