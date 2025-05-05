package com.example.springaiapp.config;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIScannerClientConfiguration {

//    @Value("${spring.ai.mcp.client.sse.connections.default.url}")
//    private String mcpServerUrl;
//
//    private static final Logger logger = LoggerFactory.getLogger(AIScannerClientConfiguration.class);
//
//    @Bean
//    public McpSyncClient mcpSyncClient() {
//
//        logger.info("Creating MCP sync client with URL: {}", mcpServerUrl);
//
//        HttpClientSseClientTransport transport = HttpClientSseClientTransport
//                .builder(mcpServerUrl)
//                .build();
//
//        return McpClient.sync(transport).build();
//    }

    @Value("${spring.ai.mcp.client.sse.connections.default.url}")
    private String mcpServerUrl;

    private static final Logger logger = LoggerFactory.getLogger(AIScannerClientConfiguration.class);

    @Bean
    public McpSyncClient mcpSyncClient() {
        logger.info("Creating MCP sync client with URL: {}", mcpServerUrl);
        HttpClientSseClientTransport transport = HttpClientSseClientTransport
                .builder(mcpServerUrl)
                .build();

        return McpClient.sync(transport).build();
    }
}


//package com.example.springaiapp.config;
//
//import org.springframework.context.annotation.Configuration;
//
///**
// * Configuration for AI Scanner Client
// *
// * Note: This class is no longer needed for bean creation as Spring Boot's
// * auto-configuration will create the McpSyncClient bean automatically based on
// * the properties in application.properties.
// */
//@Configuration
//public class AIScannerClientConfiguration {
//    // No beans needed here as Spring Boot auto-configuration will handle it
//    // based on the properties in application.properties
//}



//package com.example.springaiapp.config;
//
//import io.modelcontextprotocol.client.McpSyncClient;
//import org.springframework.beans.factory.ObjectProvider;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class AIScannerClientConfiguration {
//
//    @Autowired
//    public void configureMcpClient(ObjectProvider<McpSyncClient> clientProvider) {
//        McpSyncClient client = clientProvider.getIfAvailable();
//        if (client != null) {
//            System.out.println("MCP client is ready: " + client);
//        } else {
//            System.out.println("MCP client not yet initialized");
//        }
//    }
//}



//package com.example.springaiapp.config;
//
//import io.modelcontextprotocol.client.McpSyncClient;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class AIScannerClientConfiguration {
//
//    // This class exists for organization but does not override the default MCP config
//
//    private final McpSyncClient mcpSyncClient;
//
//    public AIScannerClientConfiguration(McpSyncClient mcpSyncClient) {
//        this.mcpSyncClient = mcpSyncClient;
//
//        // You can do additional setup or logging here if needed
//        System.out.println("Auto-configured MCP client injected: " + mcpSyncClient);
//    }
//
//    public McpSyncClient getMcpSyncClient() {
//        return this.mcpSyncClient;
//    }
//}


//package com.example.springaiapp.config;
//
//import io.modelcontextprotocol.client.McpClient;
//import io.modelcontextprotocol.client.McpSyncClient;
//import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class AIScannerClientConfiguration {
//
//    @Bean
//    public McpSyncClient mcpSyncClient() {
//        HttpClientSseClientTransport transport = HttpClientSseClientTransport
//                .builder("http://localhost:8081").build();
//
//        // Create a synchronous MCP client using the static sync() method
//        return McpClient.sync(transport).build();
//    }
//}