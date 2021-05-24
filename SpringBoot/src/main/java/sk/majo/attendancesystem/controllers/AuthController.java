package sk.majo.attendancesystem.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sk.majo.attendancesystem.config.security.JwtAuthenticationResponse;
import sk.majo.attendancesystem.config.security.JwtTokenProvider;
import sk.majo.attendancesystem.entities.User;
import sk.majo.attendancesystem.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;

@RestController
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody User user) {
        if(user.getEmail() != null && user.getPassword() != null) {

            /*User findUser = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
            if(findUser != null) {
                return ResponseEntity.ok().body("token");
            }
            return ResponseEntity.badRequest().body("Wrong email or password");*/

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = tokenProvider.generateToken(authentication);
            return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
        }
        return ResponseEntity.badRequest().body("Not Found");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> registration(@RequestBody User user) {
        if(user.getEmail() != null && user.getPassword() != null) {
            if(userRepository.existsByEmail(user.getEmail())) {
                return new ResponseEntity<>("Email is in use", HttpStatus.BAD_REQUEST);
            }
            try {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                User newUser = userRepository.save(user);
                return ResponseEntity.ok(newUser);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        return new ResponseEntity<>("Missing email or password", HttpStatus.BAD_REQUEST);
    }
}
