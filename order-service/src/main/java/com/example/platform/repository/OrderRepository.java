package com.example.platform.repository;

import com.example.platform.entity.Order;
import com.example.platform.entity.OrderItem;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final SessionFactory sessionFactory;

    public void save(Order order, List<OrderItem> items) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(order);
            for (OrderItem item : items) {
                session.persist(item);
            }
            session.getTransaction().commit();
        }
    }

    public void updateStatus(Order order) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(order);
            session.getTransaction().commit();
        }
    }
}
