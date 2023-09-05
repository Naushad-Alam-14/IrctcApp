package com.naushad.IrctcApp.repository;

import com.naushad.IrctcApp.model.Passenger;
import com.naushad.IrctcApp.model.Ticket;
import com.naushad.IrctcApp.model.Train;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class IrctcRepository {

    // key = trainNo, value - Train details
    Map<Integer, Train> trainMap = new HashMap<>();

    Map<Date,Map<Integer, List<Passenger>>> bookedSeatByDate = new TreeMap<>();

    Map<String,Ticket> ticketMap = new HashMap<>();

    private final double vegPrice = 70;
    private final double nonVegPrice = 100;

    public IrctcRepository(){
        trainMap.put(120101, new Train(120101,"Express",180,500));
        trainMap.put(120102, new Train(120102,"Express",180,500));
        trainMap.put(120103, new Train(120103,"Express",180,500));
        trainMap.put(120104, new Train(120104,"Superfast",120,1000));
        trainMap.put(120105, new Train(120105,"Superfast",120,1000));
        trainMap.put(120106, new Train(120106,"Superfast",120,1000));
        trainMap.put(120107, new Train(120107,"Superfast",120,1000));
        trainMap.put(120108, new Train(120108,"Superfast",120,1000));
        trainMap.put(120109, new Train(120109,"Rajdhani",100,1500));
        trainMap.put(120110, new Train(120110,"Rajdhani",100,1500));
    }


    public Ticket bookTicket(Passenger passenger){
        // First get the map for a particular date
        Map<Integer, List<Passenger>> passengersTrainMap= bookedSeatByDate.get(passenger.getDateOfTravel());
        // If there is no booking for a particular date then create a new map for booking of a particular date
        if(passengersTrainMap == null){
            passengersTrainMap = new HashMap<>();
        }
        List<Passenger> passengerList = passengersTrainMap.get(passenger.getTrainNo());
        if(passengerList == null){
            passengerList = new ArrayList<>();
        }
        passengerList.add(passenger);
        passengersTrainMap.put(passenger.getTrainNo(),passengerList);

        bookedSeatByDate.put(passenger.getDateOfTravel(),passengersTrainMap);

        Ticket ticket = new Ticket();
        ticket.setPassenger(passenger);
        ticket.setBookingDate(new Date());
        ticket.setFare(calculateFare(passenger));
        ticket.setPNR(generatePNR());
        ticketMap.put(ticket.getPNR(),ticket);

        return ticket;
    }

    public String generatePNR(){
        return (new Random().nextInt(1000)) + "";
    }

    public double calculateFare(Passenger passenger){
        Train train = trainMap.get(passenger.getTrainNo());
        double totalTicketPrice = train.getFare() * passenger.getNoOfSeats();
        if("Veg".equals(passenger.getFoodType())){
            totalTicketPrice += (vegPrice* passenger.getNoOfSeats());
        } else if ("Non-Veg".equals(passenger.getFoodType())) {
            totalTicketPrice += (nonVegPrice* passenger.getNoOfSeats());
        }
        return totalTicketPrice;
    }

    public boolean checkSeatIsAvailable(int trainNo, int seatToBeBooked, Date travellingDate){
        Map<Integer, List<Passenger>> passengersTrainMap= bookedSeatByDate.get(travellingDate);
        if(passengersTrainMap != null ){
            List<Passenger> passengerList = passengersTrainMap.get(trainNo);
            int totalBookedSeat = 0;
            if(passengerList != null) {
                for (Passenger passenger : passengerList) {
                    totalBookedSeat += passenger.getNoOfSeats();
                }
            }
            Train train = trainMap.get(trainNo);
            if(train.getCapacity() - totalBookedSeat >= seatToBeBooked){
                return true;
            }else {
                return false;
            }
        }

        return true;
    }

    public Ticket checkPnrStatus(String pnr) {
        return ticketMap.get(pnr);
    }
}
