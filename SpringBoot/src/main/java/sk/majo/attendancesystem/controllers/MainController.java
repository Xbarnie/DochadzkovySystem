package sk.majo.attendancesystem.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.WebAsyncTask;
import org.springframework.web.server.ResponseStatusException;
import sk.majo.attendancesystem.entities.*;
import sk.majo.attendancesystem.repositories.ArrivalRepository;
import sk.majo.attendancesystem.repositories.AttendanceRepository;
import sk.majo.attendancesystem.repositories.UserRepository;
import sk.majo.attendancesystem.services.CryptoCode;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping(path = "/api/main")
public class MainController {

    final private static Logger logger = LoggerFactory.getLogger(MainController.class);

    private final UserRepository userRepository;
    private final ArrivalRepository arrivalRepository;
    private final AttendanceRepository attendanceRepository;

    private Map<String, Object> arrivingInventory = new ConcurrentHashMap<>();
    private Map<String, Object> leaveInventory = new ConcurrentHashMap<>();
    // Lunch
    private Map<String, Object> lunchArrivalInventory = new ConcurrentHashMap<>();
    private Map<String, Object> lunchLeavingInventory = new ConcurrentHashMap<>();
    // Doctor
    private Map<String, Object> doctorArrivalInventory = new ConcurrentHashMap<>();
    private Map<String, Object> doctorLeavingInventory = new ConcurrentHashMap<>();
    // Business Trip
    private Map<String, Object> businessTripArrivalInventory = new ConcurrentHashMap<>();
    private Map<String, Object> businessTripLeavingInventory = new ConcurrentHashMap<>();
    // Private Trip
    private Map<String, Object> privateTripArrivalInventory = new ConcurrentHashMap<>();
    private Map<String, Object> privateTripLeavingInventory = new ConcurrentHashMap<>();
    // Vacation
    private Map<String, Object> vacationArrivalInventory = new ConcurrentHashMap<>();
    private Map<String, Object> vacationLeavingInventory = new ConcurrentHashMap<>();

    private MainController(UserRepository userRepository, ArrivalRepository arrivalRepository, AttendanceRepository attendanceRepository) {
        this.userRepository = userRepository;
        this.arrivalRepository = arrivalRepository;
        this.attendanceRepository = attendanceRepository;
    }

    @RequestMapping(value = "/arrival/{room}", method = RequestMethod.GET)
    public WebAsyncTask<Object> requestArrival(@PathVariable String room) {
        return request(arrivingInventory, room, (long) (5*6000));
    }

    @RequestMapping(value = "/leaving/{room}", method = RequestMethod.GET)
    public WebAsyncTask<Object> requestLeaving(@PathVariable String room) {
        return request(leaveInventory, room, (long) (5*6000));
    }

    @RequestMapping(value = "/doctor-arrival/{room}", method = RequestMethod.GET)
    public WebAsyncTask<Object> requestDoctorArrival(@PathVariable String room) {
        return request(doctorArrivalInventory, room, (long) (5*6000));
    }

    @RequestMapping(value = "/doctor-leaving/{room}", method = RequestMethod.GET)
    public WebAsyncTask<Object> requestDoctorLeaving(@PathVariable String room) {
        return request(doctorLeavingInventory, room, (long) (5*6000));
    }

    @RequestMapping(value = "/lunch-arrival/{room}", method = RequestMethod.GET)
    public WebAsyncTask<Object> requestLunchArrival(@PathVariable String room) {
        return request(lunchArrivalInventory, room, (long) (5*6000));
    }

    @RequestMapping(value = "/lunch-leaving/{room}", method = RequestMethod.GET)
    public WebAsyncTask<Object> requestLunchLeaving(@PathVariable String room) {
        return request(lunchLeavingInventory, room, (long) (5*6000));
    }

    @RequestMapping(value = "/vacation-arrival/{room}", method = RequestMethod.GET)
    public WebAsyncTask<Object> requestVacationArrival(@PathVariable String room) {
        return request(vacationArrivalInventory, room, (long) (5*6000));
    }

