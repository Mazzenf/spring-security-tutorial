package com.springsecurityclient.Services;

import com.springsecurityclient.Entities.User;
import com.springsecurityclient.Entities.VerificationToken;
import com.springsecurityclient.Models.UserModel;

import java.util.Optional;

public interface UserService
{
    public User registerUser(UserModel userModel);

    public void saveVerificationTokenForUser(String token, User user);

    public String validateVerificationToken(String token);

    public VerificationToken generateNewVerificationToken(String oldToken);

    public User findUserByEmail(String email);

    public void createPasswordResetTokenForUser(User user, String token);

    public String validatePasswordResetToken(String token);

    public Optional<User> getUserByPasswordResetToken(String token);

    public void changePassword(User user, String newPassword);

    public boolean checkIfValidOldPassword(User user, String oldPassword);
}
