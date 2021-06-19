package vms.vevs.controller.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import vms.vevs.controller.validator.Validator;
import vms.vevs.entity.common.Location;
import vms.vevs.entity.virtualObject.HttpResponse;
import vms.vevs.i18.MessageByLocaleService;
import vms.vevs.service.LocationService;

import java.util.List;


@RestController
@RequestMapping("/location/")
public class LocationController {

    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);

    @Autowired
    LocationService locationService;

    @Autowired
    MessageByLocaleService messageSource;

    @GetMapping("all")
    public HttpResponse<?> allLocation() {
        logger.info("Get all locations");
        HttpResponse<List<Location>> response = new HttpResponse<>();
        response.setResponseObject(locationService.allLocation());
        return response;
    }

    @PostMapping(value = "create", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResponse<?> createLocation(@RequestBody Location location) {
        HttpResponse<Location> response = new HttpResponse<>();
        List<String> validateLocation = new Validator().createLocation(location);
        if (!validateLocation.isEmpty()) {
            return new HttpResponse().errorResponse(validateLocation);
        }
        response.setResponseObject(locationService.newLocation(location));
        return response;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResponse<?> updateLocation(@RequestBody Location location) {
        HttpResponse<Location> response = new HttpResponse<>();

        List<String> validateMsgList = new Validator().updateLocation(location);
        if (!validateMsgList.isEmpty()) {
            return new HttpResponse().errorResponse(validateMsgList);
        }
        response.setResponseObject(locationService.newLocation(location));
        return response;
    }


    @GetMapping("view/{id}")
    public HttpResponse<?> locationById(@PathVariable Long id) {
        HttpResponse<Location> response = new HttpResponse<>();
        response.setResponseObject(locationService.locationById(id));
        return response;
    }


    @GetMapping("public/all")
    public HttpResponse<?> allLocations() {
        HttpResponse<List<Location>> response = new HttpResponse<>();
        response.setResponseObject(locationService.allLocation());
        return response;
    }

    @GetMapping("public/view/{id}")
    public HttpResponse<?> viewLocation(@PathVariable Long id) {
        HttpResponse<Location> response = new HttpResponse<>();
        response.setResponseObject(locationService.locationById(id));
        return response;
    }
  /*  //internationalization
    @GetMapping(path = "/hello-world-internationalized")
    public String helloWorldInternationalized() {
        return messageSource.getMessage("good.morning.message");
    }*/
    @GetMapping(path = "/hello-world")
    public String helloWorld() {
        logger.info("In side hello world");
        return messageSource.getMessage("good.morning", new Object[] {"Anuj", "Pal"});
    }
}
