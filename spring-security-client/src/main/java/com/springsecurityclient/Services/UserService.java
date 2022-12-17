package com.springsecurityclient.Services;

import com.springsecurityclient.Entities.User;
import com.springsecurityclient.Entities.VerificationToken;
import com.springsecurityclient.Models.UserModel;

public interface UserService
{
    public User registerUser(UserModel userModel);

    public void saveVerificationTokenForUser(String token, User user);

    public String validateVerificationToken(String token);

    public VerificationToken generateNewVerificationToken(String oldToken);
}
