package com.naushad.IrctcApp.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Passenger {
    private String source;
    private String destination;
    private Date dateOfTravel;
    private int trainNo;
    private PersonalDetail personalDetail;
    private int noOfSeats;

    private String foodType;
}
