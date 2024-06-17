package iess.pt.service;

import iess.pt.dataAccess.PaymentRepository;
import iess.pt.dataAccess.userRepository;
import iess.pt.entity.Sale;
import iess.pt.entity.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class UserService{

    @Autowired
    private userRepository repository;

    @Autowired
    private PaymentRepository paymentRepository;

    public List<User> getAll()
    {
        return repository.findAll();
    }

    public User getByName(@PathVariable("name") String name)
    {
        return repository.findByName(name);
    }


    public List<Sale> AllSalesByEmp(Long id){ return paymentRepository.getAllByEmp(id);}


    public Map<String, Double> AllEmployeesInvoices(){

        Map<String, Double> empsInv =  new HashMap<>();

        List<Long> ids = repository.getEmpsId();

        List<Integer> allInvoices =  new ArrayList<>();
        double price = 0.0;

        for(Long id : ids){
            System.out.println("\n\n ========= "+ repository.findById(id)+" ========= \n\n");

            allInvoices = repository.getAllEmployeesInvoices(id);
            System.out.println("\n ======= "+ allInvoices);

            for(double p : allInvoices)
            {
                price += p;
            }

            empsInv.put(repository.getEmpsName(id), price);
            price = 0.0;
        }
        System.out.println(empsInv);
        return empsInv;
    }


}
