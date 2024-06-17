package iess.pt.event.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import iess.pt.entity.Product;
import iess.pt.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TopicListener {
    @Autowired
    private ProductService service;
    //@KafkaListener(topics = "point_of_sale_dg", groupId = "import org.springframework.kafka.annotation.KafkaListener;\n")
    public void listen(String message) {
        System.out.println(message);



        
        /* 
        ObjectMapper objectMapper = new ObjectMapper();
        try {

            Product product = objectMapper.readValue(message, Product.class);

            System.out.println("Received Message - Product: " + product.getName() + ", Price: " + product.getPrice() + ", Provider: " + product.getProvider()
            + "Category : "+product.getCategory()+ "Unities : "+product.getCurr_quantity());


            if(service.getProductByNameAndProvider(product.getName(), product.getProvider()) == null){
                System.out.println("Product non-existent");
                service.save(product);
            }
            else
            {
                System.out.println("Product existent");
                service.updateByName(product.getName(), product);
                //service.updateByName(product.getName(), product);
            }



        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
