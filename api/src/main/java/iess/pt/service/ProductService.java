package iess.pt.service;

import iess.pt.dataAccess.PaymentRepository;
import iess.pt.dataAccess.SoldProductRepository;
import iess.pt.dataAccess.productRepository;
import iess.pt.entity.Product;
import iess.pt.entity.Sale;
import iess.pt.entity.SoldProduct;
import iess.pt.entity.Template.FormattedResponse;
import iess.pt.entity.User;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
public class ProductService {
    @Autowired
    private productRepository repository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private SoldProductRepository soldProductRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    public List<Product> getAll() {
        return repository.findAll();
    }

    public FormattedResponse getProductByName(String name) {
        final Product p = repository.findByName(name);
        FormattedResponse response;
        if (p == null) {
            response = FormattedResponse.builder()
                    .request("Get product named as  " + name)
                    .response("Product named as" + name + " was found or its out of stock")
                    .status(HttpStatus.NOT_FOUND)
                    .object(null)
                    .build();
        } else {
            response = FormattedResponse.builder()
                    .request("Get product named as  " + name)
                    .response("Product fetched from database")
                    .status(HttpStatus.OK)
                    .object(p)
                    .build();
        }
        return response;
    }

    public Product checkProduct(String name) {
        return repository.findByName(name);

    }

    public Product getProductByNameAndProvider(String name, String provider) {
        return repository.searchByNameAndProvider(name, provider);
    }

    public FormattedResponse performPayment(Long uid, Map<Long, Integer> cart) {
        int total = 0;
        double price = 0.0;
        Long userid = uid;
        double p_price = 0.0;
        List<SoldProduct> soldProducts = new ArrayList<>();
        for (Map.Entry<Long, Integer> p : cart.entrySet()) {
            Long p_id = p.getKey();
            Integer p_units = p.getValue();
            Product product = repository.searchByID(p_id);
            int final_unity = product.getCurr_quantity();

            System.out.println("|||| \n Processing sale of product id: " + p_id);
            // se n encontrar produto ou não conseguir levar o produto(unidades possiveos <
            // doq as que quer) ent n leva.
            if (repository.findById(p_id) == null) {
                System.out.println(
                        "\n\n =========================== ERROR : Product of id " + p_id + " not found, skipping ");
                continue;
            }
            else if (final_unity== 0) {
                System.out.println("\n\n  No more units to sell. Units product has " + final_unity + "units user wants:"
                        + p_units);
                continue;
            }
            else if (final_unity - p_units <0   && final_unity!= 0 ) {
                    p_units= final_unity;
            }
            
            

        
            soldProducts.add(
                    SoldProduct.builder()
                            .units(p_units)
                            .price(product.getPrice())
                            .name(product.getName())
                            .category(product.getCategory())
                            .build());
            total++;
            price += product.getPrice() * p_units;
            repository.sellProduct(p_units, p_id);

        }
        // if para caso o user n conseguri comprar nenhum dos produtos um sale n é
        // efetuado
        if (!soldProducts.isEmpty()) {
            Sale sale = Sale.builder()
                    .emp_id(userid)
                    .date(LocalDateTime.now())
                    .total_price(price)
                    .total_units(total)
                    .build();

            paymentRepository.save(sale);
            soldProducts.forEach(soldProduct -> soldProduct.setSale_id(sale.getId()));
            soldProductRepository.saveAll(soldProducts);

            System.out.println("//////////////////////// Payment DONE /////////////////////////////");
            return FormattedResponse.builder()
                    .object(sale)
                    .response("Payment performed by user with id : " + uid + " at " + sale.getDate())
                    .request("Payment request by user with id : " + uid + " at " + sale.getDate())
                    .status(HttpStatus.OK)
                    .build();

        } else {
            // else ns se está ok mas só meti pq precisava do formattedresponse
            Sale sale = Sale.builder()
                    .emp_id(null)
                    .date(LocalDateTime.now())
                    .total_price(0)
                    .total_units(0)
                    .build();
            return FormattedResponse.builder()
                    .object(sale)
                    .response("Payment performed by user with id : " + uid + " at " + sale.getDate())
                    .request("Payment request by user with id : " + uid + " at " + sale.getDate())
                    .status(HttpStatus.OK)
                    .build();

        }

    }

  

    public FormattedResponse getProductById(Long id) {
        final Product p = repository.searchByID(id);
        FormattedResponse response;
        if (p == null) {
            response = FormattedResponse.builder()
                    .request("Get product of id  = " + id)
                    .response("Product not found (by its id) or out of stock")
                    .status(HttpStatus.NOT_FOUND)
                    .object(null)
                    .build();
        } else {
            response = FormattedResponse.builder()
                    .request("Get product of id  = " + id)
                    .response("Product fetched from database")
                    .status(HttpStatus.OK)
                    .object(p)
                    .build();
        }
        return response;

    }

    public FormattedResponse deleteById(Long id) {

        final Product p = repository.searchByID(id);
        FormattedResponse response;
        if (p == null) {
            response = FormattedResponse.builder()
                    .request("Delete product of id  = " + id)
                    .response("Product not found (by its id) or out of stock")
                    .status(HttpStatus.NOT_FOUND)
                    .object(null)
                    .build();
        } else {
            repository.deleteById(id);
            response = FormattedResponse.builder()
                    .request("Delete product of id  = " + id)
                    .response("Product deleted from database")
                    .status(HttpStatus.OK)
                    .object(p)
                    .build();
        }
        return response;
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public FormattedResponse updateById(Long id, Product p) {
        final Product product = repository.searchByID(id);
        FormattedResponse response;
        if (p == null) {
            response = FormattedResponse.builder()
                    .request("Update product of id  = " + id)
                    .response("Product not found (by its id)")
                    .status(HttpStatus.NOT_FOUND)
                    .object(null)
                    .build();
        } else {

            repository.updateProductById(id, p.getName(), p.getCategory(), p.getCurr_quantity(), p.getPrice(),
                    p.getProvider());

            response = FormattedResponse.builder()
                    .request("Update product of id  = " + id)
                    .response("Product updated in database")
                    .status(HttpStatus.OK)
                    .object(getProductById(id))
                    .build();
        }
        return response;
    }

    @Transactional
    public void save(Product p) {
        repository.save(p);
    }

    @Transactional
    public List<Boolean> saveAll(List<Product> Ps) {
        return Ps.stream().map(product -> saveWithTransactionCheck(product)).collect(Collectors.toList());
    }

    private boolean saveWithTransactionCheck(Product p) {
        final AtomicBoolean status = new AtomicBoolean();

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                try {
                    repository.save(p);

                    status.set(true);
                } catch (Exception e) {
                    transactionStatus.setRollbackOnly();
                    status.set(false);
                }
            }
        });

        return status.get();
    }

}
