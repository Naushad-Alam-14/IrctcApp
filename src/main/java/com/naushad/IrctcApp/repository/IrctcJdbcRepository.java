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

    public List<PersonalDetail> findDetailByAge(int age) {
        Object[] parameters = new Object[]{age};
        return jdbcTemplate.query("select * from personalDetail where age =?",parameters,
                new BeanPropertyRowMapper<>(PersonalDetail.class));
    }

    public PersonalDetail getPersonalDetailByAadhaarNo(String aadhaarNo){

        String query = "select * from personalDetail where aadhaarNo=" + "'" + aadhaarNo + "'";
//        Object[] parameters = new Object[]{aadhaarNo};
        return jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(PersonalDetail.class));
    }

    public String deleteByAadhaarNo(String aadhaarNo){
        Object[] parameters = new Object[]{aadhaarNo};

        int deletedRows = jdbcTemplate.update("delete from personalDetail where aadhaarNo=?",parameters);
       if(deletedRows > 0)
           return "Successfully deleted person with aadhaarNo - " + aadhaarNo;
       return "Not able to delete";
    }

}
