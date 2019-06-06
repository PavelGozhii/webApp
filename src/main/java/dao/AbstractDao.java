package dao;

import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AbstractDao<T> implements GenericDao<T> {
    private Class<T> tClass;
    private static final Logger logger = Logger.getLogger(AbstractDao.class);

    public AbstractDao(Class<T> tClass) {
        this.tClass = tClass;
    }

    public boolean save(T t) {
        boolean result = false;
        try (Connection connection = DBConnector.connect()) {
            StringBuilder query = new StringBuilder("INSERT INTO " + getClassName() + " (");
            Field[] fields = tClass.getDeclaredFields();
            String[] strings = getFieldsName();
            for (int i = 0; i < strings.length; i++) {
                fields[i].setAccessible(true);
                if (fields[i].get(t) != null) {
                    if (i == fields.length - 1) {
                        query.append(strings[i]).append(")");
                    } else {
                        query.append(strings[i]).append(", ");
                    }
                }
            }
            query.append(" VALUES ( ");
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                if (fields[i].get(t) != null) {
                    if (i == fields.length - 1) {
                        query.append("'").append(fields[i].get(t)).append("')");
                    } else {
                        query.append("'").append(fields[i].get(t)).append("',");
                    }
                }
            }
            logger.debug(query);
            PreparedStatement preparedStatement = connection.prepareStatement(query.toString());
            result = preparedStatement.execute();
        } catch (SQLException | IllegalAccessException e) {
            logger.warn(e.getMessage(), e);
        }
        return result;
    }

    public T getById(String id) {
        T t = getNewInstance();
        try (Connection connection = DBConnector.connect()) {
            String query = "SELECT * FROM " + getClassName() + " WHERE id = " + id;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            logger.debug(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                t = setFields(resultSet);
            }
        } catch (SQLException e) {
            logger.warn(e.getMessage(), e);
        }
        return t;
    }

    public List<T> getByCriteria(String value, Field field) {
        List<T> list = new ArrayList<>();
        try (Connection connection = DBConnector.connect()) {
            String query = "SELECT * FROM " + getClassName() + " WHERE " + field.getName() + " = " + value;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            logger.debug(query);
            while (resultSet.next()) {
                list.add(setFields(resultSet));
            }
        } catch (SQLException e) {
            logger.warn(e.getMessage(), e);
        }
        return list;
    }

    public List<T> getByCriteria(String value, String field) {
        List<T> list = new ArrayList<>();
        try (Connection connection = DBConnector.connect()) {
            String query = "SELECT * FROM " + getClassName() + " WHERE " + field + " = '" + value + "'";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            logger.debug(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(setFields(resultSet));
            }
        } catch (SQLException e) {
            logger.warn(e.getMessage(), e);
        }
        return list;
    }

    @Override
    public boolean deleteByCriteria(String value, String field) {
        try (Connection connection = DBConnector.connect()) {
            String query = "DELETE FROM " + getClassName() + " WHERE " + field + " ='" + value + "'";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            logger.debug(query);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            logger.warn(e.getMessage());
            return false;
        }
    }

    public boolean update(T t) {
        try (Connection connection = DBConnector.connect()) {
            StringBuilder query = new StringBuilder("UPDATE " + getClassName() + " SET ");
            Field[] fields = tClass.getDeclaredFields();
            String[] strings = getFieldsName();
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                query.append(strings[i]).append(" = '").append(fields[i].get(t));
                if (i == fields.length - 1) {
                    query.append("' ");
                } else {
                    query.append("', ");
                }
            }
            Field field = tClass.getDeclaredField("id");
            field.setAccessible(true);
            query.append("WHERE id =").append(field.get(t));
            PreparedStatement preparedStatement = connection.prepareStatement(query.toString());
            logger.debug(query);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException | IllegalAccessException | NoSuchFieldException e) {
            logger.warn(e.getMessage(), e);
            return false;
        }
    }

    public boolean delete(String id) {
        try (Connection connection = DBConnector.connect()) {
            String query = "DELETE FROM " + getClassName() + " WHERE id = " + id;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            logger.debug(query);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            logger.warn(e.getMessage(), e);
            return false;
        }
    }

    public List<T> getAll() {
        List<T> list = new ArrayList<>();
        try (Connection connection = DBConnector.connect()) {
            String query = "SELECT * FROM " + getClassName();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            logger.debug(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(setFields(resultSet));
            }
        } catch (SQLException e) {
            logger.warn(e.getMessage(), e);
        }
        return list;
    }

    private String[] getFieldsName() {
        Field[] fields = tClass.getDeclaredFields();
        String[] nameOfFields = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            nameOfFields[i] = fields[i].getName();
        }
        return nameOfFields;
    }

    private String getClassName() {
        return tClass.getSimpleName();
    }

    private T getNewInstance() {
        T t = null;
        try {
            Class clazz = Class.forName(tClass.getName());
            t = (T) clazz.newInstance();
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
        return t;
    }

    private T setFields(ResultSet resultSet) {
        T t = getNewInstance();
        try {
            Field[] fields = tClass.getDeclaredFields();
            String[] strings = getFieldsName();
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                fields[i].set(t, resultSet.getObject(strings[i]));
            }
        } catch (SQLException | IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        }
        return t;
    }
}
