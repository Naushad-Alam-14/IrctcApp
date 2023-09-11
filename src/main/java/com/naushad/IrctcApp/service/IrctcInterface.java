package com.naushad.IrctcApp.service;

import com.naushad.IrctcApp.model.Passenger;
import com.naushad.IrctcApp.model.PersonalDetail;
import com.naushad.IrctcApp.model.Refund;
import com.naushad.IrctcApp.model.Ticket;

import java.util.List;

public interface IrctcInterface {

    Ticket bookTicket(Passenger passenger);
    Refund cancelTicket(String pnr);
    Ticket checkPnrStatus(String pnr);

    List<PersonalDetail> findAllPersonalDetails();
}
