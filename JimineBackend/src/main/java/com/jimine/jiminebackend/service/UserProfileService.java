package com.jimine.jiminebackend.service;

import com.jimine.jiminebackend.model.dto.UserProfileDto;
import com.jimine.jiminebackend.model.entity.User;
import com.jimine.jiminebackend.model.entity.UserInfo;
import com.jimine.jiminebackend.repository.UserInfoRepository;
import com.jimine.jiminebackend.repository.UserRepository;
import com.jimine.jiminebackend.model.request.UserProfileRequest;
import com.jimine.jiminebackend.service.security.SecurityService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserProfileService {

    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final EntityManager entityManager;

    public UserProfileDto getPrincipalProfile() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserProfileDto> userCriteriaQuery = criteriaBuilder.createQuery(UserProfileDto.class);
        Root<User> root = userCriteriaQuery.from(User.class);
        Join<User, UserInfo> userUserInfoJoin = root.join("userInfo", JoinType.INNER);
        userCriteriaQuery.select(
                criteriaBuilder.construct(
                        UserProfileDto.class,
                        root.get("username"),
                        root.get("email"),
                        userUserInfoJoin.get("firstname"),
                        userUserInfoJoin.get("lastname"),
                        userUserInfoJoin.get("surname"),
                        userUserInfoJoin.get("userSex")
                )
        );
        userCriteriaQuery.where(criteriaBuilder.equal(root.get("id"), SecurityService.getPrincipalUser().getId()));

        return entityManager.createQuery(userCriteriaQuery).getSingleResult();
    }

    public ResponseEntity<String> updateProfile(UserProfileRequest request) {
        Long principalId = SecurityService.getPrincipalUser().getId();
        User user = userRepository.findById(principalId).orElseThrow(()
                -> new RuntimeException("An error occurred"));

        if(request.getUsername() != null && !request.getUsername().isBlank()) {
            user.setUsername(request.getUsername());
        }
        if(request.getEmail() != null && !request.getEmail().isBlank()) {
            user.setEmail(request.getEmail());
        }
        userRepository.save(user);

        UserInfo userInfo = userInfoRepository.findById(principalId).orElseGet(UserInfo::new);
        if(userInfo.getId() == null) {
            userInfo.setId(principalId);
            userInfo.setUser(user);
        }
        if(request.getFirstname() != null && !request.getFirstname().isBlank()) {
            userInfo.setFirstname(request.getFirstname());
        }
        if(request.getLastname() != null && !request.getLastname().isBlank()) {
            userInfo.setLastname(request.getLastname());
        }
        if(request.getSurname() != null && !request.getSurname().isBlank()) {
            userInfo.setSurname(request.getSurname());
        }
        if(request.getUserSex() != null && !request.getUserSex().isBlank()) {
            userInfo.setUserSex(request.getUserSex());
        }
        userInfoRepository.save(userInfo);

        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
