package com.naushad.IrctcApp.model;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonalDetail {
    private String name;
    private String aadhaarNo;
    private int age;
    private String mobileNo;
}
