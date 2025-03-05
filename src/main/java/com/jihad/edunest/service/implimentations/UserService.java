package com.jihad.edunest.service.implimentations;

import com.jihad.edunest.domaine.entities.Member;
import com.jihad.edunest.exception.user.UserNotFoundException;
import com.jihad.edunest.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final MemberRepository userRepository;
    private final LocalDateTime licenseExpirationDate = LocalDateTime.now().plusYears(1);


    public Optional<Member> findByUsername(String userName) {
        if (userName == null || userName.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null");
        }
        return userRepository.findByUsernameAndDeletedFalse(userName);

    }


    public Page<Member> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Member> userPage = userRepository.findAll(pageable);

        return userPage;
    }

    public Page<Member> searchByUsernameOrCin(String searchKeyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return userRepository.findByUsernameContainingOrEmailContainingAndDeletedFalse(searchKeyword, searchKeyword, pageable);
    }

    public Optional<Member> findByEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        return userRepository.findByEmailAndDeletedFalse(email);
    }

    public Member findUserById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }



    @Transactional
    public boolean markUserAsDeleted(Long userId) {
        Member user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setDeleted(true);
        userRepository.save(user);
        return true;
    }
}
