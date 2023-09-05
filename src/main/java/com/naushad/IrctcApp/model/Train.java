package com.naushad.IrctcApp.model;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Train {
    private int trainNo;
    private String type;
    private int capacity;
    private double fare;
}
