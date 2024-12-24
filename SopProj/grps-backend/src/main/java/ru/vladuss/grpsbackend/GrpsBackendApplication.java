package ru.vladuss.grpsbackend;

import io.grpc.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GrpsBackendApplication implements CommandLineRunner {

    @Autowired
    private Server grpcServer;

    public static void main(String[] args) {
        SpringApplication.run(GrpsBackendApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Server startanyl<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<,");
        grpcServer.start();
        grpcServer.awaitTermination();
    }

}
