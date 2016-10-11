package com.pint;

import com.fasterxml.classmate.TypeResolver;
import com.pint.BusinessLogic.Security.UserRole;
import com.pint.BusinessLogic.Services.BloodDriveService;
import com.pint.BusinessLogic.Services.HospitalService;
import com.pint.BusinessLogic.Services.NotificationService;
import com.pint.BusinessLogic.Services.UserService;
import com.pint.BusinessLogic.Utilities.Utils;
import com.pint.Data.Models.*;
import org.joda.time.DateTime;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.filter.CharacterEncodingFilter;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.Filter;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static springfox.documentation.schema.AlternateTypeRules.newRule;

@EnableAutoConfiguration
@Configuration
@ComponentScan
@EnableSwagger2
public class StatelessAuthentication {

    public static void main(String[] args) {
        SpringApplication.run(StatelessAuthentication.class, args);
    }

    @Autowired
    private TypeResolver typeResolver;

    @Bean
    public Docket petApi() {
        return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any()).paths(PathSelectors.any()).build().pathMapping("/")
                .directModelSubstitute(LocalDate.class, String.class).genericModelSubstitutes(ResponseEntity.class)
                .alternateTypeRules(newRule(typeResolver.resolve(DeferredResult.class, typeResolver.resolve(ResponseEntity.class, WildcardType.class)), typeResolver.resolve(WildcardType.class)))
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET, newArrayList(new ResponseMessageBuilder().code(500).message("500 message").responseModel(new ModelRef("Error")).build()));
    }

