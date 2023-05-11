package ma.enset.springhospital;

import ma.enset.springhospital.entities.Patient;
import ma.enset.springhospital.repository.PatientRepository;
import ma.enset.springhospital.security.service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.support.xml.Jdbc4SqlXmlHandler;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import java.util.Date;
import java.util.stream.Stream;

@SpringBootApplication
public class SpringHospitalApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringHospitalApplication.class, args);
    }
    @Bean
    CommandLineRunner start(PatientRepository patientRepository){
        return args -> {
            /*patientRepository.save(new Patient().builder().id(null).name("Scott").birthday(new Date()).isSick(false).score(345).build());
            patientRepository.save(new Patient().builder().id(null).name("Beesly").birthday(new Date()).isSick(false).score(345).build());
            patientRepository.save(new Patient().builder().id(null).name("Halpert").birthday(new Date()).isSick(false).score(345).build());*/
            //patientRepository.save(new Patient(null, "Scott", new Date(), false, 345));
            Stream.of("Scott", "Beesly", "Halpert")
                    .forEach(name->{
                        Patient patient= Patient.builder()
                                .name(name)
                                .birthday(new Date())
                                .sick(false)
                                .score((int) (100+Math.random()*100))
                                .build();
                        patientRepository.save(patient);

                    });

        };
    }

    @Bean
    CommandLineRunner commandLineRunner(JdbcUserDetailsManager jdbcUserDetailsManager) {
        PasswordEncoder passwordEncoder;
        return args -> {
            UserDetails u1= jdbcUserDetailsManager.loadUserByUsername("user11");
            if (u1==null)
            jdbcUserDetailsManager.createUser(
                    User.withUsername("user11").password(passwordEncoder().encode("1234")).disabled(false).roles("USER").build()
            );
            UserDetails u2= jdbcUserDetailsManager.loadUserByUsername("user22");
            if (u2==null)
            jdbcUserDetailsManager.createUser(
                    User.withUsername("user22").password(passwordEncoder().encode("1234")).disabled(true).roles("USER").build()
            );
            UserDetails u3= jdbcUserDetailsManager.loadUserByUsername("admin2");
            if (u3==null)
            jdbcUserDetailsManager.createUser(
                    User.withUsername("admin2").password(passwordEncoder().encode("1234")).disabled(false).roles("USER","ADMIN").build()
            );
        };
    }
    @Bean
    CommandLineRunner commandLineRunnerUserDetails(AccountService accountService){
        return args -> {
          accountService.addNewRole("USER");
          accountService.addNewRole("ADMIN");

          accountService.addNewUser("user1", "1234", "user1@gmail.com", "1234");
          accountService.addNewUser("user2", "1234", "user2@gmail.com", "1234");
          accountService.addNewUser("admin", "1234", "admin@gmail.com", "1234");

          accountService.addRoleToUser("user1", "USER");
          accountService.addRoleToUser("user2", "USER");
          accountService.addRoleToUser("admin", "USER");
          accountService.addRoleToUser("admin", "ADMIN");
        };
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
