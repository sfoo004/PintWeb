package com.pint.BusinessLogic.Security;

import com.pint.BusinessLogic.Utilities.Constants;
import com.pint.Data.Models.Donor;
import com.pint.Data.Models.Employee;
import com.pint.Data.Repositories.BaseRepository;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class UserHelper extends BaseRepository {
    public Donor get(String email) {
        Donor donor = null;

        try {
            HibernateEntityManagerFactory emFactory = (HibernateEntityManagerFactory) entityManagerFactory;
            SessionFactory sessionFactory = emFactory.getSessionFactory();
            Session currentSession = sessionFactory.getCurrentSession();

            String sql = "SELECT * FROM " + Constants.DONOR_TABLE_NAME + " WHERE email_address = '" + email + "'";
            SQLQuery query = currentSession.createSQLQuery(sql);
            query.addEntity(Donor.class);

            List<Donor> donors = query.list();

            if (donors != null && donors.size() > 0)
                donor = donors.get(0);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return donor;
    }

    public void createEmployee(Employee employee) {
        create(employee);
    }

    public List<Employee> getAllEmployees(Long hospitalId) {
        List<Employee> nurses = null;

        try {
            HibernateEntityManagerFactory emFactory = (HibernateEntityManagerFactory) entityManagerFactory;
            SessionFactory sessionFactory = emFactory.getSessionFactory();
            Session currentSession = sessionFactory.getCurrentSession();

            String sql = "SELECT * FROM employee WHERE hospital_id = :hospital_id";
            SQLQuery query = currentSession.createSQLQuery(sql);
            query.addEntity(Employee.class);
            query.setParameter("hospital_id", hospitalId);

            nurses = query.list();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return nurses;
    }
}
