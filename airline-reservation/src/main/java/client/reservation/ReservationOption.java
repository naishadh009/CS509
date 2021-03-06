/**
 * 
 */
package client.reservation;

import client.flight.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
/**
 * This class is an amalgamation class of all Flights needed to create a potential
 * travel option that gets a user to their destination. It provides the superset
 * class functionality to get key details about all the individual flights.
 * 
 * @author James
 * @version 1
 * @since 07/03/2016
 *
 */
public class ReservationOption {

	/**
	 * Data attributes for a ReservationOption
	 */
	private ArrayList<Flight> flightList;
	
	/**
	 * default constructor
	 * 
	 * The default constructor initializes the object instance to default / invalid values.
	 * 
	 * precondition none
	 * postcondition attributes are initialized with valid structures 
	 */
	public ReservationOption() {
		this.flightList = new ArrayList<Flight>();
	}
	/**
	 * constructor with all required field values supplied individually
	 * 
	 * This constructor will create a valid ReservationOption object.
	 * The constructor takes a set of individual flights and stores them into its internal data
	 * attributes.
	 * 
	 * @param flightOne is the first flight
	 * @param flightTwo is the second flight
	 * @param flightThree is the third flight
	 * 
	 * precondition valid flights
	 * postcondition attributes are initialized with valid values 
	 */
	public ReservationOption(
			Flight flightOne,
			Flight flightTwo,
			Flight flightThree) {
		this.flightList = new ArrayList<Flight>();
		if(flightOne != null) { 
			this.flightList.add(flightOne);
		}
		if(flightTwo != null) { 
			this.flightList.add(flightTwo);
		}
		if(flightThree != null) {
			this.flightList.add(flightThree);
		}
	}
	/**
	 * constructor with all required field values supplied individually
	 * 
	 * This constructor will create a valid ReservationOption object.
	 * The constructor takes a set of individual flights and stores them into its internal data
	 * attributes.
	 * 
	 * @param flightOne is the first flight
	 * @param flightTwo is the second flight
	 * 
	 * precondition valid flights
	 * postcondition attributes are initialized with valid values 
	 */
	public ReservationOption(Flight flightOne, Flight flightTwo) {
		this(flightOne, flightTwo, null);
	}
	/**
	 * constructor with all required field values supplied individually
	 * 
	 * This constructor will create a valid ReservationOption object.
	 * The constructor takes a set of individual flights and stores them into its internal data
	 * attributes.
	 * 
	 * @param flightOne is the first flight
	 * 
	 * precondition valid flights
	 * postcondition attributes are initialized with valid values 
	 */
	public ReservationOption(Flight flightOne) {
		this(flightOne,null,null);
	}
	/**
	 * constructor with all required field values supplied
	 * 
	 * This constructor will create a valid ReservationOption object.
	 * The constructor takes an ArrayList to initiate the object.
	 * 
	 * @param flightList is a list of flights that make up the option.
	 * 
	 * precondition valid flights
	 * postcondition attributes are initialized with valid values 
	 */
	public ReservationOption(ArrayList<Flight> flightList) {
		this.flightList = flightList;
	}
	/**
	 * get an individual Flight from the ArrayList based on index
	 * aligns with which leg of the overall trip (layovers)
	 * 
	 * @param index of Flight in list
	 * 
	 * @return the Flight object at the index
	 */
	public Flight getFlight(int index) {
		Flight flight;
		try {
			flight = this.flightList.get(index);
		} catch (Exception ex) {
			flight = null;
		}
		return flight;
	}
	/**
	 * get total number of Flights in the reservation option
	 * 
	 * @return the number of flights
	 */
	public int getNumFlights() {
		try {
			return this.flightList.size();
		} catch (Exception ex) {
			return 0;
		}
	}
	/**
	 * get number of layovers in the reservation option
	 * 
	 * @return the number of layovers
	 */
	public int getNumLayovers() {
		try {
			return this.flightList.size()-1;
		} catch (Exception ex) {
			return 0;
		}
	}
	/**
	 * get price of the reservation option
	 * 
	 * @param seatPreference is a string of type 'firstclass' or 'coach'
	 * 
	 * @return the total price of all individual flights combined
	 */
	public double getPrice(String seatPreference) {
		double totalPrice = 0.0;
		boolean firstClass = false;
		if(seatPreference.equals("firstclass")) {
			firstClass = true;
		} else {
			firstClass = false;
		}
		for (Flight temp : this.flightList) {
			if(firstClass) {
				String price = temp.getmPriceFirstclass();
				price = price.substring(1, price.length());
				totalPrice += Double.parseDouble(price);
			} else {
				String price = temp.getmPriceCoach();
				price = price.substring(1, price.length());
				totalPrice += Double.parseDouble(price);
			}
		}
		return totalPrice;
	}
	/**
	 * get total travel time of the reservation option
	 * 
	 * @return the total travel time from start to finish in hours:minutes format
	 */
	public String getTotalTime() {
		DateTimeFormatter flightDateFormat = DateTimeFormatter.ofPattern("yyyy MMM d H:m z");
		long totalTime = 0;
		try {
			LocalDateTime departTimeLocal = LocalDateTime.parse(this.getFlight(0).getmTimeDepart(),flightDateFormat);
			ZonedDateTime departTimeZoned = departTimeLocal.atZone(ZoneId.of("GMT"));
			long departTime = departTimeZoned.toInstant().toEpochMilli();
			LocalDateTime arrivalTimeLocal = LocalDateTime.parse(this.getFlight(this.getNumFlights()-1).getmTimeArrival(), flightDateFormat);
			ZonedDateTime arrivalTimeZoned = arrivalTimeLocal.atZone(ZoneId.of("GMT"));
			long arrivalTime = arrivalTimeZoned.toInstant().toEpochMilli();
			totalTime = arrivalTime - departTime;
		} catch (DateTimeParseException ex) {
			ex.printStackTrace();
		}
		return String.format("%02d:%02d",
				TimeUnit.MILLISECONDS.toHours(totalTime),
				TimeUnit.MILLISECONDS.toMinutes(totalTime) % TimeUnit.HOURS.toMinutes(1)
				);
	}
}
