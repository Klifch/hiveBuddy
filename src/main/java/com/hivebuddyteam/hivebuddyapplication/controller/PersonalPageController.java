package com.hivebuddyteam.hivebuddyapplication.controller;

import com.hivebuddyteam.hivebuddyapplication.controller.apiDTO.DeviceDto;
import com.hivebuddyteam.hivebuddyapplication.domain.Device;
import com.hivebuddyteam.hivebuddyapplication.domain.User;
import com.hivebuddyteam.hivebuddyapplication.service.DeviceService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PersonalPageController {

    DeviceService deviceService;

    Logger logger = LoggerFactory.getLogger(PersonalPageController.class);

    @Autowired
    public PersonalPageController(DeviceService deviceService) {
        logger.info("DeviceService injected in PersonalPageController ...");
        this.deviceService = deviceService;
    }

    @GetMapping("/personalHomeOld")
    public String showPersonalPage(Model model, HttpSession httpSession) {

        User user = (User) httpSession.getAttribute("user");

        if (user == null) {
            throw new RuntimeException("User is empty, no list of devices!");
            // custom error to add
        }

        List<Device> devices = deviceService.findAllByUser(user);

        model.addAttribute("devices", devices);

        return "personal_page";
    }

    @GetMapping("/showHiveDetails")
    public String showHiveDetails(
            @RequestParam("deviceId") Integer id,
            Model model,
            HttpSession httpSession
    ) {
        logger.info("Received get request /showHiveDetails with id --> {}", id);
        Device device = deviceService.findById(id);

        if (device == null) {
            logger.warn("No Device with ID -> {} found!", id);
            throw new RuntimeException("No Device with ID: " + id + " found!");
            // custom error handling // redirect to oups page!
        }

        logger.info("Device --> {} added to the model and session", device);

        model.addAttribute("theDevice", device);
        httpSession.setAttribute("device", device);

        logger.info("Returning view --> device_hive_data");

        return "device_data";
    }

    @GetMapping("/personalHome")
    public String showDashBoard(Model model, HttpSession httpSession) {

        logger.info("Received get request /personalHome");

        User user = (User) httpSession.getAttribute("user");

        if (user == null) {
            logger.warn("User is empty, no list of devices!");
            throw new RuntimeException("User is empty, no list of devices!");
            // custom error to add
        }

        List<Device> devices = deviceService.findAllByUser(user);

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

        DeviceDto deviceDto = new DeviceDto();

        model.addAttribute("deviceDto", deviceDto);

        logger.info("Added empty deviceDTO to the model ...");
        logger.info("Returning view --> register_device_form");

        return "register_device_form";
    }

    @PostMapping("/processNewDeviceForm")
    public String processNewDevice(
            Model model,
            @ModelAttribute("deviceDto") DeviceDto deviceDto,
            HttpSession httpSession
    ) {

        logger.info("Received post request /processNewDeviceForm");

        String serial = deviceDto.getSerialNumber();

        Device existingDevice = deviceService.findBySerial(serial);

        if (existingDevice != null) {
            logger.warn("Device with this serial already exists! serial --> {}", serial);
            logger.warn("Returning view with new DeviceDto");

            model.addAttribute("deviceDto", new DeviceDto());
            model.addAttribute("deviceExistError", "Device with this serial already exists!");

            return "register_device_form";
        }

        Device device = new Device();
        device.setDeviceName(deviceDto.getDeviceName());
        device.setSerialNumber(deviceDto.getSerialNumber());
        device.setUser((User) httpSession.getAttribute("user"));

        deviceService.save(device);

        return "redirect:/personalHome";
    }



}
