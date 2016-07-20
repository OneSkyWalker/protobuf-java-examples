// See .\protobuf-2.6.1\examples\README.txt for information and build instructions.

// Please note that I am novice Java programmer.  I wrote this code to be thorough
// (that is, find everything in a feedMessage), not to be efficient.  Even though
// I attempted to be thorough, I may have overlooked some fields that are in some
// feedMessages.

import com.google.transit.realtime.GtfsRealtime.FeedMessage;
import com.google.transit.realtime.GtfsRealtime.FeedHeader;
import com.google.transit.realtime.GtfsRealtime.FeedEntity;
import com.google.transit.realtime.GtfsRealtime.TripUpdate;
import com.google.transit.realtime.GtfsRealtime.VehiclePosition;
import com.google.transit.realtime.GtfsRealtime.Alert;
import com.google.transit.realtime.GtfsRealtime.TripDescriptor;
import com.google.transit.realtime.GtfsRealtime.VehicleDescriptor;
import com.google.transit.realtime.GtfsRealtime.Position;
import com.google.transit.realtime.GtfsRealtime.TripUpdate.StopTimeUpdate;
import com.google.transit.realtime.GtfsRealtime.TripUpdate.StopTimeEvent;
import java.util.Date;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;

class ListBusInfo {
  // Iterates though all messages in feedMessage and prints information contained in the messages.
  static void Print(FeedMessage feedMessage) {

    //
    // Logic to print header in feedMessage
    //
    if (feedMessage.hasHeader()) {
      FeedHeader feedHeader=feedMessage.getHeader();
      System.out.println("Descriptor: " + feedHeader.getDescriptor());
      if (feedHeader.hasGtfsRealtimeVersion()) {System.out.println("Realtime Version: " + feedHeader.getGtfsRealtimeVersion());}
      if (feedHeader.hasIncrementality()) {System.out.println("Incrementality: " + feedHeader.getIncrementality());}
      if (feedHeader.hasTimestamp()) {System.out.println("Timestamp: " + new Date(feedHeader.getTimestamp()*1000));}
      System.out.println("Unknown header fields: " + feedHeader.getUnknownFields());
      }

    //
    // Logic to print each entity in feedMessage
    //
    if (feedMessage.getEntityCount() == 1) {System.out.println("Feed has 1 entity:");}
    else {System.out.println("Feed has " + feedMessage.getEntityCount() + " entities:");}
    for (FeedEntity feedEntity: feedMessage.getEntityList()) {

      System.out.println();
      System.out.println("  Entity ID: " + feedEntity.getId());
      if (feedEntity.hasIsDeleted()) {
      	if (feedEntity.getIsDeleted()) {System.out.println("  Entity is deleted.");}
	else {System.out.println("  Entity is not deleted.");}
	}

      //
      // Logic to print information in a Trip Update entity
      //
      if (feedEntity.hasTripUpdate()) {
        System.out.println("  Entity is Trip Update:");
      	TripUpdate tripUpdate = feedEntity.getTripUpdate();
	TripDescriptor tripDescriptor = tripUpdate.getTrip();
	System.out.print("    ");
	String spacer = "";
	if (tripDescriptor.hasTripId()) {System.out.print(spacer + "trip ID: " + tripDescriptor.getTripId()); spacer=", ";}
	if (tripDescriptor.hasRouteId()) {System.out.print(spacer + "route ID: " + tripDescriptor.getRouteId()); spacer=", ";}
	if (tripDescriptor.hasDirectionId()) {System.out.print(spacer + "direction ID: " + tripDescriptor.getDirectionId()); spacer=", ";}
	if (tripDescriptor.hasStartTime()) {System.out.print(spacer + "start time: " + tripDescriptor.getStartTime()); spacer=", ";}
	if (tripDescriptor.hasStartDate()) {System.out.print(spacer + "start time: " + tripDescriptor.getStartDate()); spacer=", ";}
	if (tripDescriptor.hasScheduleRelationship()) {System.out.print(spacer + "schedule relationship: " + tripDescriptor.getScheduleRelationship()); spacer=", ";}
	System.out.println(spacer + "unknown trip fields:" + tripDescriptor.getUnknownFields());
	spacer = "";

	if (tripUpdate.hasVehicle()) {
	  VehicleDescriptor vehicleDescriptor = tripUpdate.getVehicle();
	  System.out.print("    ");
	  if (vehicleDescriptor.hasId()) {System.out.print(spacer + "vehicle ID: " + vehicleDescriptor.getId()); spacer=", ";}
	  if (vehicleDescriptor.hasLabel()) {System.out.print(spacer + "label: " + vehicleDescriptor.getLabel()); spacer=", ";}
	  if (vehicleDescriptor.hasLicensePlate()) {System.out.print(spacer + "license plate: " + vehicleDescriptor.getLicensePlate()); spacer=", ";}
	  System.out.println(spacer + "unknown vehicle fields:" + vehicleDescriptor.getUnknownFields());
	  spacer = "";
	  }

        if (tripUpdate.getStopTimeUpdateCount() == 1) {System.out.println("    Trip Update has 1 stop time update:");}
	else {System.out.println("    Trip Update has " + tripUpdate.getStopTimeUpdateCount() + " stop time updates:");}
        for (StopTimeUpdate stopTimeUpdate: tripUpdate.getStopTimeUpdateList()) {

	  System.out.println();
	  System.out.print("      ");
          if (stopTimeUpdate.hasStopSequence()) {System.out.print(spacer + "stop sequence: " + stopTimeUpdate.getStopSequence()); spacer=", ";}
	  if (stopTimeUpdate.hasStopId()) {System.out.print(spacer + "stop ID: " + stopTimeUpdate.getStopId()); spacer=", ";}
	  if (stopTimeUpdate.hasArrival()) {

	    StopTimeEvent arrival = stopTimeUpdate.getArrival();
	    System.out.println();
	    System.out.print("      ");
	    spacer="";
	    if (arrival.hasDelay()) {System.out.print(spacer + "arrival delay: " + arrival.getDelay()); spacer=", ";}
	    if (arrival.hasTime()) {System.out.print(spacer + "arrival time: " + new Date(arrival.getTime()*1000)); spacer=", ";}
	    if (arrival.hasUncertainty()) {System.out.print(spacer + "arrival uncertainty: " + arrival.getUncertainty()); spacer=", ";}
	    System.out.print(spacer + "unknown arrival fields:" + arrival.getUnknownFields());
	    }
	  if (stopTimeUpdate.hasDeparture()) {

	    StopTimeEvent departure = stopTimeUpdate.getDeparture();
	    System.out.println();
	    System.out.print("      ");
	    spacer="";
	    if (departure.hasDelay()) {System.out.print(spacer + "departure delay: " + departure.getDelay()); spacer=", ";}
	    if (departure.hasTime()) {System.out.print(spacer + "departure time: " + new Date(departure.getTime()*1000)); spacer=", ";}
	    if (departure.hasUncertainty()) {System.out.print(spacer + "departure uncertainty: " + departure.getUncertainty()); spacer=", ";}
	    System.out.print(spacer + "unknown departure fields:" + departure.getUnknownFields());
	    }

	  System.out.println();
	  System.out.print("      ");
	  spacer="";
	  if (stopTimeUpdate.hasScheduleRelationship()) {System.out.print(spacer + "schedule relationship: " + stopTimeUpdate.getScheduleRelationship()); spacer=", ";}
	  System.out.println(spacer + "unknown stop time update fields:" + stopTimeUpdate.getUnknownFields());
	  spacer = "";

	  }

        }

      //
      // Logic to print information in a Vehicle Location entity
      //
      if (feedEntity.hasVehicle()) {
        System.out.println("  Entity is Vehicle Location:");
      	VehiclePosition vehiclePosition = feedEntity.getVehicle();
      	String spacer = "";

      	if (vehiclePosition.hasTrip()) {
	  TripDescriptor tripDescriptor = vehiclePosition.getTrip();
	  System.out.print("    ");
	  if (tripDescriptor.hasTripId()) {System.out.print(spacer + "trip ID: " + tripDescriptor.getTripId()); spacer=", ";}
	  if (tripDescriptor.hasRouteId()) {System.out.print(spacer + "route ID: " + tripDescriptor.getRouteId()); spacer=", ";}
	  if (tripDescriptor.hasDirectionId()) {System.out.print(spacer + "direction ID: " + tripDescriptor.getDirectionId()); spacer=", ";}
	  if (tripDescriptor.hasStartTime()) {System.out.print(spacer + "start time: " + tripDescriptor.getStartTime()); spacer=", ";}
	  if (tripDescriptor.hasStartDate()) {System.out.print(spacer + "start time: " + tripDescriptor.getStartDate()); spacer=", ";}
	  if (tripDescriptor.hasScheduleRelationship()) {System.out.print(spacer + "schedule relationship: " + tripDescriptor.getScheduleRelationship()); spacer=", ";}
	  System.out.println(spacer + "unknown trip fields:" + tripDescriptor.getUnknownFields());
	  spacer = "";
          }

	if (vehiclePosition.hasVehicle()) {
	  VehicleDescriptor vehicleDescriptor = vehiclePosition.getVehicle();
	  System.out.print("    ");
	  if (vehicleDescriptor.hasId()) {System.out.print(spacer + "vehicle ID: " + vehicleDescriptor.getId()); spacer=", ";}
	  if (vehicleDescriptor.hasLabel()) {System.out.print(spacer + "label: " + vehicleDescriptor.getLabel()); spacer=", ";}
	  if (vehicleDescriptor.hasLicensePlate()) {System.out.print(spacer + "license plate: " + vehicleDescriptor.getLicensePlate()); spacer=", ";}
	  System.out.println(spacer + "unknown vehicle fields:" + vehicleDescriptor.getUnknownFields());
	  spacer = "";
	  }

	if (vehiclePosition.hasPosition()) {
	  Position position = vehiclePosition.getPosition();
	  System.out.print("    ");
	  if (position.hasLatitude()) {System.out.print(spacer + "latitude: " + position.getLatitude()); spacer=", ";}
	  if (position.hasLongitude()) {System.out.print(spacer + "longitude: " + position.getLongitude()); spacer=", ";}
	  if (position.hasBearing()) {System.out.print(spacer + "bearing: " + position.getBearing()); spacer=", ";}
	  if (position.hasOdometer()) {System.out.print(spacer + "odometer: " + position.getOdometer()); spacer=", ";}
	  if (position.hasSpeed()) {System.out.print(spacer + "speed: " + position.getSpeed()); spacer=", ";}
	  System.out.println(spacer + "unknown position fields:" + position.getUnknownFields());
	  spacer = "";
	  }

        }

      //
      // Logic to print information in Alert entity can be implemented here
      //
      if (feedEntity.hasAlert()) {
        System.out.println("  Entity is Alert:");
      	Alert alert = feedEntity.getAlert();

      	System.out.println("  Alert parse/display is not yet implemented!");
      	break ;
        }

      //
      // Look for extensions in feed entity of any type
      //
      System.out.println("    Unknown entity fields: " + feedEntity.getUnknownFields());
      }
    }

  // Main function:  Reads a real-time feed information file and prints information in the FeedMessage from the file.
  public static void main(String[] args) throws Exception {
    if (args.length != 1) {
      System.err.println("Usage:  BusList REAL_TIME_FEED_FILE");
      System.exit(-1);
      }

    // Read the real-time feed file.
    FeedMessage feedMessage =
      FeedMessage.parseFrom(new FileInputStream(args[0]));

    Print(feedMessage);
    }
}
