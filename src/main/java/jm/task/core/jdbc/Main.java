package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // реализуйте алгоритм здесь

        UserService service = new UserServiceImpl();
        service.createUsersTable();
        service.saveUser("Max", "Lionov", (byte) 25);
        service.saveUser("Ivan", "Block", (byte) 44);
        service.saveUser("Helen", "Muromova", (byte) 18);
        service.saveUser("Alex", "Ya", (byte) 35);



        List<User> users = service.getAllUsers();

        for (User user : users) {
            System.out.println(user);
        }

//        service.removeUserById(4);
//        System.out.println();
//        users = service.getAllUsers();
//
//        for (User user : users) {
//            System.out.println(user);
//        }



        service.cleanUsersTable();
        service.dropUsersTable();
    }
}
