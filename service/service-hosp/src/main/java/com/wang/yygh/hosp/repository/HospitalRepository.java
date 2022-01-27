package com.wang.yygh.hosp.repository;

import com.wang.yygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Wang
 * @since 2022/1/26
 */
@Repository
public interface HospitalRepository extends MongoRepository<Hospital, String> {
}
