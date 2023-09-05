package com.naushad.IrctcApp.service;

import com.naushad.IrctcApp.model.Passenger;
import com.naushad.IrctcApp.model.Ticket;

public interface IrctcInterface {

    Ticket bookTicket(Passenger passenger);
}
