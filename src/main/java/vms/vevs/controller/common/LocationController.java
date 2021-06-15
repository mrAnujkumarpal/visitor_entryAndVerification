package vms.vevs.controller.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import vms.vevs.controller.validator.Validator;
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

    @PostMapping(value = "create", produces = MediaType.APPLICATION_JSON_VALUE)
    public   Location createLocation(@RequestBody Location location){

        List<String>  validateLocation=   new Validator().createLocation(location);
        if (validateLocation.size()>0){
            logger.info(validateLocation.toString());
        }
           return  locationService.newLocation(location);
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public   Location updateLocation(@RequestBody Location location){



        List<String>  validateLocation=   new Validator().updateLocation(location);
        if (validateLocation.size()>0){
            logger.info(validateLocation.toString());
        }
        return  locationService.newLocation(location);
    }



    @GetMapping("view/{id}")
    public Optional<Location> locationById(@PathVariable Long id){
        return  locationService.locationById(id);
    }



    @GetMapping("withoutLogin/all")
    public  List<Location> allLocations(){
        return locationService.allLocation();
    }
    @GetMapping("withoutLogin/view/{id}")
    public Optional<Location> viewLocationById(@PathVariable Long id){
        return  locationService.locationById(id);
    }

}
