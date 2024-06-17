package iess.pt;

import iess.pt.entity.Product;
import iess.pt.entity.Role;
import iess.pt.entity.User;
import iess.pt.service.AuthService;
import iess.pt.service.ProductService;
import iess.pt.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SetupStartUpDate {

    @Autowired
    private final UserService userService;

    @Autowired
    private final AuthService auth;

    @Autowired
    private final ProductService productService;


    public void setup()
    {

        if(userService.getByName("Alex Jones") == null) {
            System.out.println("\n ================== inserting users ================== \n");
            System.out.println("\n ================== inserting Manager ================== \n");

            auth.register(
                    User.builder()
                            .name("Alex Jones")
                            .email("alexj@gmail.com")
                            .password("alexiano")
                            .role(Role.MANAGER)
                            .HiredIn(LocalDate.now())
                            .address("Somewhere")
                            .phone("111222333")
                            .build()
            );
        }

        if(userService.getByName("Sarah Smith") == null) {
            System.out.println("\n ================== inserting Employee ================== \n");

            auth.register(
                    User.builder()
                            .name("Sarah Smith")
                            .email("lilsarah@gmail.com")
                            .password("secretpassword")
                            .role(Role.EMPLOYEE)
                            .HiredIn(LocalDate.now())
                            .address("Nowhere")
                            .phone("444555666")
                            .build()
            );
        }
         if(userService.getByName("José Gil") == null) {
            System.out.println("\n ================== inserting Employee ================== \n");

            auth.register(
                    User.builder()
                            .name("José Gil")
                            .email("gil@gmail.com")
                            .password("velha")
                            .role(Role.EMPLOYEE)
                            .HiredIn(LocalDate.now())
                            .address("Rua do Nunca")
                            .phone("182918292")
                            .build()
            );
        }
        if(userService.getByName("Flavio Mario") == null) {
            System.out.println("\n ================== inserting Employee ================== \n");

            auth.register(
                    User.builder()
                            .name(" Flavio Mario")
                            .email("flaviomario@gmail.com")
                            .password("mario")
                            .role(Role.EMPLOYEE)
                            .HiredIn(LocalDate.now())
                            .address("Rua da ponte")
                            .phone("123123123")
                            .build()
            );
        }

        List<Product> products = new ArrayList<>();

        products.add(
                Product.builder()
                        .name("Fritadeira sem Óleo UFESA Blackbird (6.5 L)")
                        .price(99)
                        .curr_quantity(30)
                        .category("Eletrodomestic")
                        .provider("Worten")
                        .build()
        );

        products.add(
                Product.builder()
                        .name("Discord nitro")
                        .price(6)
                        .curr_quantity(100)
                        .category("Digital Service")
                        .provider("Discord ltda")
                        .build()
        );

        products.add(
                Product.builder()
                        .name("Berried delight")
                        .price(9.99)
                        .curr_quantity(30)
                        .category("Food")
                        .provider("Virgel")
                        .build()
        );

        products.add(
                Product.builder()
                        .name("Creatina Mono-Hidratada 300")
                        .price(20.99)
                        .curr_quantity(30)
                        .category("Food")
                        .provider("Prozis")
                        .build()
        );

        products.add(
                Product.builder()
                        .name("O mínimo que você precisa saber para não ser um idiota")
                        .price(20)
                        .curr_quantity(17)
                        .category("Book")
                        .provider("Amazon")
                        .build()
        );

        products.add(
                Product.builder()
                        .name("Strawberry elephant")
                        .price(2)
                        .curr_quantity(3)
                        .category("Food")
                        .provider("OnandOn")
                        .build()
        );

        products.add(
                Product.builder()
                        .name("yogurt")
                        .price(2.99)
                        .curr_quantity(50)
                        .category("Food")
                        .provider("YogurtDelight")
                        .build()
        );

        for(Product p : products)
        {
            if(productService.checkProduct(p.getName()) == null)
            {
                System.out.println("\n ================== inserting "+p+ "================== \n");
                productService.save(p);
            }

        }
    }
}
