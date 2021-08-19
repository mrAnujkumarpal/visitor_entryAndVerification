package com.vevs.service;

import com.vevs.entity.common.Location;

import java.util.List;

public interface LocationService {

    List<Location> allLocation();

    Location newLocation(Location location,Long loggedInUserId);

     Location  locationById(Long id);

    Location updateLocation(Location location, Long loggedInUserId);
}
