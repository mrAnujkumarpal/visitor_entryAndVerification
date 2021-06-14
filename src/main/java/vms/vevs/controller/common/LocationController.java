package vms.vevs.controller.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import vms.vevs.entity.common.Location;
import vms.vevs.service.LocationService;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/location/")
public class LocationController {

    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);

    @Autowired
    LocationService locationService;

    @GetMapping("all")
    public  List<Location> allLocation(){
        logger.info("Get all locations");

          return locationService.allLocation();
    }

    @RequestMapping(value = "create",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public   Location createLocation(@RequestBody Location location){

           return  locationService.newLocation(location);
    }

    @GetMapping("view/{id}")
    public Optional<Location> locationById(@PathVariable Long id){
        return  locationService.locationById(id);
    }


}
