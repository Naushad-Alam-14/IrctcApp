package com.naushad.IrctcApp.controller;


import com.naushad.IrctcApp.model.Passenger;
import com.naushad.IrctcApp.model.Ticket;
import com.naushad.IrctcApp.service.impl.IrctcImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class IrctcController {

    @Autowired
    private IrctcImpl irctcImpl;

    @PostMapping("/book")
    public ResponseEntity<Object> bookTicket(@RequestBody Passenger passenger){
        Ticket ticket = irctcImpl.bookTicket(passenger);

        return new ResponseEntity<>(ticket, HttpStatus.OK);
    }

    @GetMapping("/checkPnrStatus")
    public Ticket checkPnrStatus(@RequestParam String pnr){
        return irctcImpl.checkPnrStatus(pnr);
    }
}
