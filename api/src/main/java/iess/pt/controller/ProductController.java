package iess.pt.controller;

import iess.pt.entity.Product;
import iess.pt.entity.Template.FormattedResponse;
import iess.pt.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/")
//@CrossOrigin(origins = "*") // Adicione esta linha
public class ProductController //extends RootApi
{
    @Autowired
    private ProductService service;
    @GetMapping("stock/test")
    public ResponseEntity<String> test() {return new ResponseEntity<>("Test from api/stock", HttpStatus.OK);}


    @GetMapping("stock/products")
    public List<Product> getAllProducts()
    {
        return service.getAll()   ;
    }

    @GetMapping("stock/{name}")
    public ResponseEntity<FormattedResponse> getProductByName(@PathVariable("name") String name)
    {
        return service.getProductByName(name).response();
    }

    @GetMapping("stock/product")
    public ResponseEntity<FormattedResponse> getProductByName(@RequestParam Long id)
    {
        return service.getProductById(id).response();
    }

    @PostMapping("stock/")
    public ResponseEntity<String> addProduct(@RequestBody Product p)
    {
        service.save(p);
        return new ResponseEntity<>("Product added ", HttpStatus.OK);
    }

    @PutMapping("stock/{id}")
    public ResponseEntity<FormattedResponse> updateById(@PathVariable("id") Long id, @RequestBody Product p)
    {
        return service.updateById(id, p).response();

    }


    @DeleteMapping("stock/{id}")
    public ResponseEntity<FormattedResponse> deleteById(@PathVariable("id") Long id)
    {
        return service.deleteById(id).response();
    }


    @PostMapping("stock/storeAll")
    public ResponseEntity<String> addProducts(@RequestBody List<Product> products)
    {
        final List<Boolean> resultOfTransactions = service.saveAll(products);

        final boolean hadBadTransaction = resultOfTransactions.stream().anyMatch(status -> !status);

        String status = "All the products saved successfly";

        if(hadBadTransaction){
            status = "Failed to save all the products";
        }

        return new ResponseEntity<>(status, HttpStatus.OK);
    }


    @DeleteMapping("stock/delete/all")
    public ResponseEntity<String> deleteAll()
    {
        service.deleteAll();

        return new ResponseEntity<>("All Products deleted sucessfully",  HttpStatus.OK);
    }




}
