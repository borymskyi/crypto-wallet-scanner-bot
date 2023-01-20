package com.borymskyi.dao;

import com.borymskyi.entity.RawData;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Dmitrii Borymskyi
 * @version 1.0
 */
public interface RawDataDAO extends JpaRepository<RawData, Long> {
}
