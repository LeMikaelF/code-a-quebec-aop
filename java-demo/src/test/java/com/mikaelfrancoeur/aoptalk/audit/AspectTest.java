package com.mikaelfrancoeur.aoptalk.audit;

import com.mikaelfrancoeur.aoptalk.repository.UserRepository;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.AopTestUtils;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ImportAutoConfiguration(AopAutoConfiguration.class)
@SpringBootTest(classes = {
        AuditAspect.class,
        AspectTest.Config.class,
})
class AspectTest implements WithAssertions {

    public static final String USER_ID = "user id";
    public static final String USER_ID_2 = "user id 2";
    public static final Object PAYLOAD = new Object();

    @MockitoBean
    private AuditService auditService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void resetManualMock() {
        Mockito.reset(userRepository);
    }

    @Test
    void withSingleUserId() {
        userRepository.save(USER_ID, PAYLOAD);
        verify(auditService).audit(AuditAction.SAVE, USER_ID);
    }

    @Test
    void withMultipleUserIds() {
        userRepository.save(UserRepository.UsersDTO.builder()
                .users(List.of(
                        UserRepository.UsersDTO.UserDTO.builder().userId(USER_ID).build(),
                        UserRepository.UsersDTO.UserDTO.builder().userId(USER_ID_2).build()
                ))
                .metadata("some metadata")
                .build(), PAYLOAD);

        verify(auditService).audit(AuditAction.SAVE, USER_ID);
        verify(auditService).audit(AuditAction.SAVE, USER_ID_2);
    }

    @Test
    void whenRepositoryThrows() {
        doThrow(new RuntimeException()).when(AopTestUtils.<UserRepository>getUltimateTargetObject(userRepository))
                .save(any(String.class), any());

        @SuppressWarnings("ThrowableNotThrown")
        var _ = catchThrowable(() -> userRepository.save(USER_ID, PAYLOAD));

        verifyNoInteractions(auditService);
    }

    static class Config {
        @Bean
        UserRepository userRepository() {
            return mock();
        }
    }
}