    @RequestMapping(value = "/vacation-leaving/{room}", method = RequestMethod.GET)
    public WebAsyncTask<Object> requestVacationLeaving(@PathVariable String room) {
        return request(vacationLeavingInventory, room, (long) (5*6000));
    }

    @RequestMapping(value = "/businesstrip-arrival/{room}", method = RequestMethod.GET)
    public WebAsyncTask<Object> requestBusinesstripArrival(@PathVariable String room) {
        return request(businessTripArrivalInventory, room, (long) (5*6000));
    }

    @RequestMapping(value = "/businesstrip-leaving/{room}", method = RequestMethod.GET)
    public WebAsyncTask<Object> requestBusinesstripLeaving(@PathVariable String room) {
        return request(businessTripLeavingInventory, room, (long) (5*6000));
    }

    @RequestMapping(value = "/privatetrip-arrival/{room}", method = RequestMethod.GET)
    public WebAsyncTask<Object> requestPrivatetripArrival(@PathVariable String room) {
        return request(privateTripArrivalInventory, room, (long) (5*6000));
    }

    @RequestMapping(value = "/privatetrip-leaving/{room}", method = RequestMethod.GET)
    public WebAsyncTask<Object> requestPrivatetripLeaving(@PathVariable String room) {
        return request(privateTripLeavingInventory, room, (long) (5*6000));
    }

    @RequestMapping(value = "/call", method = RequestMethod.POST)
    public Object call(@RequestBody SignUser signUser) {
        logger.info("TOKEN::::"+signUser.getToken());
        if(!arrivingInventory.isEmpty() && arrivingInventory.containsKey(signUser.getRoom())) {
            logger.info("ARRIVING !!!!");
            return provideDelayData(arrivingInventory, signUser, "arrival");
        }
        if(!leaveInventory.isEmpty() && leaveInventory.containsKey(signUser.getRoom())) {
            logger.info("LEAVING !!!!!");
            return provideDelayData(leaveInventory, signUser, "leaving");
        }
        if(!lunchArrivalInventory.isEmpty() && lunchArrivalInventory.containsKey(signUser.getRoom())) {
            logger.info("lunchArrivalInventory !!!!!");
            return provideDelayData(lunchArrivalInventory, signUser, "lunchArrival");
        }
        if(!lunchLeavingInventory.isEmpty() && lunchLeavingInventory.containsKey(signUser.getRoom())) {
            logger.info("lunchLeavingInventory !!!!!");
            return provideDelayData(lunchLeavingInventory, signUser, "lunchLeaving");
        }
        if(!doctorArrivalInventory.isEmpty() && doctorArrivalInventory.containsKey(signUser.getRoom())) {
            logger.info("doctorArrivalInventory !!!!!");
            return provideDelayData(doctorArrivalInventory, signUser, "doctorArrival");
        }
        if(!doctorLeavingInventory.isEmpty() && doctorLeavingInventory.containsKey(signUser.getRoom())) {
            logger.info("doctorLeavingInventory !!!!!");
            return provideDelayData(doctorLeavingInventory, signUser, "doctorLeaving");
        }
        if(!businessTripArrivalInventory.isEmpty() && businessTripArrivalInventory.containsKey(signUser.getRoom())) {
            logger.info("businessTripArrivalInventory !!!!!");
            return provideDelayData(businessTripArrivalInventory, signUser, "businessTripArrival");
        }
        if(!businessTripLeavingInventory.isEmpty() && businessTripLeavingInventory.containsKey(signUser.getRoom())) {
            logger.info("businessTripLeavingInventory !!!!!");
            return provideDelayData(businessTripLeavingInventory, signUser, "businessTripLeaving");
        }
        if(!privateTripArrivalInventory.isEmpty() && privateTripArrivalInventory.containsKey(signUser.getRoom())) {
            logger.info("privateTripArrivalInventory !!!!!");
            return provideDelayData(privateTripArrivalInventory, signUser, "privateTripArrival");
        }
        if(!privateTripLeavingInventory.isEmpty() && privateTripLeavingInventory.containsKey(signUser.getRoom())) {
            logger.info("privateTripLeavingInventory !!!!!");
            return provideDelayData(privateTripLeavingInventory, signUser, "privateTripLeaving");
        }
        if(!vacationArrivalInventory.isEmpty() && vacationArrivalInventory.containsKey(signUser.getRoom())) {
            logger.info("vacationArrivalInventory !!!!!");
            return provideDelayData(vacationArrivalInventory, signUser, "vacationArrival");
        }
        if(!vacationLeavingInventory.isEmpty() && vacationLeavingInventory.containsKey(signUser.getRoom())) {
            logger.info("vacationLeavingInventory !!!!!");
            return provideDelayData(vacationLeavingInventory, signUser, "vacationLeaving");
        }
        return null;
    }

