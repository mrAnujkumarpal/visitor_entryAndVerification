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
    public Location newLocation(Location location) {

        location.setEnable(true);
        location.setCreatedOn(VmsUtils.currentTime());
        return locationRepository.save(location);
    }

    @Override
    public Location  locationById(Long id) {
        return locationRepository.getById(id);
    }
}
