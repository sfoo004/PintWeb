package com.pint.Data.Repositories;

import com.pint.Data.Models.Hospital;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@org.springframework.stereotype.Repository
@Transactional
public class HospitalRepository extends BaseRepository {

    public Hospital get(long hospitalId) {
        HibernateEntityManagerFactory emFactory = (HibernateEntityManagerFactory) entityManagerFactory;
        SessionFactory sessionFactory = emFactory.getSessionFactory();
        Session currentSession = sessionFactory.getCurrentSession();

        Hospital hospital = (Hospital) currentSession.get(Hospital.class, hospitalId);

        return hospital;
    }

    public List<Hospital> getHospitals() {
        List<Hospital> hospitals = null;

        try {
            HibernateEntityManagerFactory emFactory = (HibernateEntityManagerFactory) entityManagerFactory;
            SessionFactory sessionFactory = emFactory.getSessionFactory();
            Session currentSession = sessionFactory.getCurrentSession();

            SQLQuery query = currentSession.createSQLQuery(("SELECT * FROM hospital"));
            query.addEntity(Hospital.class);

            hospitals = query.list();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return hospitals;
    }
}
