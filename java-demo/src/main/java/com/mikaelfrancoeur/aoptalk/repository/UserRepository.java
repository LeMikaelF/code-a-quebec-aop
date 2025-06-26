package com.mikaelfrancoeur.aoptalk.repository;

import com.mikaelfrancoeur.aoptalk.audit.AuditAction;
import com.mikaelfrancoeur.aoptalk.audit.Audited;
import lombok.Builder;

import java.util.List;

@SuppressWarnings("unused")
public class UserRepository {

    @Builder
    public record UsersDTO(
            List<UserDTO> users,
            Object metadata
    ) {
        @Builder
        public record UserDTO(
                String userId
        ) {
        }
    }

    @Audited(action = AuditAction.SAVE, expression = "#userId")
    public <T> void save(String userId, T object) {
        // ...
    }

    @Audited(action = AuditAction.SAVE, expression = "#usersDTO.users.![userId]")
    public <T> void save(UsersDTO usersDTO, T object) {
        // ...
    }
}
