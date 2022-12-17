package com.springsecurityclient.Entities.Listeners;

import com.springsecurityclient.Entities.User;
import com.springsecurityclient.Events.RegistrationCompleteEvent;
import org.springframework.context.ApplicationListener;

import java.util.UUID;

public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent>
{
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event)
    {
        // create the verification token for the user.

        User user = event.getUser();
        String token = UUID.randomUUID().toString();
    }
}
