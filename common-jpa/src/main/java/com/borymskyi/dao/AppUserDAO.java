package com.borymskyi.dao;

import com.borymskyi.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Dmitrii Borymskyi
 * @version 1.0
 */
public interface AppUserDAO extends JpaRepository<AppUser, Long> {
    AppUser findAppUserByTelegramUserId(Long id);
}
