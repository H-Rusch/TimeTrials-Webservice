package com.hrusch.webapp.io.request;

import com.hrusch.webapp.model.request.UserRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class UserRequestTest {

    @Autowired
    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void validRequestHasNoErrors() {
        var userRequest = createValidUserRequest();

        var validationErrors = validator.validate(userRequest);

        assertThat(validationErrors).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("provideUserRequestsViolatingPasswordFormat")
    void violatingPasswordFormatResultsInCorrectErrorMessage(UserRequest userRequest, int size, String message) {
        var validationErrors = validator.validate(userRequest);

        assertThat(validationErrors).hasSize(size);
        assertThat(validationErrors.stream().findFirst().orElseThrow().getMessage())
                    .startsWith(message);
    }

    @ParameterizedTest
    @MethodSource("provideUserRequestsViolatingUsernameFormat")
    void violatingUsernameFormatResultsInCorrectErrorMessage(UserRequest userRequest, int size, String message) {
        var validationErrors = validator.validate(userRequest);

        assertThat(validationErrors).hasSize(size);
        assertThat(validationErrors.stream().findFirst().orElseThrow().getMessage())
                .startsWith(message);
    }

    private static UserRequest createValidUserRequest() {
        UserRequest request = new UserRequest();
        request.setUsername("Testing");
        request.setPassword("password123");
        request.setRepeatedPassword("password123");

        return request;
    }

    private static Stream<Arguments> provideUserRequestsViolatingPasswordFormat() {
        return Stream.of(
                Arguments.of(createNonMatchingPasswordsUserRequest(), 1, "The given passwords have to match."),
                Arguments.of(createMissingDigitsPasswordUserRequest(), 2, "The given password has an invalid format. It must contain a letter and a digit."),
                Arguments.of(createMissingLettersPasswordUserRequest(), 2, "The given password has an invalid format. It must contain a letter and a digit."),
                Arguments.of(createTooShortPasswordUserRequest(), 2, "A password has to contain at least")
        );
    }

    private static UserRequest createNonMatchingPasswordsUserRequest() {
        UserRequest request = createValidUserRequest();
        request.setPassword("anotherPassword123");

        return request;
    }

    private static UserRequest createMissingLettersPasswordUserRequest() {
        UserRequest request = createValidUserRequest();
        request.setPassword("12345678");
        request.setRepeatedPassword("12345678");

        return request;
    }

    private static UserRequest createMissingDigitsPasswordUserRequest() {
        UserRequest request = createValidUserRequest();
        request.setPassword("abcdefgh");
        request.setRepeatedPassword("abcdefgh");

        return request;
    }

    private static UserRequest createTooShortPasswordUserRequest() {
        UserRequest request = createValidUserRequest();
        request.setPassword("abc123");
        request.setRepeatedPassword("abc123");

        return request;
    }

    private static Stream<Arguments> provideUserRequestsViolatingUsernameFormat() {
        return Stream.of(
                Arguments.of(createTooShortUsernameRequest(), 1, "The username has to be between"),
                Arguments.of(createTooLongUsernameRequest(), 1, "The username has to be between")
        );
    }

    private static UserRequest createTooShortUsernameRequest() {
        UserRequest request = createValidUserRequest();
        request.setUsername("ab");

        return request;
    }

    private static UserRequest createTooLongUsernameRequest() {
        UserRequest request = createValidUserRequest();
        request.setUsername("a".repeat(25));

        return request;
    }
}
