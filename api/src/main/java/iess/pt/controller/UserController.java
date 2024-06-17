package iess.pt.controller;

import iess.pt.entity.Sale;
import iess.pt.entity.User;
import iess.pt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController extends RootApi
{
    @Autowired
    private UserService service;
    @GetMapping("/employees/all")
    public List<User> getAll(){
        return service.getAll();
    }




}
