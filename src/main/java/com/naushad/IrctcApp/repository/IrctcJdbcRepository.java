package com.naushad.IrctcApp.repository;

import com.naushad.IrctcApp.model.PersonalDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class IrctcJdbcRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<PersonalDetail> findAllPersonalDetails() {
        return jdbcTemplate.query("select * from personalDetail", new BeanPropertyRowMapper<>(PersonalDetail.class));
    }

}
