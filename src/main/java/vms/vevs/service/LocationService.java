package vms.vevs.service;

import vms.vevs.entity.common.Location;

import java.util.List;
import java.util.Optional;

public interface LocationService {

    List<Location> allLocation();

    Location newLocation(Location location,Long loggedInUserId);

     Location  locationById(Long id);

    Location updateLocation(Location location, Long loggedInUserId);
}
