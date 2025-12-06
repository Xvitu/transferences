package com.xvitu.transferences.boundary;


import com.xvitu.transferences.boundary.requests.TransferRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransferController {

    @PostMapping("/transfer")
    public void transfer(@RequestBody TransferRequest request) {

    }


}
