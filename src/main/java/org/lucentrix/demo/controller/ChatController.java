package org.lucentrix.demo.controller;

import org.lucentrix.demo.config.ProductDetails;
import org.lucentrix.demo.persistence.Product;
import org.lucentrix.demo.tool.AvailableProductsTool;
import org.lucentrix.demo.tool.FindProductTool;
import org.lucentrix.demo.service.ProductService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
public class ChatController {

    private final ChatClient chatClient;
    private final ProductService productService;

    private final Pattern notFoundRespPattern = Pattern.compile("The product with the name \".+?\" was not found");

    private final AvailableProductsTool availableProductTool;
    private final FindProductTool findProductTool;

    @Autowired
    public ChatController(ChatClient.Builder builder, ProductService productService) {
        this.productService = productService;
        this.findProductTool = new FindProductTool(productService);
        this.availableProductTool = new AvailableProductsTool(productService);
        this.chatClient = builder
                .defaultSystem(
                        "You are an AI assistant answering questions about products.\n" +
                        "- Always first try to call `getProductDetailsByName` when user asks for a specific product.\n" +
                        "- Provide only product details to user when asked about product, no additional information such as location, price formula, etc." +
                        "- If the product details not found or product with name '"+Product.NOT_FOUND_NAME+"' returned, immediately call `getAvailableProductDetails` " +
                        " to provide the user with a list of available products.\n" +
                        "- Then explain to the user that the requested product was not found, but show them 'getAvailableProductDetails' alternatives."
                )
                .build();
    }

    @PostMapping("/ai/ask")
    public ResponseEntity<String> ask(@RequestBody Map<String, String> body) {
        try {
            String message = body.get("message");

            String response = chatClient.prompt()
                    .user(message)
                    .tools(findProductTool, availableProductTool)
                    .call()
                    .content() + "\n";

            // Hack to handle product not found to provide list of available products
            // Don't use it in prod, it is demo only
            if (response.contains(Product.NOT_FOUND_NAME) ||  notFoundRespPattern.matcher(response).find()) {
                // Call available products tool explicitly
                List<ProductDetails> available = availableProductTool.getAvailableProductDetails();
                if(available.isEmpty()) {
                    response = "No products available.";
                } else {
                    response = "Product not found. Here are available (top 10) products: " +
                            available.stream().map(ProductDetails::name).collect(Collectors.joining(", "));
                }
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("An error occurred: " + e.getMessage());
        }
    }


}
