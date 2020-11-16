package com.sjsushil09.controllers;

import com.sjsushil09.model.*;
import com.sjsushil09.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/db-init")
@RestController
public class DbInitController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PassengerRepository passengerRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private ColorRepository colorRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CarRepository carRepository;

    @GetMapping("")
    public String initDB() {
        // this will delete the rows
        // it will not reset the sequence numbers
        passengerRepository.deleteAll();
        driverRepository.deleteAll();
        accountRepository.deleteAll();
        carRepository.deleteAll();
        colorRepository.deleteAll();
        roleRepository.deleteAll();

        Color white = Color.builder().name("white").build();
        Color black = Color.builder().name("black").build();
        Color yellow = Color.builder().name("yellow").build();
        colorRepository.save(white);
        colorRepository.save(black);
        colorRepository.save(yellow);

        Role driver = Role.builder().name("ROLE_DRIVER").description("Driver").build();
        Role passenger = Role.builder().name("ROLE_PASSENGER").description("Passenger").build();
        Role admin = Role.builder().name("ROLE_ADMIN").description("Admin").build();
        roleRepository.save(driver);
        roleRepository.save(passenger);
        roleRepository.save(admin);

        Driver luffy = Driver.builder()
                .name("Monkey D. Luffy")
                .account(Account.builder()
                        .userName("0000")
                        .passWord(passwordEncoder.encode("luffy"))
                        .role(driver)
                        .build())
                .car(Car.builder()
                        .brandModel("Large Ship")
                        .color(yellow)
                        .carType(CarType.XL)
                        .plateNumber("Thousand Sunny")
                        .build())
                .gender(Gender.MALE)
                .phoneNumber("0000")
                .build();
        Driver zoro = Driver.builder()
                .name("Roronoa Zoro")
                .account(Account.builder()
                        .userName("0001")
                        .passWord(passwordEncoder.encode("zoro"))
                        .role(driver)
                        .build())
                .car(Car.builder()
                        .brandModel("Caravel")
                        .color(white)
                        .carType(CarType.Sedan)
                        .plateNumber("Going Merry")
                        .build())
                .gender(Gender.MALE)
                .phoneNumber("0001")
                .build();
        driverRepository.save(luffy);
        driverRepository.save(zoro);

        Passenger usopp = Passenger.builder()
                .account(Account.builder()
                        .userName("0002")
                        .passWord(passwordEncoder.encode("usopp"))
                        .role(passenger)
                        .build())
                .gender(Gender.MALE)
                .phoneNumber("0002")
                .build();

        Passenger nami = Passenger.builder()
                .account(Account.builder()
                        .userName("0004")
                        .passWord(passwordEncoder.encode("nami"))
                        .role(passenger)
                        .build())
                .gender(Gender.FEMALE)
                .phoneNumber("0004")
                .build();
        passengerRepository.save(usopp);
        passengerRepository.save(nami);

        Account sanji = Account.builder()
                .userName("0003")
                .role(admin)
                .passWord(passwordEncoder.encode("sanji"))
                .build();
        accountRepository.save(sanji);
        return "success";
    }
}
