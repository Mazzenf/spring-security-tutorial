package com.springsecurityclient.Controllers;


import com.springsecurityclient.Entities.User;
import com.springsecurityclient.Entities.VerificationToken;
import com.springsecurityclient.Events.RegistrationCompleteEvent;
import com.springsecurityclient.Models.PasswordModel;
import com.springsecurityclient.Models.UserModel;
import com.springsecurityclient.Services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
public class RegistrationController
{

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher publisher;


    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel, final HttpServletRequest request)
    {
        User user = userService.registerUser(userModel);
        publisher.publishEvent(new RegistrationCompleteEvent(user,
                applicationUrl(request)
        ));
        return "Success";

    }




    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") String token) {
        String result = userService.validateVerificationToken(token);
        if(result.equalsIgnoreCase("valid")) {
            return "User Verified Successfully";
        }
        return "Bad User";
    }


    @GetMapping("/resendVerifyToken")
    public String resendVerificationToken(@RequestParam("token") String oldToken, HttpServletRequest request)
    {
        VerificationToken verificationToken = userService.generateNewVerificationToken(oldToken);

        User user = verificationToken.getUser();
        resendVerificationTokenMail(user, applicationUrl(request), verificationToken);

        return "Verification Link Sent";

    }


    @PostMapping("resetPassword")
    public String resetPassword(@RequestBody PasswordModel passwordModel, HttpServletRequest request)
    {
        User user = userService.findUserByEmail(passwordModel.getEmail());

        String url = "";

        if (user != null)
        {
            String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, token);

            url = passwordResetTokenMail(user, applicationUrl(request), token);

        }

        return url;

    }


    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("token") String token, @RequestBody PasswordModel passwordModel)
    {
        String result = userService.validatePasswordResetToken(token);

        if (!result.equalsIgnoreCase("valid"))
        {
            return "Invalid token";
        }

        Optional<User> user = userService.getUserByPasswordResetToken(token);

        if (user.isPresent())
        {
            userService.changePassword(user.get(), passwordModel.getNewPassword());

            return "Password reset successfully";
        }
        else
        {
            return "Invalid token";
        }


    }


    @PostMapping("/changePassword")
    public String changePassword(@RequestBody PasswordModel passwordModel)
    {
        User user = userService.findUserByEmail(passwordModel.getEmail());

        if (!userService.checkIfValidOldPassword(user, passwordModel.getOldPassword()))
        {
            return "Invalid old password";
        }


        //save new password
        userService.changePassword(user, passwordModel.getNewPassword());
        return "Password changed Successfully";

    }




    private String passwordResetTokenMail(User user, String applicationUrl, String token)
    {
        String url = applicationUrl + "/savePassword?token=" + token;


        log.info("Click the link to reset your password: " + url);

        return url;
    }


    private void resendVerificationTokenMail(User user, String applicationUrl, VerificationToken verificationToken)
    {
        String url = applicationUrl + "/verifyRegistration?token=" + verificationToken.getToken();


        log.info("Click the link to verify your account: " + url);
    }

    private String applicationUrl(HttpServletRequest request)
    {
        return "http://" + request.getServerName()+ ":" + request.getServerPort() + request.getContextPath();
    }

}
