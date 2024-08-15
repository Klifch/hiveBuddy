package com.hivebuddyteam.hivebuddyapplication.controller;

import com.hivebuddyteam.hivebuddyapplication.dto.AddNewDeviceDto;
import com.hivebuddyteam.hivebuddyapplication.dto.DeviceDto;
import com.hivebuddyteam.hivebuddyapplication.domain.Device;
import com.hivebuddyteam.hivebuddyapplication.domain.User;
import com.hivebuddyteam.hivebuddyapplication.service.DeviceService;
import com.hivebuddyteam.hivebuddyapplication.service.UnregisteredPoolService;
import com.hivebuddyteam.hivebuddyapplication.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PersonalPageController {

    private final DeviceService deviceService;
    private final UserService userService;
    private final UnregisteredPoolService unregisteredPoolService;

    Logger logger = LoggerFactory.getLogger(PersonalPageController.class);

    @Autowired
    public PersonalPageController(DeviceService deviceService, UserService userService, UnregisteredPoolService unregisteredPoolService) {
        this.unregisteredPoolService = unregisteredPoolService;
        logger.info("DeviceService injected in PersonalPageController ...");
        this.userService = userService;
        this.deviceService = deviceService;
    }

//    @GetMapping("/personalHomeOld")
//    public String showPersonalPage(Model model, HttpSession httpSession) {
//
//        User user = (User) httpSession.getAttribute("user");
//
//        if (user == null) {
//            throw new RuntimeException("User is empty, no list of devices!");
//            // custom error to add
//        }
//
//        List<Device> devices = deviceService.findAllByUser(user);
//
//        model.addAttribute("devices", devices);
//
//        return "personal_page";
//    }

    @GetMapping("/hiveData")
    public String showHiveDetails(
            @AuthenticationPrincipal User user,
            @RequestParam("deviceSerial") String deviceSerial,
            Model model,
            HttpSession httpSession
    ) {
        logger.info("Received get request /showHiveDetails with serial --> {}", deviceSerial);
        Device device = deviceService.findBySerial(deviceSerial);

        if (device == null) {
            logger.warn("No Device with Serial -> {} found!", deviceSerial);
            throw new RuntimeException("No Device with Serial: " + deviceSerial + " found!");
            // TODO: custom error handling // redirect to oups page!
        }

        logger.info("Device --> {} added to the model and session", device);

        model.addAttribute("theDevice", device);
        httpSession.setAttribute("device", device); // Wy tf do I need it?

        logger.info("Returning view --> device_hive_data");

        return "device_data2";
    }

    @GetMapping("/hiveDataTest")
    public String showHiveData(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam("deviceId") Integer id,
            Model model
    ) {
        logger.info("Received get request /showHiveData");

        // some code here

        return "hive_data";
    }

    // TODO: Why the fuck it is called personal home? Change it!
    @GetMapping("/personalHome")
    public String showDashBoard(
            @AuthenticationPrincipal UserDetails user,
            Model model,
            HttpSession httpSession
    ) {
        logger.info("Received get request /personalHome");

        List<Device> devices = deviceService.findAllByUser(userService.finByUsername(user.getUsername()));
        model.addAttribute("devices", devices);

        logger.info("List of devices: {} added to the model", devices);
        logger.info("Returning view --> personal");

        return "personal";
    }

    @GetMapping("/registerNewDevice")
    public String registerNewDevice(
            Model model,
            HttpSession httpSession
    ) {

        logger.info("Received get request /registerNewDevice");

        AddNewDeviceDto deviceDto = new AddNewDeviceDto();

        model.addAttribute("deviceDto", deviceDto);

        logger.info("Added empty deviceDTO to the model ...");
        logger.info("Returning view --> register_device_form");

        return "register_device_form";
    }

    @PostMapping("/processNewDeviceForm")
    public String processNewDevice(
            @AuthenticationPrincipal UserDetails user,
            Model model,
            @ModelAttribute("deviceDto") AddNewDeviceDto deviceDto,
            HttpSession httpSession
    ) {

        logger.info("Received post request /processNewDeviceForm");

        if (deviceDto == null) {
            return "register_device_form";
        }

        String serial = deviceDto.getSerialNumber();

        if (!unregisteredPoolService.checkIfExists(serial)) {
            logger.warn("Invalid device, can't find in pool of manufactured devices");

            model.addAttribute("deviceDto", new DeviceDto());
            model.addAttribute("wrongCredentials", "Device with this serial could not be registered!");

            return "register_device_form";
        }

        Device existingDevice = deviceService.findBySerial(serial);

        if (existingDevice != null) {
            logger.warn("Device with this serial already exists! serial --> {}", serial);
            logger.warn("Returning view with new DeviceDto");

            model.addAttribute("deviceDto", new DeviceDto());
            model.addAttribute("deviceExistError", "Device with this serial already exists!");

            return "register_device_form";
        }

        if (!unregisteredPoolService.checkIfSecurityCodeValid(deviceDto.getSecurityCode(), serial)) {
            logger.warn("Wrong combination of security code and serial!");
            logger.warn("Returning view with new DeviceDto");

            model.addAttribute("deviceDto", new DeviceDto());
            model.addAttribute("wrongCredentials", "Wrong combination of security code and serial!");

            return "register_device_form";
        }

        Device device = new Device();
        device.setDeviceName(deviceDto.getDeviceName());
        device.setSerialNumber(deviceDto.getSerialNumber());
        device.setUser(userService.finByUsername(user.getUsername()));
        device.setActive(false);
        device.setSecurityCode(deviceDto.getSecurityCode());

        deviceService.save(device);

        return "redirect:/personalHome";
    }



}
