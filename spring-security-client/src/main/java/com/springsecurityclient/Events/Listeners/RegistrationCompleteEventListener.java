package com.springsecurityclient.Events.Listeners;

import com.springsecurityclient.Entities.User;
import com.springsecurityclient.Events.RegistrationCompleteEvent;
import com.springsecurityclient.Services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent>
{

    private final UserService userService;

    public RegistrationCompleteEventListener(UserService userService)
    {
        this.userService = userService;
    }


    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event)
    {
        // create the verification token for the user.

        User user = event.getUser();
        String token = UUID.randomUUID().toString();

        userService.saveVerificationTokenForUser(token,user);


        String url = event.getApplicationUrl() + "/verifyRegistration?token=" + token;


        log.info("Click the link to verify your account: " + url);

    }




}
