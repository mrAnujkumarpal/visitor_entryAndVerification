package vms.vevs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vms.vevs.common.util.VmsUtils;
import vms.vevs.entity.common.Location;
import vms.vevs.repo.LocationRepository;
import vms.vevs.service.LocationService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LocationServiceImpl implements LocationService {
    @Autowired
    LocationRepository locationRepository;

    @Override
    public List<Location> allLocation() {
        return locationRepository.findAll();
    }

    @Override
    public Location newLocation(Location location,Long loggedInUserId) {

        location.setEnable(true);
        location.setCreatedBy(loggedInUserId);
        location.setCreatedOn(VmsUtils.currentTime());
        location.setModifiedOn(VmsUtils.currentTime());
        return locationRepository.save(location);
    }

    @Override
    public Location  locationById(Long id) {
        return locationRepository.getById(id);
    }

    @Override
    public Location updateLocation(Location location, Long loggedInUserId) {
        Location locFromDb=locationRepository.getById(location.getId());
        locFromDb.setModifiedBy(loggedInUserId);
        locFromDb.setModifiedOn(VmsUtils.currentTime());
        locFromDb.setName(location.getName());
        locFromDb.setEnable(location.isEnable());
        locFromDb.setLocationContactNo(location.getLocationContactNo());
        return locationRepository.save(locFromDb);
    }
}
