package com.example.nk.qw.QWProvingGrounds;

import com.example.nk.qw.QWProvingGrounds.dbEntities.MessageRequest;
import com.example.nk.qw.QWProvingGrounds.repositories.MessageRequestRepository;

import java.util.HashMap;
import java.util.Optional;

public class StubMessageRequestRepository implements MessageRequestRepository {
    HashMap<Long, MessageRequest> hashMapDB = new HashMap<>();
    long counter = 0;

    @Override
    public <S extends MessageRequest> S save(S s) {
        System.out.println("IN THE SAAAAAAVYYYYYYY TA DA DAAA DAAAA -----------------");
        counter++;
        hashMapDB.put(counter, s);
        return s;
    }

    @Override
    public boolean existsById(Long aLong) {
        return hashMapDB.containsKey(aLong);
    }


    @Override
    public Optional<MessageRequest> findById(Long aLong) {
        MessageRequest messageRequest = hashMapDB.get(aLong);
        return Optional.of(new MessageRequest(messageRequest.getType(), messageRequest.getPayload(), messageRequest.getCreated_at()));
    }


    @Override
    public Iterable<MessageRequest> findAll() {
        return hashMapDB.values();
    }

    //rest are irrelevant for this test ->
    @Override
    public Iterable<MessageRequest> findAllById(Iterable<Long> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(MessageRequest messageRequest) {

    }

    @Override
    public void deleteAll(Iterable<? extends MessageRequest> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends MessageRequest> Iterable<S> saveAll(Iterable<S> iterable) {
        return null;
    }
}
