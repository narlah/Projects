package com.example.nk.qw.QWProvingGrounds.repositories;

import com.example.nk.qw.QWProvingGrounds.dbEntities.MessageRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRequestRepository extends CrudRepository<MessageRequest, Long> {

}
