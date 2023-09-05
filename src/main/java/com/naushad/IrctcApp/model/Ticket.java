package com.naushad.IrctcApp.model;

import lombok.*;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    private Passenger passenger;
    private double fare;
    private String PNR;
    private Date bookingDate;
}
