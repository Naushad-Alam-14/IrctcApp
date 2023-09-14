package com.naushad.IrctcApp.repository;

import com.naushad.IrctcApp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class IrctcJdbcRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Value("${vegPrice}")
    private double vegPrice;
    @Value("${nonVegPrice}")
    private double nonVegPrice;

    public List<PersonalDetail> findAllPersonalDetails() {
        return jdbcTemplate.query("select * from personalDetail", new BeanPropertyRowMapper<>(PersonalDetail.class));
    }

    public List<PersonalDetail> findDetailByAge(int age) {
        Object[] parameters = new Object[]{age};
        return jdbcTemplate.query("select * from personalDetail where age =?",parameters,
                new BeanPropertyRowMapper<>(PersonalDetail.class));
    }

    public PersonalDetail getPersonalDetailByAadhaarNo(String aadhaarNo){

        String query = "select * from personalDetail where aadhaarNo=" + "'" + aadhaarNo + "'";
//        Object[] parameters = new Object[]{aadhaarNo};
        return jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(PersonalDetail.class));
    }

    public String deleteByAadhaarNo(String aadhaarNo){
        Object[] parameters = new Object[]{aadhaarNo};

        int deletedRows = jdbcTemplate.update("delete from personalDetail where aadhaarNo=?",parameters);
       if(deletedRows > 0)
           return "Successfully deleted person with aadhaarNo - " + aadhaarNo;
       return "Not able to delete";
    }

    public Ticket bookTicket(Passenger passenger){
        PersonalDetail personalDetail = passenger.getPersonalDetail();
        boolean isAlreadyRegistered = alreadyRegistered(personalDetail);
        boolean insertedPersonalDetail = true;
        if(!isAlreadyRegistered){
            insertedPersonalDetail = insertPersonalDetail(personalDetail);
        }
        boolean insertedPassenger;
        boolean success = false;
        Ticket ticket = null;
        if(insertedPersonalDetail){
            insertedPassenger = insertPassengerDetail(passenger);
            if(insertedPassenger){
                int passengerId = getPassengerId();
                passenger.setId(passengerId);
                ticket = new Ticket();
                ticket.setPassenger(passenger);
                ticket.setBookingDate(new Date());
                ticket.setFare(calculateFare(passenger));
                ticket.setPNR(generatePNR());
                success = insertTicket(ticket);
            }
        }
        if(!success)
            throw new RuntimeException("Not able to book ticket");
        return ticket;
    }

    private int getPassengerId() {
        return 0;
    }

    private boolean insertTicket(Ticket ticket){
        String query = "insert into Ticket(fare,pnr,bookingDate,passengerId) " +
                "values(?,?,?,?)";
        Object[] parameters = new Object[]{
                ticket.getFare(),ticket.getPNR(),ticket.getBookingDate(),ticket.getPassenger().getId()
        };
        return jdbcTemplate.update(query,parameters,Integer.class) >= 0;
    }

    private boolean insertPassengerDetail(Passenger passenger){
        String query = "insert into Passenger(source,destination,dateOfJourney,trainNo,noOfSeats,foodType,aadhaarNo) " +
                "values(?,?,?,?,?,?,?)";
        Object[] parameters = new Object[]{
                passenger.getSource(),passenger.getDestination(),passenger.getDateOfJourney(),passenger.getTrainNo(),
                passenger.getNoOfSeats(),passenger.getFoodType(),passenger.getPersonalDetail().getAadhaarNo()
        };
        return jdbcTemplate.update(query,parameters,Integer.class) >= 0;
    }

    private boolean insertPersonalDetail(PersonalDetail personalDetail){

        String query = "Insert into personaldetail(name,aadhaarNo,age,mobileNo) values(?,?,?,?)";
        Object[] parameter = new Object[]{
                personalDetail.getName(),personalDetail.getAadhaarNo(),personalDetail.getAge(),personalDetail.getMobileNo()
        };
        int count = jdbcTemplate.update(query,parameter,Integer.class);
        return count > 0;
    }

    private boolean alreadyRegistered(PersonalDetail newPersonalDetail){
        PersonalDetail personalDetail = getPersonalDetailByAadhaarNo(newPersonalDetail.getAadhaarNo());
        if(personalDetail != null){
            if(personalDetail.equals(newPersonalDetail)){
                return true;
            }else{
                deleteByAadhaarNo(newPersonalDetail.getAadhaarNo());
                return false;
            }
        }
        return false;
    }

    public boolean checkSeatIsAvailable(int trainNo, int seatToBeBooked, Date dateOfJourney){
        int totalBookedSeat = countAllBookingByDateAndTrainNo(dateOfJourney,trainNo);
        Train train = getTrainByTrainNo(trainNo);
        return (train.getCapacity() - totalBookedSeat) >= seatToBeBooked;
    }

    public int countAllBookingByDateAndTrainNo(Date dateOfJourney,int trainNo){
        String query = "select sum(noOfSeats) from Passenger where dateOfJourney =? and trainNo =?";
        Object[] parameters = new Object[]{dateOfJourney,trainNo};
        return jdbcTemplate.queryForObject(query,parameters,Integer.class);
    }

    public Train getTrainByTrainNo(int trainNo){
        String query = "select * from train where trainNo = ?";
        Object[] parameters = new Object[]{trainNo};
        return jdbcTemplate.queryForObject(query,parameters,new BeanPropertyRowMapper<>(Train.class));
    }

    public double calculateFare(Passenger passenger){
        Train train = getTrainByTrainNo(passenger.getTrainNo());
        double totalTicketPrice = train.getFare() * passenger.getNoOfSeats();
        if(Constants.FoodType.VEG.equals(passenger.getFoodType())){
            totalTicketPrice += (vegPrice* passenger.getNoOfSeats());
        } else if (Constants.FoodType.NON_VEG.equals(passenger.getFoodType())) {
            totalTicketPrice += (nonVegPrice* passenger.getNoOfSeats());
        }
        return totalTicketPrice;
    }

    public String generatePNR(){
        return (new Random().nextInt(1000)) + "";
    }

}