//    private SecurityContext securityContext() {
//        return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.regex("/anyPath.*")).build();
//    }

    @Bean
    public InitializingBean insertDefaultUsers() {
        return new InitializingBean() {

            @Autowired
            HospitalService hospitalService;

            @Autowired
            UserService userService;

            @Autowired
            BloodDriveService bloodDriveService;

            @Autowired
            NotificationService notificationService;

            @Override
            public void afterPropertiesSet() throws Exception {
                if (userService.getUserByEmail("bpate005@fiu.edu") != null) {
                    return;
                }

                // Hospitals
                Hospital fiuMmc = addHospital("FIU-MMC Hospital");
                Hospital fiuBbc = addHospital("FIU-BBC Hospital");
                Hospital redcross = addHospital("Red Cross Hospital");
                Hospital miami = addHospital("Miami Children's Hospital");
                Hospital index = addHospital("Index Hospital");

                // Donors
                Donor bruce = addDonor("bpate005@fiu.edu", "bruPINT#111", "USA", "Miami", "FL", 33165); // Mmc + Bbc
                Donor accha = addDonor("accha004@fiu.edu", "pint#pint", "USA", "Miami", "FL", 33165);
                Donor jliu = addDonor("jliu005@fiu.edu", "pineapple", "USA", "Miami", "FL", 33165);

                Donor calvin = addDonor("calvin121@fiu.edu", "calPINT#231", "USA", "San Francisco", "CA", 82211); // Red cross
                Donor frank = addDonor("frank004@fiu.edu", "fraPINT#567", "USA", "San Francisco", "CA", 82211);
                Donor ruby = addDonor("ruby009@fiu.edu", "rubPINT#777", "USA", "San Francisco", "CA", 82211);

                Donor cora = addDonor("cora337@fiu.edu", "corPINT#671", "USA", "Atlanta", "GA", 55487); // No blood drive
                Donor jan = addDonor("jan008@fiu.edu", "janPINT#444", "USA", "Atlanta", "GA", 33328);

                Donor xuejiao = addDonor("Xuejiao@gmail.com", "Xuejiao12345", "USA", "Index", "WA", 12348); // Index blood drive
                Donor azizul = addDonor("Azizul@gmail.com", "Azizul12345", "USA", "Index", "WA", 12348);
                Donor xliu = addDonor("xliu002@fiu.edu", "helloworld", "USA", "Index", "WA", 12348);

                Donor dolly = addDonor("dolly999@fiu.edu", "dolPINT#177", "USA", "New York", "NY", 48779); // No blood drive
                Donor david = addDonor("david888@gmail.com", "davPINT#345", "USA", "Dallas", "TX", 77654); // No blood drive

                // Managers
                Employee mhaki = addEmployee(fiuMmc, "mhaki005@pint.edu", "Pintpass123", "Muhammad", "Hakim", "1231231234", UserRole.MANAGER);
                Employee dpatel = addEmployee(fiuBbc, "dpatel001@pint.edu", "pint#628", "Dweep", "Patel", "4231231234", UserRole.MANAGER);
                Employee mikeh = addEmployee(redcross, "mharr001@pint.edu", "pint#773", "Michael", "Harrison", "4231481234", UserRole.MANAGER);
                Employee edlop = addEmployee(miami, "edulop@pint.edu", "pint#112", "Eduardo", "Lopez", "4231671234", UserRole.MANAGER);
                Employee sandra = addEmployee(index, "sandra@pint.edu", "pint#2112", "Sandra", "Jennings", "4212671234", UserRole.MANAGER);

                // Coordinators
                Employee gregory = addEmployee(fiuMmc, "Greg001@pint.edu", "Greg123456", "Gregory", "Jean-Baptiste", "7861738554", UserRole.COORDINATOR);
                Employee diane = addEmployee(fiuMmc, "dpatel@pint.edu", "dpatepoq123456", "Diane", "Patel", "9541778554", UserRole.COORDINATOR);
                Employee jpea = addEmployee(fiuMmc, "jpea006@gmail.edu", "Xert123456", "Jan", "Peas", "3058469514", UserRole.COORDINATOR);
                Employee asaks = addEmployee(fiuMmc, "asaks004@fiu.edu", "corPINT#254", "Ali", "Saksh", "7864313245", UserRole.COORDINATOR);
                Employee msham = addEmployee(fiuBbc, "msham001@pint.edu", "asham!@3254", "Mike", "Sham", "7812313245", UserRole.COORDINATOR);
                Employee ravi = addEmployee(fiuBbc, "rdavi006@pint.edu", "rdavi!#478", "Ravi", "David", "783113245", UserRole.COORDINATOR);
                Employee kperr = addEmployee(redcross, "kperr@pint.edu", "PIN897#RY", "Katy", "Perry", "7831663245", UserRole.COORDINATOR);
                Employee djone = addEmployee(miami, "djone@pint.edu", "DJ897#RY", "David", "Jones", "7831633245", UserRole.COORDINATOR);
                Employee jlaw = addEmployee(index, "jlaw@pint.edu", "!@@B#RY", "Jennifer", "Lawrence", "3331633245", UserRole.COORDINATOR);

                // Nurse
                Employee jenny = addEmployee(fiuMmc, "jenny@pint.edu", "jennyPINTwhhh", "Jenny", "Smith", "7951738554", UserRole.NURSE);
                Employee jennifer = addEmployee(fiuMmc, "jennifer@pint.edu", "jenniferPINTwhhh", "Jennifer", "James", "1151438615", UserRole.NURSE);
                Employee maritza = addEmployee(fiuMmc, "maritza@pint.edu", "maritzaPINTwhhh", "Maritza", "Michaels", "7756638615", UserRole.NURSE);
                Employee diana = addEmployee(fiuMmc, "diana@pint.edu", "dianaPINTwhhh", "Diana", "King", "7751488615", UserRole.NURSE);
                Employee clarke = addEmployee(fiuMmc, "clarke@pint.edu", "clarkePINTwhhh", "Peter", "Clarke", "8123911223", UserRole.NURSE);
                Employee leonardo = addEmployee(fiuMmc, "leonardo@pint.edu", "leonardoPINTwhhh", "Leonardo", "Bobadilla", "9451438615", UserRole.NURSE);

                Employee adelina = addEmployee(fiuBbc, "adelina@pint.edu", "adelinaPINTwhhh", "Amanda", "Delina", "7951438615", UserRole.NURSE);
                Employee linda = addEmployee(fiuBbc, "linda@pint.edu", "lindaPINTwhhh", "Linda", "Spears", "7751788115", UserRole.NURSE);
                Employee dionny = addEmployee(redcross, "dionny@pint.edu", "dionnyPINTwhhh", "Dionny", "Santiago", "4584484711", UserRole.NURSE);

                // Blood Drives
                BloodDrive fiuMmcDrive = createBloodDrive(fiuMmc, "FIU-MMC Blood Drive", "1234 FIU Way",
                        "We need blood due to the high frequency of accidents in the area.",
                        "Miami", "FL",
                        Utils.sqlDate(new DateTime(2015, 9, 23, 0, 0)),
                        Utils.sqlDate(new DateTime(2015, 12, 23, 0, 0)),
                        gregory);

                BloodDrive fiuBbcDrive = createBloodDrive(fiuBbc, "FIU-BBC Blood Drive", "1234 FIU Way",
                        "We need blood and we need it now.",
                        "Miami", "FL",
                        Utils.sqlDate(new DateTime(2015, 11, 01, 0, 0)),
                        Utils.sqlDate(new DateTime(2015, 11, 30, 0, 0)),
                        ravi);

                BloodDrive redcrossDrive = createBloodDrive(redcross, "Red Cross Blood Drive", "Red Cross Way",
                        "Due to a recent earthquake we are in dire need of blood donations.",
                        "San Francisco", "CA",
                        Utils.sqlDate(new DateTime(2015, 9, 23, 0, 0)),
                        Utils.sqlDate(new DateTime(2015, 12, 23, 0, 0)),
                        kperr);

                BloodDrive indexDrive = createBloodDrive(index, "Index Blood Drive", "Index Way",
                        "Due to a recent hurricane we are in need of blood.",
                        "Index", "WA",
                        Utils.sqlDate(new DateTime(2015, 9, 23, 0, 0)),
                        Utils.sqlDate(new DateTime(2015, 12, 23, 0, 0)),
                        jlaw);

                List<Long> nurses = new ArrayList<>();
                nurses.add(jenny.getUserId());
                nurses.add(clarke.getUserId());
                nurses.add(maritza.getUserId());
                bloodDriveService.assignNurses(userService.getUserById(gregory.getUserId()), fiuMmcDrive.getBloodDriveId(), nurses);

                List<Long> newNurses = new ArrayList<>();
                newNurses.add(linda.getUserId());
                bloodDriveService.assignNurses(userService.getUserById(ravi.getUserId()), fiuBbcDrive.getBloodDriveId(), newNurses);

                // No notifications.
                BloodDrive miamiChildrensDrive = bloodDriveService.createBloodDrive(
                        miami,
                        "Miami Children's Blood Drive", "1234 Infinite Way",
                        "We need donations for children patients.",
                        "Miami",
                        "FL",
                        Utils.sqlDate(DateTime.now().plusDays(25)),
                        Utils.sqlDate(DateTime.now().plusDays(45)),
                        djone);

                // Create a few notifications.
                Notification mmcNotification1 =
                        addNotification(fiuMmcDrive,
                                Utils.sqlDate(DateTime.now().minusDays(1)),
                                "FIU-MMC Hospital Notification", "The pizza is here.", "We've got some pineapple pizza.");

                Notification mmcNotification2 = addNotification(fiuMmcDrive,
                        Utils.sqlDate(DateTime.now().minusDays(1).plusMinutes(30)),
                        "FIU-MMC Hospital Notification", "We need O+ blood type.", "O+ is a rare blood type and we are lacking. Please come and donate.s");

                Notification mmcNotification3 = addNotification(fiuMmcDrive,
                        Utils.sqlDate(DateTime.now().minusDays(1).plusMinutes(60)),
                        "FIU-MMC Hospital Notification", "The son of one of our employees needs blood urgently.", "We are here until 6PM today. Please come by and donate.");

                // Create a few notifications.
                Notification rcNotification1 =
                        addNotification(redcrossDrive,
                                Utils.sqlDate(DateTime.now().minusDays(2)),
                                "Red Cross Notification", "We need a lot more blood.", "We required a lot more blood than originally anticipated.");

                Notification rcNotification2 =
                        addNotification(redcrossDrive,
                                Utils.sqlDate(DateTime.now().minusDays(1).plusMinutes(30)),
                                "Red Cross Notification",
                                "Thank you very much for your donations.",
                                "We are very thankful that we met our donation goals yesterday. Now let's keep going strong.");

                Notification bbcNotification1 =
                        addNotification(fiuBbcDrive,
                                Utils.sqlDate(DateTime.now().minusDays(1)),
                                "FIU-BBC Notification", "We need A- blood.", "A patient of thrombocytopenia is in a very critical condition and needs A negative blood. Please come and donate.");

                Notification bbcNotification2 =
                        addNotification(fiuBbcDrive,
                                Utils.sqlDate(DateTime.now().minusDays(1)),
                                "FIU-BBC Notification", "We are running out of time!", "Let's get more blood donations.");

                Notification indexNotification1 =
                        addNotification(indexDrive,
                                Utils.sqlDate(DateTime.now().minusDays(1)),
                                "Index Notification", "Hurricane Devistation", "Due to a recent hurricane we are in major need of blood donations. Please come and assist us as soon as possible.");

                Notification indexNotification2 =
                        addNotification(indexDrive,
                                Utils.sqlDate(DateTime.now().minusDays(1)),
                                "Index Notification", "We are collecting blood for thalassemia patients.", "Please come and donate to help these patients.");

                // Miami, FL
                addUserNotification(mmcNotification1, bruce);
                addUserNotification(mmcNotification1, accha);
                addUserNotification(mmcNotification1, jliu);
                addUserNotification(mmcNotification2, bruce);
                addUserNotification(mmcNotification2, accha);
                addUserNotification(mmcNotification2, jliu);
                addUserNotification(mmcNotification3, bruce);
                addUserNotification(mmcNotification3, accha);
                addUserNotification(mmcNotification3, jliu);

                // Miami, FL
                addUserNotification(bbcNotification1, bruce);
                addUserNotification(bbcNotification1, accha);
                addUserNotification(bbcNotification1, jliu);
                addUserNotification(bbcNotification2, bruce);
                addUserNotification(bbcNotification2, accha);
                addUserNotification(bbcNotification2, jliu);

                // San Francisco, CA
                addUserNotification(rcNotification1, calvin);
                addUserNotification(rcNotification1, frank);
                addUserNotification(rcNotification1, ruby);
                addUserNotification(rcNotification2, calvin);
                addUserNotification(rcNotification2, frank);
                addUserNotification(rcNotification2, ruby);

                // Index, WA
                addUserNotification(indexNotification1, xuejiao);
                addUserNotification(indexNotification1, azizul);
                addUserNotification(indexNotification1, xliu);
                addUserNotification(indexNotification2, xuejiao);
                addUserNotification(indexNotification2, azizul);
                addUserNotification(indexNotification2, xliu);
            }

            private BloodDrive createBloodDrive(Hospital hospital,
                                                String title,
                                                String address,
                                                String description,
                                                String city,
                                                String state,
                                                Date from,
                                                Date to,
                                                Employee coordinator) {
                return bloodDriveService.createBloodDrive(hospital,
                        title,
                        address,
                        description,
                        city,
                        state,
                        from,
                        to,
                        coordinator);
            }

            private Notification addNotification(BloodDrive drive,
                                                 Date sentTime,
                                                 String title,
                                                 String shortDescription,
                                                 String longDescription) {
                return notificationService.createNotification(drive, sentTime, title, shortDescription, longDescription);
            }

            private UserNotification addUserNotification(Notification notification, Donor user) {
                return notificationService.createUserNotification(notification, user);
            }

            private Hospital addHospital(String name) {
                return hospitalService.createHospital(name);
            }

            private Employee addEmployee(Hospital hospital,
                                         String username,
                                         String password,
                                         String firstName,
                                         String lastName,
                                         String phoneNumber,
                                         UserRole role) throws Exception {

                if (userService.getUserByEmail(username) != null) {
                    return null;
                }

                return userService.createEmployee(username, password, firstName, lastName,
                        phoneNumber, role, hospital.getId());
            }

            private Donor addDonor(String username, String password, String country, String city, String state, int zip) {

                if (userService.getUserByEmail(username) != null) {
                    return null;
                }

                return userService.createDonor(username, password, country, city, state, zip);
            }
        };
    }

    @Bean
    public Filter characterEncodingFilter() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }
}
