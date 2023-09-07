package com.naushad.IrctcApp.controller;


import com.naushad.IrctcApp.model.Passenger;
import com.naushad.IrctcApp.model.Refund;
import com.naushad.IrctcApp.model.Ticket;
import com.naushad.IrctcApp.service.IrctcInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

@RestController
public class IrctcController {

    @Autowired
    private IrctcInterface irctcInterface;

    @PostMapping("/book")
    public ResponseEntity<Object> bookTicket(@RequestBody Passenger passenger, WebRequest webRequest){
        Ticket ticket = irctcInterface.bookTicket(passenger);

        return new ResponseEntity<>(ticket, HttpStatus.OK);
    }

    @GetMapping("/checkPnrStatus")
    public Ticket checkPnrStatus(@RequestParam String pnr){
        return irctcInterface.checkPnrStatus(pnr);
    }
    @DeleteMapping("/cancelTicket")
    public Refund cancelTicket(@RequestBody String pnr) {
        return irctcInterface.cancelTicket(pnr);
    }

}