    @RequestMapping(value = "/remove-action", method = RequestMethod.POST)
    public Object removeAction(@RequestBody SignUser signUser) {
        if(!arrivingInventory.isEmpty() && arrivingInventory.containsKey(signUser.getRoom())) {
            logger.info("CANCEL ARRIVING !!!!");
            return provideDelayData(arrivingInventory, signUser, "CANCEL");
        }
        if(!leaveInventory.isEmpty() && leaveInventory.containsKey(signUser.getRoom())) {
            logger.info("CANCEL LEAVING !!!!!");
            return provideDelayData(leaveInventory, signUser, "CANCEL");
        }
        if(!lunchArrivalInventory.isEmpty() && lunchArrivalInventory.containsKey(signUser.getRoom())) {
            logger.info("CANCEL lunchArrivalInventory !!!!!");
            return provideDelayData(lunchArrivalInventory, signUser, "CANCEL");
        }
        if(!lunchLeavingInventory.isEmpty() && lunchLeavingInventory.containsKey(signUser.getRoom())) {
            logger.info("CANCEL lunchLeavingInventory !!!!!");
            return provideDelayData(lunchLeavingInventory, signUser, "CANCEL");
        }
        if(!doctorArrivalInventory.isEmpty() && doctorArrivalInventory.containsKey(signUser.getRoom())) {
            logger.info("CANCEL doctorArrivalInventory !!!!!");
            return provideDelayData(doctorArrivalInventory, signUser, "CANCEL");
        }
        if(!doctorLeavingInventory.isEmpty() && doctorLeavingInventory.containsKey(signUser.getRoom())) {
            logger.info("CANCEL doctorLeavingInventory !!!!!");
            return provideDelayData(doctorLeavingInventory, signUser, "CANCEL");
        }
        if(!businessTripArrivalInventory.isEmpty() && businessTripArrivalInventory.containsKey(signUser.getRoom())) {
            logger.info("CANCEL businessTripArrivalInventory !!!!!");
            return provideDelayData(businessTripArrivalInventory, signUser, "CANCEL");
        }
        if(!businessTripLeavingInventory.isEmpty() && businessTripLeavingInventory.containsKey(signUser.getRoom())) {
            logger.info("CANCEL businessTripLeavingInventory !!!!!");
            return provideDelayData(businessTripLeavingInventory, signUser, "CANCEL");
        }
        if(!privateTripArrivalInventory.isEmpty() && privateTripArrivalInventory.containsKey(signUser.getRoom())) {
            logger.info("CANCEL privateTripArrivalInventory !!!!!");
            return provideDelayData(privateTripArrivalInventory, signUser, "CANCEL");
        }
        if(!privateTripLeavingInventory.isEmpty() && privateTripLeavingInventory.containsKey(signUser.getRoom())) {
            logger.info("CANCEL privateTripLeavingInventory !!!!!");
            return provideDelayData(privateTripLeavingInventory, signUser, "CANCEL");
        }
        if(!vacationArrivalInventory.isEmpty() && vacationArrivalInventory.containsKey(signUser.getRoom())) {
            logger.info("CANCEL vacationArrivalInventory !!!!!");
            return provideDelayData(vacationArrivalInventory, signUser, "CANCEL");
        }
        if(!vacationLeavingInventory.isEmpty() && vacationLeavingInventory.containsKey(signUser.getRoom())) {
            logger.info("CANCEL vacationLeavingInventory !!!!!");
            return provideDelayData(vacationLeavingInventory, signUser, "CANCEL");
        }
        return null;
    }

