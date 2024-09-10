package com.wanted.gold.client;

import com.wanted.gold.client.dto.UserResponseDto;
import com.wanted.gold.exception.*;
import com.wanted.gold.grpc.AuthRequest;
import com.wanted.gold.grpc.AuthResponse;
import com.wanted.gold.grpc.AuthServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AuthGrpcClient {
    private final AuthServiceGrpc.AuthServiceBlockingStub blockingStub;

    public AuthGrpcClient() {
        ManagedChannel channel = NettyChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();
        blockingStub = AuthServiceGrpc.newBlockingStub(channel);
    }

    public UserResponseDto getUserIdAndRole(String accessToken) {
        AuthRequest request = AuthRequest.newBuilder()
                .setAccessToken(accessToken)
                .build();
        try {
            AuthResponse response = blockingStub.authCall(request);
            return new UserResponseDto(response.getUserId(), response.getRole());
        } catch (StatusRuntimeException e) {
            if(e.getStatus().getCode() == Status.Code.UNAUTHENTICATED) // 401
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getStatus().getDescription());
            else if(e.getStatus().getCode() == Status.Code.NOT_FOUND) // 404
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getStatus().getDescription());
            else if(e.getStatus().getCode() == Status.Code.INVALID_ARGUMENT) // 400
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getStatus().getDescription());
            else // 500
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getStatus().getDescription());
        }
    }
}
