package iess.pt.service;

import com.fasterxml.jackson.core.type.TypeReference;

import iess.pt.dataAccess.productRepository;
import iess.pt.entity.Product;
import iess.pt.entity.SoldProduct;
import iess.pt.entity.User;
import iess.pt.entity.cartProd;
import iess.pt.entity.Template.FormattedResponse;
import iess.pt.event.kafka.KafkaMessageProducer;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.print.event.PrintEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import iess.pt.service.ProductService;

@Service
public class SendMessages {

    @Autowired
    private KafkaMessageProducer kafkaMessageProducer;
    @Autowired
    private ProductService productService;

    @Autowired
    private UserService UserService;
    @Autowired
    private productRepository repository;

    @Async
    public void sendProducts(String topic) {
        while (true) {
            // Generate or retrieve your message here
            List<Product> allProducts = productService.getAll();
            if (allProducts.isEmpty()) {
                System.out.println("No products available.");
            }
            Random random = new Random();
            int randomIndex = random.nextInt(allProducts.size());
            Product randomProduct = allProducts.get(1);

            String product = randomProduct.getName();
            double price = randomProduct.getPrice();
            String provider = randomProduct.getProvider();
            String category = randomProduct.getCategory();

            System.out.println("\n" + price + "\n");
            String message = String.format(
                    "{\"product\": \"%s\", \"price\": %.2f, \"provider\": \"%s\", \"category\": \"%s\"}",
                    product, price, provider, category);
            // Send the message using the KafkaMessageProducer
            kafkaMessageProducer.sendMessage(topic, message);

            try {
                // Add a delay or sleep if needed
                Thread.sleep(1000); // Sleep for 1 second, adjust as needed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @KafkaListener(topics = "point_of_sale_dg", groupId = "index2")
    public void consume(String message) {
        System.out.println("\n\n= |||   === Received message: " + message);
        List<User> allUsers = UserService.getAll();
        if (allUsers.isEmpty()) {
            System.out.println("No Users available");
        } else {
            // Deserialize JSON to list of ShoppingCartItem objects
            List<cartProd> shoppingCartItems = deserializeShoppingCart(message);
            Random random = new Random();
            int randomIndex = random.nextInt(allUsers.size());
            User randomUser = allUsers.get(randomIndex);
            long Userid = randomUser.getId();
            String IdString = String.valueOf(Userid);
            System.out.println("\nUser id: " + Userid + "\n");

            // Process the deserialized objects as needed
            Map<Long, Integer> cart = new HashMap<>();
            for (cartProd item : shoppingCartItems) {
                System.out.println("\n ------ | Y | ------\n");
                Long id = item.getProduct();
                int units = item.getUnits();
                cart.put(id, units);
                // cartProd(product=7, units=28, Price=980.0)
                Product produto = repository.searchByID(id);
                int product_units = produto.getCurr_quantity();


                if (product_units<10 ){
                    System.out.println("Updating Products inside the listener");
                       repository.updateProductByName(id, product_units + 20);
                }
            }
            productService.performPayment(Userid, cart);

        }
    }

   

    private List<cartProd> deserializeShoppingCart(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(message, new TypeReference<List<cartProd>>() {
            });
        } catch (IOException e) {
            // Handle deserialization exception
            e.printStackTrace();
            return null;
        }
    }

    @Async
    public void sendEmployees(String topic) throws Exception {
        try {
            while (true) {
                System.out.println("\n\n=========================== Sales sim | api ============================\n\n");

                List<Product> allProducts = productService.getAll();
                if (allProducts.isEmpty()) {
                    System.out.println("No products available.");
                } else {
                    ObjectMapper objectMapper = new ObjectMapper();
                    String jsonString = objectMapper.writeValueAsString(allProducts);
                    System.out.println("\n |x| === Sending prodcuts to main ");
                    // System.out.println(jsonString);
                    kafkaMessageProducer.sendMessage(topic, jsonString);
                }
                try {
                    // Add a delay or sleep if needed
                    Thread.sleep(5000); // Sleep for 1 second, adjust as needed
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            // Handle the exception (print a message, log it, or take appropriate action)
            e.printStackTrace();
        }

    }
}
