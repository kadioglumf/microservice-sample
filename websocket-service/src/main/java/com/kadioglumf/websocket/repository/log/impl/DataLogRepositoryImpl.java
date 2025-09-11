package com.kadioglumf.websocket.repository.log.impl;

import com.kadioglumf.websocket.model.WebsocketLogModel;
import com.kadioglumf.websocket.repository.log.WebsocketLogRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class DataLogRepositoryImpl implements WebsocketLogRepository {

  @PersistenceContext private EntityManager entityManager;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void save(WebsocketLogModel logModel) {
    entityManager.persist(logModel);
  }
}
