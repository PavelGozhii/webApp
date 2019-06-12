package dao;

import model.annotation.Column;
import model.annotation.Id;
import model.annotation.Table;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AbstractDao<T> implements GenericDao<T> {
    private Class<T> tClass;
    private static final Logger logger = Logger.getLogger(AbstractDao.class);

    public AbstractDao(Class<T> tClass) {
        this.tClass = tClass;
    }

    @Override
    public boolean save(T t) {
        boolean result = false;
        try (Connection connection = DBConnector.connect()) {
            String query = generateSaveQuery(t);
            logger.debug(query);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            result = preparedStatement.execute();
        } catch (SQLException e) {
            logger.warn(e.getMessage(), e);
        }
        return result;
    }

    private String generateSaveQuery(T t) {
        List<Field> fields = getColumn();
        StringBuilder query = new StringBuilder("INSERT INTO " + getTableName() + " (");
        query = appendFieldToSaveQuery(fields.stream()
                .filter(field -> isNotNull(t, field))
                .map(field -> field.getAnnotation(Column.class).name())
                .collect(Collectors.toList()), query)
                .append(" VALUES ( ");
        query = appendFieldToSaveQuery(fields.stream()
                .filter(field -> isNotNull(t, field))
                .map(field -> {
                    try {
                        field.setAccessible(true);
                        return "'" + String.valueOf(field.get(t)) + "'";
                    } catch (IllegalAccessException e) {
                        logger.warn(e);
                        return "";
                    }
                })
                .collect(Collectors.toList()), query);
        logger.debug(query);
        return query.toString();
    }

    private boolean isNotNull(T t, Field field) {
        try {
            field.setAccessible(true);
            return field.get(t) != null;
        } catch (IllegalAccessException e) {
            logger.warn(e);
            return false;
        }
    }

    private StringBuilder appendFieldToSaveQuery(List<String> fields, StringBuilder query) {
        IntStream.range(0, fields.size())
                .filter(i -> fields.get(i) != null)
                .forEachOrdered(i -> query.append(fields.get(i))
                        .append(i == fields.size() - 1 ? ")" : ","));
        return query;
    }

    @Override
    public T getById(String id) {
        T t = getNewInstance();
        try (Connection connection = DBConnector.connect()) {
            String query = "SELECT * FROM " + getTableName() + " WHERE " + getId().getAnnotation(Column.class).name() + " = " + id;
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

    @Override
    public List<T> getByCriteria(String value, String field) {
        List<T> list = new ArrayList<>();
        try (Connection connection = DBConnector.connect()) {
            String query = "SELECT * FROM " + getTableName() + " WHERE " + field + " = '" + value + "'";
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
            String query = "DELETE FROM " + getTableName() + " WHERE " + field + " ='" + value + "'";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            logger.debug(query);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            logger.warn(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(T t) {
        try (Connection connection = DBConnector.connect()) {
            String query = createUpdateQuery(t);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            logger.debug(query);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            logger.warn(e.getMessage(), e);
            return false;
        }
    }

    private String createUpdateQuery(T t) {
        StringBuilder query = new StringBuilder("UPDATE " + getTableName() + " SET ");
        try {
            List<Field> fields = getColumn();
            for (int i = 0; i < fields.size(); i++) {
                fields.get(i).setAccessible(true);
                query.append(fields.get(i).getAnnotation(Column.class).name()).append(" = '").append(fields.get(i).get(t));
                if (i == fields.size() - 1) {
                    query.append("' ");
                } else {
                    query.append("', ");
                }
            }
            Field field = tClass.getDeclaredField("id");
            field.setAccessible(true);
            query.append("WHERE " + getId().getAnnotation(Column.class).name() + " =").append(field.get(t));
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return query.toString();
    }

    @Override
    public boolean delete(String id) {
        try (Connection connection = DBConnector.connect()) {
            String query = "DELETE FROM " + getTableName() + " WHERE " + getId().getAnnotation(Column.class).name() + " = " + id;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            logger.debug(query);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            logger.warn(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public List<T> getAll() {
        List<T> list = new ArrayList<>();
        try (Connection connection = DBConnector.connect()) {
            String query = "SELECT * FROM " + getTableName();
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

    private List<Field> getColumn() {
        Field[] fields = tClass.getDeclaredFields();
        List<Field> fieldList = new ArrayList<>();
        for (Field field : fields) {
            if (field.getAnnotation(Column.class) != null) {
                fieldList.add(field);
            }
        }
        return fieldList;
    }

    private String getTableName() {
        return tClass.getAnnotation(Table.class).name();
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

    private Field getId() {
        Field[] fields = tClass.getDeclaredFields();
        Field key = null;
        for (Field field : fields) {
            if (field.getAnnotation(Id.class) != null) {
                key = field;
                break;
            }
        }
        return key;
    }

    private T setFields(ResultSet resultSet) {
        T t = getNewInstance();
        try {
            List<Field> fields = getColumn();
            for (int i = 0; i < fields.size(); i++) {
                fields.get(i).setAccessible(true);
                fields.get(i).set(t, resultSet.getObject(fields.get(i).getAnnotation(Column.class).name()));
            }
        } catch (SQLException | IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        }
        return t;
    }
}
