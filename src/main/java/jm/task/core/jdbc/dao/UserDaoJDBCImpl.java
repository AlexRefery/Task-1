package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS users (" +
                    "    id INT8 PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                    "    name VARCHAR(100) NOT NULL, " +
                    "    lastname VARCHAR(100) NOT NULL, " +
                    "    age SMALLINT NOT NULL" +
                    ");";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS users CASCADE;";
    private static final String SAVE_QUERY = "INSERT INTO users (name, lastname, age) VALUES(?, ?, ?);";
    private static final String SELECT = "SELECT id, name, lastname, age FROM users ORDER BY name, lastname";
    private static final String CLEAN_TABLE = "TRUNCATE TABLE users RESTART IDENTITY";
    private static final String DELETE_ONE = "DELETE FROM users WHERE id=?";

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try(Connection connection = Util.getConnection();
            Statement statement = connection.createStatement()) {

            statement.executeUpdate(CREATE_TABLE);
        } catch (SQLException e) {
            System.out.println("При создании таблицы что-то пошло не так!");
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        try(Connection connection = Util.getConnection();
            Statement statement = connection.createStatement()) {

            statement.executeUpdate(DROP_TABLE);
        } catch (SQLException e) {
            System.out.println("При удалении таблицы что-то пошло не так!");
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try(Connection connection = Util.getConnection();
            PreparedStatement statement = connection.prepareStatement(SAVE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setInt(3, age);
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                System.out.println("Создали юзера с номером id= " + rs.getLong("id"));
            }

        } catch (SQLException e) {
            System.out.println("При записи юзера что-то пошло не так!");
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        try(Connection connection = Util.getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_ONE)) {

            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("При удалении юзера что-то пошло не так!");
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try(Connection connection = Util.getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT);
            ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastname"));
                user.setAge(resultSet.getByte("age"));
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println("При чтении списка юзеров что-то пошло не так!");
            throw new RuntimeException(e);
        }
        return users;
    }

    public void cleanUsersTable() {
        try(Connection connection = Util.getConnection();
            Statement statement = connection.createStatement()) {

            statement.executeUpdate(CLEAN_TABLE);
        } catch (SQLException e) {
            System.out.println("При очистке таблицы что-то пошло не так!");
            throw new RuntimeException(e);
        }
    }
}
