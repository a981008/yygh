package com.wang.yygh.hosp.repository;

import com.wang.yygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Wang
 * @since 2022/1/26
 */
@Repository
public interface HospitalRepository extends MongoRepository<Hospital, String> {
    List<Hospital> findHospitalByHosnameLike(String hosname);
}
