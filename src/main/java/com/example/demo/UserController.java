package com.example.demo;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService, DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.userService = userService;
        this.jdbcTemplate = jdbcTemplate;
        try (Connection connection = DataSourceUtils.getConnection(dataSource)) {
            connection.setReadOnly(true);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to set read-only mode", e);
        }
    }

    /**
     * Обновляет пользователя по ID.
     *
     * @param id  ID пользователя
     * @param dto JSON-данные для обновления
     * @return обновлённый пользователь
     */
    @PutMapping("/user/{id}")
    public UserModel update(@PathVariable Long id, @RequestBody UserUpdateDto dto) {
        return userService.updateUser(id, dto);
    }

    @PostMapping("/user")
    public UserModel create(@RequestBody UserCreateDto dto) {
        return userService.createUser(dto);
    }

    @GetMapping("/user/{id}")
    public UserModel getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    private final DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/probe")
    public String probeReadOnly() {
        jdbcTemplate.execute("SET SESSION CHARACTERISTICS AS TRANSACTION READ ONLY");
        return "session is now read-only";
    }
}