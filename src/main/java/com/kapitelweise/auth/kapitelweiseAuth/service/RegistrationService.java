package com.kapitelweise.auth.kapitelweiseAuth.service;


import com.kapitelweise.auth.kapitelweiseAuth.dao.UserDao;
import com.kapitelweise.auth.kapitelweiseAuth.domain.AppUser;
import com.kapitelweise.auth.kapitelweiseAuth.request.RegistrationRequest;
import com.kapitelweise.auth.kapitelweiseAuth.response.EventType;
import com.kapitelweise.auth.kapitelweiseAuth.response.RegisterResponse;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RegistrationService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final AmazonSQSService amazonSQSService;


    public RegistrationService(UserDao userDao, PasswordEncoder passwordEncoder, AmazonSQSService amazonSQSService){
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.amazonSQSService = amazonSQSService;
    }
    @Transactional
    public RegisterResponse register(RegistrationRequest request){
        if(userDao.userExists(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ("Benutzername bereits vergeben"));
        }

        if(request.getPassword().length() < 10){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ("ungültige Eingabe"));
        }
        String hash = passwordEncoder.encode(request.getPassword());

        try{
            AppUser user =  userDao.saveUser(new AppUser(request.getUsername(), hash));
            // Nachricht geht noch innerhalb der Transaktion raus, scheitert danach der Commit, wäre das Backend inkonsistent.
            amazonSQSService.sendNewUser(EventType.USER_CREATED, user.getId(), user.getUsername());

            return new RegisterResponse(user.getId(), user.getUsername(), user.getCreated_at());
        }catch (DuplicateKeyException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, ("Benutzername bereits vergeben."));
        }


    }
}
