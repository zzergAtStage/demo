package com.example.demo;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис для обновления пользователя.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Обновляет пользователя по ID.
     *
     * @param id ID пользователя
     * @param dto данные для обновления
     * @return обновлённая сущность
     */
    @Transactional
    public UserModel updateUser(Long id, UserUpdateDto dto) {
        UserModel user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setName(dto.name);
        user.setEmail(dto.email);
        return user;
    }

    public UserModel createUser(UserCreateDto dto) {
        UserModel user = new UserModel();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        return userRepository.save(user);
    }

    public UserModel getUser(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}