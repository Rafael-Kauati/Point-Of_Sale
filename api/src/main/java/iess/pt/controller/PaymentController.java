package iess.pt.controller;

import iess.pt.entity.Template.FormattedResponse;
import iess.pt.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController
{

    @Autowired
    private final ProductService service;


    @PostMapping("/{user_id}")
    //                                                                                                   Product id   Units
    public ResponseEntity<FormattedResponse> payment(@PathVariable("user_id") Long uid, @RequestBody Map<Long      , Integer> cart)
    {
        return service.performPayment(uid, cart).response();
    }


}
