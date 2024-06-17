package iess.pt.controller;

import iess.pt.entity.Sale;
import iess.pt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class SalesController extends RootApi
{
    @Autowired
    private UserService service;


    @GetMapping("/sales/{id}")
    public List<Sale> getEmpSales(@PathVariable("id") Long id){
        return service.AllSalesByEmp(id);
    }


    @GetMapping("/sales")
    public Map<String, Double> getEmpSales(){
        return service.AllEmployeesInvoices();
    }
}
