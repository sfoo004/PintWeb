package com.pint.Data.Repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;

@org.springframework.stereotype.Repository
@Transactional
public abstract class BaseRepository extends JDBCDriver {
    @Autowired
    protected EntityManagerFactory entityManagerFactory;

    /**
     * Executes a read request
     *
     * @param id
     * @param data
     */
    public Object get(long id, Object data) {
        Object returnedObject = null;
        try {
            HibernateEntityManagerFactory emFactory = (HibernateEntityManagerFactory) entityManagerFactory;
            SessionFactory sessionFactory = emFactory.getSessionFactory();
            Session currentSession = sessionFactory.getCurrentSession();

            returnedObject = currentSession.get(data.getClass(), id);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return returnedObject;
    }

    /**
     * Executes an update request
     *
     * @param data
     */
    public void update(Object data) {
        // TODO Auto-generated method
    }

    /**
     * Executes a delete request
     *
     * @param data
     */
    public void delete(Object data) {
        try {
            HibernateEntityManagerFactory emFactory = (HibernateEntityManagerFactory) entityManagerFactory;
            SessionFactory sessionFactory = emFactory.getSessionFactory();
            Session currentSession = sessionFactory.getCurrentSession();

            currentSession.delete(data);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    ;

    /**
     * Executes a write request
     *
     * @param data
     */
    public void create(Object data) {
        try {
            HibernateEntityManagerFactory emFactory = (HibernateEntityManagerFactory) entityManagerFactory;
            SessionFactory sessionFactory = emFactory.getSessionFactory();
            Session currentSession = sessionFactory.getCurrentSession();

            currentSession.save(data);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Executes a read request
     *
     * @param id
     * @param object
     */
    public void get(String id, Object object) {
        // TODO Auto-generated method
    }
}
