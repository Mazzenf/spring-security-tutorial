package com.springsecurityclient.Services;

import com.springsecurityclient.Entities.User;
import com.springsecurityclient.Entities.VerificationToken;
import com.springsecurityclient.Models.UserModel;
import com.springsecurityclient.Repositories.UserRepository;
import com.springsecurityclient.Repositories.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationTokenRepository tokenRepository;


    @Override
    public User registerUser(UserModel userModel)
    {
        User user = new User();
        user.setEmail(userModel.getEmail());
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));

        userRepository.save(user);

        return user;
    }

    @Override
    public void saveVerificationTokenForUser(String token, User user)
    {
        VerificationToken verificationToken = new VerificationToken(user, token);

        tokenRepository.save(verificationToken);
    }

    @Override
    public String validateVerificationToken(String token)
    {
        VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null)
        {
            return "invalid";
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();

        if (verificationToken.getExpirationTime().getTime() - cal.getTime().getTime() <= 0 )
        {
            tokenRepository.delete(verificationToken);
            return "expired";
        }

        user.setEnabled(true);
        userRepository.save(user);

        return "valid";
    }

    @Override
    public VerificationToken generateNewVerificationToken(String oldToken)
    {

        VerificationToken verificationToken = tokenRepository.findByToken(oldToken);

        verificationToken.setToken(UUID.randomUUID().toString());

        tokenRepository.save(verificationToken);

        return verificationToken;
    }
}
