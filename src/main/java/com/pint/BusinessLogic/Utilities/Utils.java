package com.pint.BusinessLogic.Utilities;

import com.pint.BusinessLogic.Security.UserRole;
import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by Dionny on 11/20/2015.
 */
public class Utils {
    public static <T> Collection<T> iterableToCollection(Iterable<T> iterable) {
        Collection<T> collection = new ArrayList<>();
        for (T obj : iterable) {
            collection.add(obj);
        }
        return collection;
    }

    public static List<Long> toLongs(List<Integer> ints) {
        int nInts = ints.size();
        List<Long> longs = new ArrayList<Long>(nInts);
        for (int i = 0; i < nInts; ++i) {
            longs.add(ints.get(i).longValue());
        }
        return longs;
    }

    public static String toTitleCase(String input) {
        if (input == null) {
            return "";
        }
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toLowerCase().toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }

    public static java.sql.Date parseDate(String date) {
        try {
            return new java.sql.Date(new SimpleDateFormat("yyyy-mm-dd").parse(date).getTime());
        } catch (ParseException e) {
            return null;
        }
    }

    public static UserRole mapRole(String role) {
        switch (role.toLowerCase()) {
            case "manager":
                return UserRole.MANAGER;

            case "coordinator":
                return UserRole.COORDINATOR;

            case "nurse":
                return UserRole.NURSE;
        }

        return null;
    }

    public static java.sql.Date sqlDate(DateTime dateTime) {
        return new java.sql.Date(dateTime.toDate().getTime());
    }
}
