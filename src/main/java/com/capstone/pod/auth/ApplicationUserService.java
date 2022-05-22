package com.capstone.pod.auth;

import com.capstone.pod.constant.user.UserErrorMessage;
import com.capstone.pod.entities.User;
import com.capstone.pod.exceptions.EmailNotFoundException;
import com.capstone.pod.repositories.UserRepository;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Data
@RequiredArgsConstructor
@Builder
public class ApplicationUserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws EmailNotFoundException {
        Optional<User> user = userRepository.findUserByEmail(email);
        if (user.get() == null) {
            throw new EmailNotFoundException(UserErrorMessage.EMAIL_NOT_FOUND);
        }
        return new UserDetail(user.get());
    }
}
