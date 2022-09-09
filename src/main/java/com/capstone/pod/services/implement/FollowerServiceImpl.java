package com.capstone.pod.services.implement;

import com.capstone.pod.constant.common.EntityName;
import com.capstone.pod.constant.common.ErrorMessage;
import com.capstone.pod.constant.credential.CredentialErrorMessage;
import com.capstone.pod.dto.follower.FollowerDTO;
import com.capstone.pod.entities.Credential;
import com.capstone.pod.entities.Follower;
import com.capstone.pod.entities.User;
import com.capstone.pod.exceptions.CredentialNotFoundException;
import com.capstone.pod.repositories.CredentialRepository;
import com.capstone.pod.repositories.FollowerRepository;
import com.capstone.pod.repositories.UserRepository;
import com.capstone.pod.services.FollowerService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.core.ApplicationContext;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowerServiceImpl implements FollowerService {

    private final FollowerRepository followerRepository;
    private final CredentialRepository credentialRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    private Credential getCredential() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentCredentialId = (String) authentication.getCredentials();
        Credential credential = credentialRepository.findById(currentCredentialId).orElseThrow(() -> new CredentialNotFoundException(CredentialErrorMessage.CREDENTIAL_NOT_FOUND_EXCEPTION));

        return credential;

    }

    public List<FollowerDTO> get() {
        Credential credential = getCredential();
        return credential.getUser().getFollowers()
            .stream()
            .map(Follower::getUser)
            .collect(Collectors.toList())
            .stream()
            .map(follower ->modelMapper.map(follower , FollowerDTO.class))
            .collect(Collectors.toList());
    }

    public List<Follower> getUser() {
        Credential credential = getCredential();
        return followerRepository.findByUser(credential.getUser());
    }

    @Override
    public void follow(String idolId) {
        Credential credential = getCredential();
        User idol = userRepository.findById(idolId).orElseThrow(
            () -> new EntityNotFoundException(EntityName.USER + "_" + ErrorMessage.NOT_FOUND)
        );

        Optional<Follower> follower = followerRepository.findByFollowerAndUser(credential.getUser(), idol);

        if (follower.isPresent()) {
            throw new EntityNotFoundException(EntityName.FOLLOWER + "_" + ErrorMessage.ALREADY_FOLLOW);
        }
        followerRepository.save(Follower.builder().user(credential.getUser()).follower(idol).build());
    }
}
