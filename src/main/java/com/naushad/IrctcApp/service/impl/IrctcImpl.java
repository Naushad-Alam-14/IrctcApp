package com.naushad.IrctcApp.service.impl;

import com.naushad.IrctcApp.model.Passenger;
import com.naushad.IrctcApp.model.Ticket;
import com.naushad.IrctcApp.model.exception.InvalidPNRException;
import com.naushad.IrctcApp.model.exception.SeatNotFoundException;
import com.naushad.IrctcApp.repository.IrctcRepository;
import com.naushad.IrctcApp.service.IrctcInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IrctcImpl implements IrctcInterface {

    @Autowired
    private IrctcRepository irctcRepository;

    @Override
    public Ticket bookTicket(Passenger passenger) {
        // 1. Availability
        // 2. yes - book. No - SeatNotAvailable
        boolean isSeatAvailable = irctcRepository.checkSeatIsAvailable(passenger.getTrainNo(),
                passenger.getNoOfSeats(),passenger.getDateOfTravel());
        if(!isSeatAvailable){
            // throw seatNotavailable exception
            throw new SeatNotFoundException("Seat is not available");
        }
        return irctcRepository.bookTicket(passenger);
    }

    public Ticket checkPnrStatus(String pnr) {
        Ticket ticket = irctcRepository.checkPnrStatus(pnr);
        if(ticket == null){
            throw new InvalidPNRException("PNR is not valid - " + pnr);
        }
        return ticket;
    }
}
