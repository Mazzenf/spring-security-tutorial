package com.springsecurityclient.Repositories;

import com.springsecurityclient.Entities.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long>
{


     PasswordResetToken findByToken(String token);
}
