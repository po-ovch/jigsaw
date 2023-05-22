package com.jigsaw.server.dao;

import com.jigsaw.server.db.HibernateSessionFactoryUtil;
import com.jigsaw.server.db.Result;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
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
        List<Result> users = new ArrayList<>();
        try (var session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
//            session.createQuery("from Result", Result.class).list();
//            CriteriaQuery<Result> q = session.getCriteriaBuilder().createQuery(Result.class);
//            Root<Result> r = q.from(Result.class);
//            q.select(r);
//            users = session.createQuery(q).list();
            users = session.createQuery("from Result", Result.class).list();
        } catch (Exception e) {
            System.out.println(e);
        }

        return users;
    }
}
