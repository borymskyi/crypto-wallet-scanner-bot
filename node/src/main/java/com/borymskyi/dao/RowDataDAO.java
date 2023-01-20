package com.borymskyi.dao;

import com.borymskyi.entity.RowData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Dmitrii Borymskyi
 * @version 1.0
 */
public interface RowDataDAO extends JpaRepository<RowData, Long> {
}
