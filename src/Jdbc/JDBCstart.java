package Jdbc;

import Jdbc.models.Role;
import Jdbc.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class JDBCstart {

    public static void main(String[] args)  {
        String sqlForRoles = "CREATE TABLE IF NOT EXISTS roles ( " +
                "id serial PRIMARY KEY," +
                "name VARCHAR(255) UNIQUE)";
        String sqlForUsers = "CREATE TABLE users (" +
                "id serial PRIMARY KEY," +
                "username VARCHAR(255))";
        String sqlForUserRole = "CREATE TABLE user_role (" +
                "user_id INT NOT NULL," +
                "role_id INT NOT NULL," +
                "PRIMARY KEY (user_id, role_id)," +
                "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE," +
                "FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE)";


        try (Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://172.17.0.2:5432/db_jdbc",
                "postgres",
                "qwerty")
        ) {
            System.out.println("Connected to PostgreSQL database");
            System.out.println("--------------------------------");

            createTable(connection, sqlForRoles);
            createTable(connection, sqlForUsers);
            createTable(connection, sqlForUserRole);

            insertData(connection, "INSERT INTO roles (name) VALUES ('user'), ('admin'), ('owner'), ('guest')");

            createUser(connection);
            try {
                System.out.println(getUsersByRole(connection, "admin"));
                System.out.println(getRolesByUsers(connection, 15));
            } catch (SQLException e) {
                System.out.println("Неудачный SELECT: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Ошибка при подключении к бд: " + e.getMessage());
        }
    }



    private static void createTable(Connection connection, String sql) {
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("Таблица создана");
        } catch (SQLException e) {
            System.out.println("Ошибка при создании таблицы: " + e.getMessage());
        }
    }

    private static void insertData(Connection connection, String sql) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            System.out.println("Данные успешно добавлены в таблицу");
        } catch (SQLException e) {
            System.out.println("Ошибка при добавлении данных: " + e.getMessage());
        }
    }

    private static void createUser(Connection connection)  {
        try (Statement statement = connection.createStatement()) {
            for(int i=1; i<=20; i++ ) {
//                User user = new User("user" + i);
                ResultSet resultSet = statement.executeQuery("INSERT INTO users (username) VALUES ('user" + i + "') RETURNING id");

                if(resultSet.next()) {
                    System.out.println("Создан пользователь с id " + resultSet.getString("id"));

                    PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO user_role (user_id, role_id) VALUES (?, ?)");
                    // роль по умолчанию гость
                    preparedStatement.setInt(1, resultSet.getInt("id"));
                    preparedStatement.setInt(2, 4);
                    preparedStatement.executeUpdate();

                    if (resultSet.getInt("id") % 2 == 0) {
                        // роль юзер
                        preparedStatement.setInt(1, resultSet.getInt("id"));
                        preparedStatement.setInt(2, 1);
                        preparedStatement.executeUpdate();
                        System.out.println("Пользователь с id " + resultSet.getString("id") + " получил роль user");
                    }
                    if (resultSet.getInt("id") % 5 == 0) {
                        // роль owner
                        preparedStatement.setInt(1, resultSet.getInt("id"));
                        preparedStatement.setInt(2, 3);
                        preparedStatement.executeUpdate();
                        System.out.println("Пользователь с id " + resultSet.getString("id") + " получил роль owner");
                    }
                    if (resultSet.getInt("id") == 5 || resultSet.getInt("id") == 15) {
                        //роль admin
                        preparedStatement.setInt(1, resultSet.getInt("id"));
                        preparedStatement.setInt(2, 2);
                        preparedStatement.executeUpdate();
                        System.out.println("Пользователь с id " + resultSet.getString("id") + " получил роль admin");
                    }
                }

            }

        } catch (SQLException e) {
            System.out.println("Ошибка при создании пользователей: " + e.getMessage());
        }
    }

    private static List<User> getUsersByRole(Connection connection, String role) throws SQLException {
        List<User> users = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement("""
                SELECT users.id, users.username FROM users
                JOIN user_role ON users.id = user_role.user_id
                JOIN roles ON user_role.role_id = roles.id
                WHERE roles.name = ?""")) {

            preparedStatement.setString(1, role);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                users.add(new User(resultSet.getInt("id"), resultSet.getString("username")));
            }
            return users;
        }
    }

    private static List<Role> getRolesByUsers(Connection connection, int userId) throws SQLException {
        List<Role> roles = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement("""
                SELECT roles.id, roles.name FROM roles
                JOIN user_role ON roles.id = user_role.role_id
                JOIN users ON user_role.user_id = users.id
                WHERE users.id = ?""")) {

            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                roles.add(new Role(resultSet.getInt("id"), resultSet.getString("name")));
            }
            return roles;
        }
    }

    }