    private WebAsyncTask<Object> request(Map<String, Object> inventory, String room, Long timeout) {
        DataCollectCallable dataCollectCallable = new DataCollectCallable();
        dataCollectCallable.setData("MojObject");
        inventory.put(room, dataCollectCallable);
        //inventory.put("room", room);
        WebAsyncTask<Object> webAsyncTask = new WebAsyncTask<Object>(timeout, dataCollectCallable);

        webAsyncTask.onTimeout(() -> {
            logger.info("TIME*OUT");
            dataCollectCallable.unfreeze();
            inventory.remove(room);
            throw new ResponseStatusException(HttpStatus.REQUEST_TIMEOUT);
        });
        return webAsyncTask;
    }

    public Object provideDelayData(Map<String, Object> inventory, SignUser signUser, String action) {
        logger.info("Current Inventory " + (String)inventory.get("room"));

        if(action.equals("CANCEL")) {
            DataCollectCallable dataCollectCallable = (DataCollectCallable)inventory.remove(signUser.getRoom());
            dataCollectCallable.setData(signUser);
            dataCollectCallable.unfreeze();
            return Collections.singletonMap("msg", "Canceled");
        }

        Date date = new Date();

        User findUser = null;
        try {
            /*CryptoCode cryptoCode = new CryptoCode();
            String decrypted = cryptoCode.decrypt(signUser.getToken());
            logger.info("Decrypted String:" + decrypted);
            signUser.setEmployeeName(cryptoCode.decrypt(signUser.getToken()));

            // Find user by uuid

            findUser = userRepository.findUserByUuid(UUID.fromString(decrypted));*/

            findUser = userRepository.findById(Integer.parseInt(signUser.getToken())).orElse(null);

            if(action.equals("arrival")) {
                if(findUser != null) {
                    // Get today date
                    LocalDate now = LocalDate.now();
                    String currentDate = now.getYear() + "-" + now.getMonthValue() + "-" + now.getDayOfMonth();
                    // Check if user exists in attendence
                    Attendance existsAttendance = attendanceRepository.findByDateAndUserId(currentDate, findUser.getId());
                    // Add to database if user doesn't exists
                    if(existsAttendance == null) {
                        Attendance attendance = new Attendance();
                        attendance.setDateAt(date);
                        attendance.setUser(findUser);
                        attendance.setArrivalAt(new Date());
                        // Add leaving + 8H
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(new Date());
                        cal.add(Calendar.HOUR_OF_DAY, 8);      // adds one hour
                        attendance.setLeaveAt( cal.getTime());
                        attendanceRepository.save(attendance);
                        signUser.setType("Príchod");
                    }
                }
            }
            if(action.equals("leaving")) {
                // Get today date
                LocalDateTime now = LocalDateTime.now();
                String currentDate = now.getYear() + "-" + now.getMonthValue() + "-" + now.getDayOfMonth();
                // Check if user exists in attendence
                Attendance attendance = attendanceRepository.findByDateAndUserId(currentDate, findUser.getId());
                // Edit leave date if arrived already
                if(attendance != null && attendance.getArrivalAt() != null) {
                    attendance.setLeaveAt(new Date());
                    attendanceRepository.save(attendance);
                    signUser.setType("Odchod");
                } else {
                    signUser.setErrorMessage("Zamestnanec nie je prihlásený v práci.");
                }
            }
            // Lunch
            if(action.equals("lunchLeaving")) {
                // Get today date
                LocalDateTime now = LocalDateTime.now();
                String currentDate = now.getYear() + "-" + now.getMonthValue() + "-" + now.getDayOfMonth();
                // Check if user exists in attendence
                Attendance attendance  = attendanceRepository.findByDateAndUserId(currentDate, findUser.getId());
                // Set lunch time if user arrived already
                if(attendance != null && attendance.getLunchFrom() == null) {
                    attendance.setLunchFrom(now.format(DateTimeFormatter.ofPattern("HH:mm")));
                    attendanceRepository.save(attendance);
                    signUser.setType("Obed odchod");
                } else {
                    signUser.setErrorMessage("Zamestnanec nie je prihlásený v práci.");
                }
            }
            if(action.equals("lunchArrival")) {
                // Get today date
                LocalDateTime now = LocalDateTime.now();
                String currentDate = now.getYear() + "-" + now.getMonthValue() + "-" + now.getDayOfMonth();
                // Check if user exists in attendence
                Attendance attendance = attendanceRepository.findByDateAndUserId(currentDate, findUser.getId());
                // Set lunch time if user arrived already
                if(attendance != null && attendance.getLunchFrom() != null) {
                    attendance.setLunchTo(now.format(DateTimeFormatter.ofPattern("HH:mm")));
                    attendanceRepository.save(attendance);
                    signUser.setType("Obed príchod");
                } else {
                    signUser.setErrorMessage("Zamestnanec nie je prihlásený v práci, alebo nie je prihlásený na obed.");
                }
            }
            // Doctor
            if(action.equals("doctorLeaving")) {
                // Get today date
                LocalDateTime now = LocalDateTime.now();
                String currentDate = now.getYear() + "-" + now.getMonthValue() + "-" + now.getDayOfMonth();
                // Check if user exists in attendence
                Attendance attendance  = attendanceRepository.findByDateAndUserId(currentDate, findUser.getId());
                // Set lunch time if user arrived already
                if(attendance != null && attendance.getDoctorFrom() == null) {
                    attendance.setDoctorFrom(now.format(DateTimeFormatter.ofPattern("HH:mm")));
                    attendanceRepository.save(attendance);
                    signUser.setType("Lekár odchod");
                } else {
                    signUser.setErrorMessage("Zamestnanec nie je prihlásený v práci.");
                }
            }
            if(action.equals("doctorArrival")) {
                // Get today date
                LocalDateTime now = LocalDateTime.now();
                String currentDate = now.getYear() + "-" + now.getMonthValue() + "-" + now.getDayOfMonth();
                // Check if user exists in attendence
                Attendance attendance = attendanceRepository.findByDateAndUserId(currentDate, findUser.getId());
                // Set lunch time if user arrived already
                if(attendance != null && attendance.getDoctorFrom() != null) {
                    attendance.setDoctorTo(now.format(DateTimeFormatter.ofPattern("HH:mm")));
                    attendanceRepository.save(attendance);
                    signUser.setType("Lekár príchod");
                } else {
                    signUser.setErrorMessage("Zamestnanec nie je prihlásený v práci, alebo nie je prihlásený k doktorovi.");
                }
            }
            // Business Trip
            if(action.equals("businessTripLeaving")) {
                // Get today date
                LocalDateTime now = LocalDateTime.now();
                String currentDate = now.getYear() + "-" + now.getMonthValue() + "-" + now.getDayOfMonth();
                // Check if user exists in attendence
                Attendance attendance  = attendanceRepository.findByDateAndUserId(currentDate, findUser.getId());
                // Set lunch time if user arrived already
                if(attendance != null && attendance.getBusinessFrom() == null) {
                    attendance.setBusinessFrom(now.format(DateTimeFormatter.ofPattern("HH:mm")));
                    attendanceRepository.save(attendance);
                    signUser.setType("Služobná cesta odchod");
                } else {
                    signUser.setErrorMessage("Zamestnanec nie je prihlásený v práci.");
                }
            }
            if(action.equals("businessTripArrival")) {
                // Get today date
                LocalDateTime now = LocalDateTime.now();
                String currentDate = now.getYear() + "-" + now.getMonthValue() + "-" + now.getDayOfMonth();
                // Check if user exists in attendence
                Attendance attendance = attendanceRepository.findByDateAndUserId(currentDate, findUser.getId());
                // Set lunch time if user arrived already
                if(attendance != null && attendance.getBusinessFrom() != null) {
                    attendance.setBusinessTo(now.format(DateTimeFormatter.ofPattern("HH:mm")));
                    attendanceRepository.save(attendance);
                    signUser.setType("Služobná cesta príchod");
                } else {
                    signUser.setErrorMessage("Zamestnanec nie je prihlásený v práci, alebo nie je prihlásený na služobnú cestu.");
                }
            }
            // Private route
            if(action.equals("privateTripLeaving")) {
                // Get today date
                LocalDateTime now = LocalDateTime.now();
                String currentDate = now.getYear() + "-" + now.getMonthValue() + "-" + now.getDayOfMonth();
                // Check if user exists in attendence
                Attendance attendance  = attendanceRepository.findByDateAndUserId(currentDate, findUser.getId());
                // Set lunch time if user arrived already
                if(attendance != null && attendance.getPrivateFrom() == null) {
                    attendance.setPrivateFrom(now.format(DateTimeFormatter.ofPattern("HH:mm")));
                    attendanceRepository.save(attendance);
                    signUser.setType("Súkromná cesta odchod");
                } else {
                    signUser.setErrorMessage("Zamestnanec nie je prihlásený v práci.");
                }
            }
            if(action.equals("privateTripArrival")) {
                // Get today date
                LocalDateTime now = LocalDateTime.now();
                String currentDate = now.getYear() + "-" + now.getMonthValue() + "-" + now.getDayOfMonth();
                // Check if user exists in attendence
                Attendance attendance = attendanceRepository.findByDateAndUserId(currentDate, findUser.getId());
                // Set lunch time if user arrived already
                if (attendance != null && attendance.getPrivateFrom() != null) {
                    attendance.setPrivateTo(now.format(DateTimeFormatter.ofPattern("HH:mm")));
                    attendanceRepository.save(attendance);
                    signUser.setType("Súkromná cesta príchod");
                } else {
                    signUser.setErrorMessage("Zamestnanec nie je prihlásený v práci, alebo nie je prihlásený na súkromnú cestu.");
                }
            }
            // Vacation
            if(action.equals("vacationLeaving")) {
                // Get today date
                LocalDateTime now = LocalDateTime.now();
                String currentDate = now.getYear() + "-" + now.getMonthValue() + "-" + now.getDayOfMonth();
                // Check if user exists in attendence
                Attendance attendance  = attendanceRepository.findByDateAndUserId(currentDate, findUser.getId());
                // Set lunch time if user arrived already
                if(attendance != null && attendance.getVacationFrom() == null) {
                    attendance.setVacationFrom(now.format(DateTimeFormatter.ofPattern("HH:mm")));
                    attendanceRepository.save(attendance);
                    signUser.setType("Dovolenka odchod");
                } else {
                    signUser.setErrorMessage("Zamestnanec nie je prihlásený v práci.");
                }
            }
            if(action.equals("vacationArrival")) {
                // Get today date
                LocalDateTime now = LocalDateTime.now();
                String currentDate = now.getYear() + "-" + now.getMonthValue() + "-" + now.getDayOfMonth();
                // Check if user exists in attendence
                Attendance attendance = attendanceRepository.findByDateAndUserId(currentDate, findUser.getId());
                // Set lunch time if user arrived already
                if (attendance != null && attendance.getVacationFrom() != null) {
                    attendance.setVacationTo(now.format(DateTimeFormatter.ofPattern("HH:mm")));
                    attendanceRepository.save(attendance);
                    signUser.setType("Dovolenka príchod");
                }  else {
                    signUser.setErrorMessage("Zamestnanec nie je prihlásený v práci, alebo nie je prihlásený na dovolenku.");
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        logger.info((String) inventory.get("room"));
        logger.info(signUser.getRoom());
        if(signUser.getRoom().equals(inventory.get(signUser.getRoom()))){
            logger.info("Room is same");
        } else {
            logger.warn("Room is not same");
        }

        signUser.setTime(new Date());
        if(findUser != null) {
            signUser.setEmployeeName(findUser.getFirstName() + " " + findUser.getLastName());
        }
        DataCollectCallable dataCollectCallable = (DataCollectCallable)inventory.remove(signUser.getRoom());
        if(dataCollectCallable != null) {
            dataCollectCallable.setData(signUser);
            dataCollectCallable.unfreeze();
            return Collections.singletonMap("msg", "Send data successfully");
        } else {
            logger.info("Coming to work");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Token not found");
        }
    }
}
