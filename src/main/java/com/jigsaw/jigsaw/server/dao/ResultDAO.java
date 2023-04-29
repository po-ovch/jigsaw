package com.jigsaw.jigsaw.server.dao;

import com.jigsaw.jigsaw.server.db.HibernateSessionFactoryUtil;
import com.jigsaw.jigsaw.server.db.Result;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ResultDAO {

    public Result findById(int id) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(Result.class, id);
    }

    public void save(Result user) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.persist(user);
        tx1.commit();
        session.close();
    }

    public List<Result> findAll() {
        var session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        CriteriaQuery<Result> q = session.getCriteriaBuilder().createQuery(Result.class);
        Root<Result> r = q.from(Result.class);
        q.select(r);
        List<Result> users = (List<Result>) session.createQuery(q).list();
        return users;
    }
}
