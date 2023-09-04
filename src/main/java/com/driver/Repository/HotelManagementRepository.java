package com.driver.Repository;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Repository
public class HotelManagementRepository {
    HashMap<String,Hotel> HotelDB = new HashMap<>();
    HashMap<Integer,User> UserDB = new HashMap<>();
    HashMap<String,Booking> BookingDB = new HashMap<>();

    public String addHotel(Hotel hotel) {
        if(hotel.getHotelName()==null || hotel==null || HotelDB.containsKey(hotel.getHotelName())==true){
            return "FAILURE";
        }
        HotelDB.put(hotel.getHotelName(),hotel);
        return "SUCCESS";
    }

    public Integer addUser(User user) {
        UserDB.put(user.getaadharCardNo(),user);
        return user.getaadharCardNo();
    }

    public String getHotelWithMostFacilities() {
        String h = "";
        int max = 0;
        for(var ele : HotelDB.keySet()){
            int n = HotelDB.get(ele).getFacilities().size();
            if(max<n){
                max=n;
                h=HotelDB.get(ele).getHotelName();
            }
            else if(max==n){
                String hotelName = HotelDB.get(ele).getHotelName();
                int compare = h.compareTo(hotelName);
                if(compare>0){
                    h=hotelName;
                }
            }
        }
        if(max==0) return "";
        return h;
    }

    public int bookARoom(Booking booking) {
        if (booking == null || booking.getHotelName() == null || !HotelDB.containsKey(booking.getHotelName())) {
            return -1;
        }

        // Get the hotel from the database
        Hotel hotel = HotelDB.get(booking.getHotelName());

        // Calculate the total amount paid
        int totalAmountPaid = booking.getNoOfRooms() * hotel.getPricePerNight();

        // Check if there are enough rooms available
        if (booking.getNoOfRooms() > hotel.getAvailableRooms()) {
            return -1;
        }

        // Update the available rooms in the hotel
        hotel.setAvailableRooms(hotel.getAvailableRooms() - booking.getNoOfRooms());

        // Generate a random UUID for the booking
        String bookingId = UUID.randomUUID().toString();
        booking.setBookingId(bookingId);

        // Store the booking in the database
        BookingDB.put(bookingId, booking);

        // Set the amount to be paid in the booking
        booking.setAmountToBePaid(totalAmountPaid);

        return totalAmountPaid;// Check if the booking or hotel does not exist

    }

    public int getBookings(Integer aadharCard) {
       int val = aadharCard.intValue();
       int cnt = 0;
       for(Booking booking : BookingDB.values()){
           if(booking.getBookingAadharCard()==val){
               cnt++;
           }
       }
       return cnt;
    }

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName) {
        if (!HotelDB.containsKey(hotelName)) {
            return null; // Hotel doesn't exist
        }

        // Get the hotel from the database
        Hotel hotel = HotelDB.get(hotelName);

        // Get the existing facilities for the hotel
        List<Facility> existingFacilities = hotel.getFacilities();

        // Add new facilities to the existing ones if they are not already present
        for (Facility facility : newFacilities) {
            if (!existingFacilities.contains(facility)) {
                existingFacilities.add(facility);
            }
        }

        // Update the facilities for the hotel
        hotel.setFacilities(existingFacilities);

        // Update the hotel in the database
        HotelDB.put(hotelName, hotel);

        return hotel; // Return the updated hotel
    }
}
