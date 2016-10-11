package com.pint.Data.Repositories;

import com.pint.BusinessLogic.Utilities.Constants;
import com.pint.Data.Models.BloodDrive;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class BloodDriveBaseRepository extends BaseRepository {
    public List<BloodDrive> getBloodDrivesByLocation(String city, String state) {
        try {
            HibernateEntityManagerFactory emFactory = (HibernateEntityManagerFactory) entityManagerFactory;
            SessionFactory sessionFactory = emFactory.getSessionFactory();
            Session currentSession = sessionFactory.getCurrentSession();


            String sql = "SELECT * FROM " + Constants.BLOODDRIVE_TABLE_NAME +
                    " WHERE city='" + city + "' " +
                    "AND state='" + state + "'";

            SQLQuery query = currentSession.createSQLQuery(sql);
            query.addEntity(BloodDrive.class);

            List<BloodDrive> result = query.list();

            return result;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }
}
