package org.example;

import java.sql.*;
import java.time.LocalTime;
import java.util.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class Main {
    static void main() {
        try(Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/app_db",
                "dev_user",
                "dev_password")) {
            clearDatabase(connection);
            seedDatabase(connection);

            // Усі напої
            String query1 = "SELECT * FROM drinks";
            Statement statement1 = connection.createStatement();
            ResultSet rs1 = statement1.executeQuery(query1);
            while (rs1.next()){
                int id = rs1.getInt("id_drinks");
                String name_uk = rs1.getString("name_uk");
                String name_en = rs1.getString("name_en");
                int price = rs1.getInt("price");
                System.out.println(id + " " + name_uk + " " + name_en + " " + price);
            }

            // Усі десерти
            String query2 = "SELECT * FROM desserts";
            Statement statement2 = connection.createStatement();
            ResultSet rs2 = statement2.executeQuery(query2);
            while (rs2.next()){
                int id = rs2.getInt("id_desserts");
                String name_uk = rs2.getString("name_uk");
                String name_en = rs2.getString("name_en");
                int price = rs2.getInt("price");
                System.out.println(id + " " + name_uk + " " + name_en + " " + price);
            }

            //− Показати інформацію про всіх барист;
            String query3 = """
                SELECT s.id_staff, s.full_name, s.phone, s.address, p.name AS position_name
                FROM staff s
                JOIN positions p ON s.id_positions = p.id_positions
                WHERE p.name = 'Бариста'
                """;
            Statement statement3 = connection.createStatement();
            ResultSet rs3 = statement3.executeQuery(query3);
            while (rs3.next()) {
                int id = rs3.getInt("id_staff");
                String fullName = rs3.getString("full_name");
                String phone = rs3.getString("phone");
                String address = rs3.getString("address");
                String positionName = rs3.getString("position_name");

                System.out.println(id + " " + fullName + " " + phone + " " + address + " " + positionName);
            }

            //− Показати інформацію про всіх офіціантів;
            String query4 = """
                SELECT s.id_staff, s.full_name, s.phone, s.address, p.name AS position_name
                FROM staff s
                JOIN positions p ON s.id_positions = p.id_positions
                WHERE p.name = 'Офіціант'
                """;
            Statement statement4 = connection.createStatement();
            ResultSet rs4 = statement4.executeQuery(query4);
            while (rs4.next()) {
                int id = rs4.getInt("id_staff");
                String fullName = rs4.getString("full_name");
                String phone = rs4.getString("phone");
                String address = rs4.getString("address");
                String positionName = rs4.getString("position_name");

                System.out.println(id + " " + fullName + " " + phone + " " + address + " " + positionName);
            }

            //− Змінити ціну на окремий вид кави;
            String query5 = """
                    UPDATE drinks
                    SET price = ?
                    WHERE id_drinks = ?
                    """;
            int drinkID = 1;
            int newPrice = 150;
            PreparedStatement ps1 = connection.prepareStatement(query5);
            ps1.setInt(1, newPrice);
            ps1.setInt(2, drinkID);
            isSuccessfulUpdate(ps1.executeUpdate());


            //− Змінити контактний, поштовий адрес кондитера;
            String query6 = """
                    UPDATE staff
                    SET address = ?
                    WHERE id_staff = ?
                    """;
            String newAddress = "new address";
            int staffId1 = 4;
            PreparedStatement ps2 = connection.prepareStatement(query6);
            ps2.setString(1, newAddress);
            ps2.setInt(2, staffId1);
            isSuccessfulUpdate(ps2.executeUpdate());

            //− Змінити контактний телефон баристи;
            String query7 = """
                    UPDATE staff
                    SET phone = ?
                    WHERE id_staff = ?
                    """;
            String newPhone = "new phone";
            int staffId2 = 2;
            PreparedStatement ps3 = connection.prepareStatement(query7);
            ps3.setString(1, newPhone);
            ps3.setInt(2, staffId2);
            isSuccessfulUpdate(ps3.executeUpdate());

            //− Змінити відсоток знижки конкретного клієнта;
            String query8 = """
                    UPDATE clients
                    SET discount_percent = ?
                    WHERE id_clients = ?
                    """;
            double newDiscount = 0.5;
            int clientId1 = 1;
            PreparedStatement ps4 = connection.prepareStatement(query8);
            ps4.setDouble(1, newDiscount);
            ps4.setInt(2, clientId1);
            isSuccessfulUpdate(ps4.executeUpdate());

            //− Видалити інформацію про конкретний десерт;
            String query9 = """
                    DELETE FROM desserts
                    WHERE id_desserts = ?
                    """;
            int dessertIdToDelete = 7;
            PreparedStatement ps5 = connection.prepareStatement(query9);
            ps5.setInt(1, dessertIdToDelete);
            isSuccessfulUpdate(ps5.executeUpdate());

            //− Видалити інформацію про конкретного офіціанта по причині звільнення;
            String query10 = """
                    DELETE FROM staff
                    WHERE id_staff = ?
                    """;
            int waiterIdToDelete = 6;
            PreparedStatement ps6 = connection.prepareStatement(query10);
            ps6.setInt(1, waiterIdToDelete);
            isSuccessfulUpdate(ps6.executeUpdate());

            //− Видалити інформацію про конкретного баристи (звільнення);
            String query11 = """
                    DELETE FROM staff
                    WHERE id_staff = ?
                    """;
            int baristaIdToDelete = 7;
            PreparedStatement ps7 = connection.prepareStatement(query11);
            ps7.setInt(1, baristaIdToDelete);
            isSuccessfulUpdate(ps7.executeUpdate());

            //− Видалення інформації про конкретного клієнта;
            String query12 = """
                    DELETE FROM clients
                    WHERE id_clients = ?
                    """;
            int clientIdToDelete = 3;
            PreparedStatement ps8 = connection.prepareStatement(query12);
            ps8.setInt(1, clientIdToDelete);
            isSuccessfulUpdate(ps8.executeUpdate());

            //− Додавання інформації про новий вид кави;
            String query13 = """
                    INSERT INTO drinks (name_uk, name_en, price)
                    VALUES (?, ?, ?)
                    """;
            String newDrinkNameUk = "Раф кава";
            String newDrinkNameEn = "Raf Coffee";
            int newDrinkPrice = 80;

            PreparedStatement ps9 = connection.prepareStatement(query13);
            ps9.setString(1, newDrinkNameUk);
            ps9.setString(2, newDrinkNameEn);
            ps9.setInt(3, newDrinkPrice);
            isSuccessfulUpdate(ps9.executeUpdate());

            //− Додавання інформації про графік роботи в найближчий понеділок;
            LocalDate nextMonday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
            int staffIdMonday = 2;
            LocalTime mondayStartTime = LocalTime.of(8, 0);
            LocalTime mondayEndTime = LocalTime.of(17, 0);

            String query14 = """
                    INSERT INTO staff_schedules (id_staff, work_date, start_time, end_time)
                    VALUES (?, ?, ?, ?)
                    """;
            PreparedStatement ps10 = connection.prepareStatement(query14);
            ps10.setInt(1, staffIdMonday);
            ps10.setObject(2, nextMonday);
            ps10.setObject(3, mondayStartTime);
            ps10.setObject(4, mondayEndTime);
            isSuccessfulUpdate(ps10.executeUpdate());

            //− Змінити розклад роботи на найближчий вівторок;
            LocalDate nextTuesday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.TUESDAY));
            int staffIdTuesday = 2;
            LocalTime TuesdayStartTime = LocalTime.of(9, 0);
            LocalTime TuesdayEndTime = LocalTime.of(18, 0);
            String query15 = """
                    UPDATE staff_schedules
                    SET start_time = ?, end_time = ?
                    WHERE id_staff = ? AND work_date = ?
                    """;
            PreparedStatement ps11 = connection.prepareStatement(query15);
            ps11.setObject(1, TuesdayStartTime);
            ps11.setObject(2, TuesdayEndTime);
            ps11.setInt(3, staffIdTuesday);
            ps11.setObject(4, nextTuesday);
            isSuccessfulUpdate(ps11.executeUpdate());

            // Створення нового замовлення
            int newOrderClientId = 1;
            int newOrderStaffId = 2;
            int newOrderInitialTotal = 70;

            String createOrder = """
                    INSERT INTO orders (id_clients, id_staff, total_amount)
                    VALUES (?, ?, ?) RETURNING id_orders
                    """;
            PreparedStatement psCreateOrder = connection.prepareStatement(createOrder);
            psCreateOrder.setInt(1, newOrderClientId);
            psCreateOrder.setInt(2, newOrderStaffId);
            psCreateOrder.setInt(3, newOrderInitialTotal);
            ResultSet orderRs = psCreateOrder.executeQuery();
            int newOrderId = 0;
            if (orderRs.next()) {
                newOrderId = orderRs.getInt("id_orders");
            }

            int orderDrinkId = 3;
            int orderDrinkQuantity = 1;
            int orderDrinkPrice = 70;
            //− Додавання інформації про нове замовлення кави;
            String query16 = """
                    INSERT INTO order_drinks (id_orders, id_drinks, quantity, unit_price)
                    VALUES (?, ?, ?, ?)
                    """;
            PreparedStatement ps12 = connection.prepareStatement(query16);
            ps12.setInt(1, newOrderId);
            ps12.setInt(2, orderDrinkId);
            ps12.setInt(3, orderDrinkQuantity);
            ps12.setInt(4, orderDrinkPrice);
            isSuccessfulUpdate(ps12.executeUpdate());

            //− Додавання інформації про нове замовлення десерту;
            int orderDessertId = 2;
            int orderDessertQuantity = 1;
            int orderDessertPrice = 110;

            String query17 = """
                    INSERT INTO order_desserts (id_orders, id_desserts, quantity, unit_price)
                    VALUES (?, ?, ?, ?)
                    """;
            PreparedStatement ps13 = connection.prepareStatement(query17);
            ps13.setInt(1, newOrderId);
            ps13.setInt(2, orderDessertId);
            ps13.setInt(3, orderDessertQuantity);
            ps13.setInt(4, orderDessertPrice);
            isSuccessfulUpdate(ps13.executeUpdate());

            //− Змінити назву вже існуючого виду кави;
            String updatedDrinkNameUk = "Подвійний Еспресо";
            String updatedDrinkNameEn = "Doppio";
            int drinkIdToRename = 1;

            String query18 = """
                    UPDATE drinks
                    SET name_uk = ?, name_en = ?
                    WHERE id_drinks = ?
                    """;
            PreparedStatement ps14 = connection.prepareStatement(query18);
            ps14.setString(1, updatedDrinkNameUk);
            ps14.setString(2, updatedDrinkNameEn);
            ps14.setInt(3, drinkIdToRename);
            isSuccessfulUpdate(ps14.executeUpdate());

            //− Змінити назву вже існуючого десерту;
            String updatedDessertNameUk = "Чизкейк Сан-Себастьян";
            String updatedDessertNameEn = "San Sebastian Cheesecake";
            int dessertIdToRename = 1;

            String query19 = """
                    UPDATE desserts
                    SET name_uk = ?, name_en = ?
                    WHERE id_desserts = ?
                    """;
            PreparedStatement ps15 = connection.prepareStatement(query19);
            ps15.setString(1, updatedDessertNameUk);
            ps15.setString(2, updatedDessertNameEn);
            ps15.setInt(3, dessertIdToRename);
            isSuccessfulUpdate(ps15.executeUpdate());

            //− Змінити інформацію про існуюче замовлення;
            int updatedTotalAmount = 250;
            int orderIdToUpdate = newOrderId;

            String query20 = """
                    UPDATE orders
                    SET total_amount = ?
                    WHERE id_orders = ?
                    """;
            PreparedStatement ps16 = connection.prepareStatement(query20);
            ps16.setInt(1, updatedTotalAmount);
            ps16.setInt(2, orderIdToUpdate);
            isSuccessfulUpdate(ps16.executeUpdate());

            //− Видалити замовлення конкретного десерту;
            int orderDessertOrderTargetId = newOrderId;
            int orderDessertTargetId = 2;

            String query21 = """
                    DELETE FROM order_desserts
                    WHERE id_orders = ? AND id_desserts = ?
                    """;
            PreparedStatement ps17 = connection.prepareStatement(query21);
            ps17.setInt(1, orderDessertOrderTargetId);
            ps17.setInt(2, orderDessertTargetId);
            isSuccessfulUpdate(ps17.executeUpdate());

            //− Видалити конкретне замовлення;
            int orderIdToDelete = newOrderId;

            String query22 = """
                    DELETE FROM orders
                    WHERE id_orders = ?
                    """;
            PreparedStatement ps18 = connection.prepareStatement(query22);
            ps18.setInt(1, orderIdToDelete);
            isSuccessfulUpdate(ps18.executeUpdate());

            //− Видалити розклад роботи на конкретний день;
            LocalDate specificDayToDelete = LocalDate.of(2026, 9, 30);
            String query23 = """
                    DELETE FROM staff_schedules
                    WHERE work_date = ?
                    """;
            PreparedStatement ps19 = connection.prepareStatement(query23);
            ps19.setObject(1, specificDayToDelete);
            isSuccessfulUpdate(ps19.executeUpdate());

            //− Видалити розклад роботи між вказаними датами;
            LocalDate rangeStartDate = LocalDate.of(2026, 8, 1);
            LocalDate rangeEndDate = LocalDate.of(2026, 8, 30);

            String query24 = """
                    DELETE FROM staff_schedules
                    WHERE work_date BETWEEN ? AND ?
                    """;
            PreparedStatement ps20 = connection.prepareStatement(query24);
            ps20.setObject(1, rangeStartDate);
            ps20.setObject(2, rangeEndDate);
            isSuccessfulUpdate(ps20.executeUpdate());

            //− Показати всі замовлення конкретного десерту;
            int dessertFilterId = 1;
            System.out.println("Замовлення з десертом ID: " + dessertFilterId);

            String query25 = """
                    SELECT o.id_orders, o.order_date, od.quantity, od.unit_price
                    FROM orders o
                    JOIN order_desserts od ON o.id_orders = od.id_orders
                    WHERE od.id_desserts = ?
                    """;
            PreparedStatement ps21 = connection.prepareStatement(query25);
            ps21.setInt(1, dessertFilterId);
            ResultSet rsDessertOrders = ps21.executeQuery();
            while (rsDessertOrders.next()) {
                System.out.println("Замовлення № " + rsDessertOrders.getInt("id_orders") +
                        " | Дата: " + rsDessertOrders.getTimestamp("order_date") +
                        " | Кількість: " + rsDessertOrders.getInt("quantity") +
                        " | Ціна: " + rsDessertOrders.getInt("unit_price"));
            }

            //− Показати розклад роботи на вказаний день;
            LocalDate targetScheduleDate = nextTuesday;
            System.out.println("Розклад роботи на день: " + targetScheduleDate);

            String query26 = """
                    SELECT s.full_name, sc.start_time, sc.end_time
                    FROM staff_schedules sc
                    JOIN staff s ON sc.id_staff = s.id_staff
                    WHERE sc.work_date = ?
                    """;
            PreparedStatement ps22 = connection.prepareStatement(query26);
            ps22.setObject(1, targetScheduleDate);
            ResultSet rsSchedule = ps22.executeQuery();
            while (rsSchedule.next()) {
                System.out.println(rsSchedule.getString("full_name") + ": " +
                        rsSchedule.getTime("start_time") + " - " + rsSchedule.getTime("end_time"));
            }

            //− Показати всі замовлення обраного офіціанта;
            int targetStaffId = 5;
            System.out.println("Замовлення співробітника офіціанта з ID: " + targetStaffId);

            String query27 = """
                    SELECT id_orders, order_date, total_amount
                    FROM orders
                    WHERE id_staff = ?
                    """;
            PreparedStatement ps23 = connection.prepareStatement(query27);
            ps23.setInt(1, targetStaffId);
            ResultSet rsStaffOrders = ps23.executeQuery();
            while (rsStaffOrders.next()) {
                System.out.println("Замовлення № " + rsStaffOrders.getInt("id_orders") +
                        " | Сума: " + rsStaffOrders.getInt("total_amount") +
                        " | Дата: " + rsStaffOrders.getTimestamp("order_date"));
            }

            //− Показати всі замовлення обраного клієнта;
            int targetClientId = 1;
            System.out.println("Замовлення клієнта ID: " + targetClientId);

            String query28 = """
                    SELECT id_orders, order_date, total_amount
                    FROM orders
                    WHERE id_clients = ?
                    """;
            PreparedStatement ps24 = connection.prepareStatement(query28);
            ps24.setInt(1, targetClientId);
            ResultSet rsClientOrders = ps24.executeQuery();
            while (rsClientOrders.next()) {
                System.out.println("Замовлення № " + rsClientOrders.getInt("id_orders") +
                        " | Сума: " + rsClientOrders.getInt("total_amount") +
                        " | Дата: " + rsClientOrders.getTimestamp("order_date"));
            }


            // Lab 3

            // Показати 3 найулюбленіші напої за попередній місяць
            LocalDate firstDayPrevMonth = LocalDate.now().minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
            LocalDate firstDayCurrentMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
            String query29 = """
                    SELECT d.id_drinks, d.name_uk, d.name_en, SUM(od.quantity) AS total_sold
                    FROM drinks d
                    JOIN order_drinks od ON d.id_drinks = od.id_drinks
                    JOIN orders o ON od.id_orders = o.id_orders
                    WHERE o.order_date >= ? AND o.order_date < ?
                    GROUP BY d.id_drinks, d.name_uk, d.name_en
                    ORDER BY total_sold DESC
                    LIMIT 3
                    """;
            PreparedStatement ps25 = connection.prepareStatement(query29);
            ps25.setObject(1, firstDayPrevMonth);
            ps25.setObject(2, firstDayCurrentMonth);
            ResultSet rsFavoritesDrinks = ps25.executeQuery();
            while (rsFavoritesDrinks.next()) {
                System.out.println(rsFavoritesDrinks.getInt("id_drinks") +
                        " | " + rsFavoritesDrinks.getString("name_uk") +
                        " | " + rsFavoritesDrinks.getString("name_en") +
                        " | " + rsFavoritesDrinks.getInt("total_sold"));
            }

            // Показати 5 найулюбленіших десертів за попередні 10 днів
            LocalDate today = LocalDate.now();
            LocalDate prev10Day = today.minusDays(10);
            String query30 = """
                    SELECT d.id_desserts, d.name_uk, d.name_en, SUM(od.quantity) AS total_sold
                    FROM desserts d
                    JOIN order_desserts od ON d.id_desserts = od.id_desserts
                    JOIN orders o ON od.id_orders = o.id_orders
                    WHERE o.order_date >= ? AND o.order_date < ?
                    GROUP BY d.id_desserts, d.name_uk, d.name_en
                    ORDER BY total_sold DESC
                    LIMIT 5
                    """;
            PreparedStatement ps26 = connection.prepareStatement(query30);
            ps26.setObject(1, prev10Day);
            ps26.setObject(2, today);
            ResultSet rsFavoritesDesserts = ps26.executeQuery();
            while (rsFavoritesDesserts.next()) {
                System.out.println(rsFavoritesDesserts.getInt("id_desserts") +
                        " | " + rsFavoritesDesserts.getString("name_uk") +
                        " | " + rsFavoritesDesserts.getString("name_en") +
                        " | " + rsFavoritesDesserts.getInt("total_sold"));
            }

            // Підрахувати середню суму замовлення на конкретний день;
            LocalDate orderDate = LocalDate.of(2026, 8, 20);
            LocalDate nextDay = orderDate.plusDays(1);
            String query31 = """
                    SELECT AVG(o.total_amount) AS avg_total
                    FROM orders o
                    WHERE o.order_date >= ? AND o.order_date < ?
                    """;
            PreparedStatement ps27 = connection.prepareStatement(query31);
            ps27.setObject(1, orderDate);
            ps27.setObject(2, nextDay);
            ResultSet rsAVG = ps27.executeQuery();
            if (rsAVG.next()) {
                    System.out.printf("Середня сума замовлення за %s : %.2f грн%n", orderDate, rsAVG.getDouble("avg_total"));
            }

            // Показати інформацію про найбільше або найбільші(якщо таких замовлень декілька) замовлення на конкретну дату;
            LocalDate bOrderDate = LocalDate.of(2026, 8, 20);
            LocalDate bNextDay = bOrderDate.plusDays(1);
            String query32 = """
                    SELECT o.id_orders, o.order_date, o.total_amount
                    FROM orders o
                    WHERE o.order_date >= ? AND o.order_date < ?
                    AND o.total_amount = (
                        SELECT MAX(sub_o.total_amount)
                        FROM orders sub_o
                        WHERE sub_o.order_date >= ? AND sub_o.order_date < ?
                        )
                    ORDER BY o.id_orders
                    """;
            PreparedStatement ps28 = connection.prepareStatement(query32);
            ps28.setObject(1, bOrderDate);
            ps28.setObject(2, bNextDay);
            ps28.setObject(3, bOrderDate);
            ps28.setObject(4, bNextDay);
            ResultSet rsOrders = ps28.executeQuery();
            while (rsOrders.next()) {
                System.out.printf("Замовлення #%d на дату %s: сума %.2f грн%n", rsOrders.getInt("id_orders"), bOrderDate, rsOrders.getDouble("total_amount"));
            }


            // Показати інформацію про постійних клієнтів
            LocalDate weekAgo = today.minusDays(7);
            String query33 = """
                    SELECT c.id_clients, c.full_name, c.phone, COUNT(o.id_orders) AS orders_count
                    FROM clients c
                    JOIN orders o ON c.id_clients = o.id_clients
                    WHERE o.order_date >= ? AND o.order_date < ?
                    GROUP BY c.id_clients, c.full_name, c.phone
                    HAVING COUNT(o.id_orders) >= 3
                    ORDER BY orders_count DESC
                    """;
            PreparedStatement ps29 = connection.prepareStatement(query33);
            ps29.setObject(1, weekAgo);
            ps29.setObject(2, today);
            ResultSet rsClients = ps29.executeQuery();
            while (rsClients.next()) {
                int clientId = rsClients.getInt("id_clients");
                String fullName = rsClients.getString("full_name");
                String phone = rsClients.getString("phone");
                int ordersCount = rsClients.getInt("orders_count");

                System.out.printf("Клієнт #%d: %s | Тел: %s | Замовлень за тиждень: %d%n",
                        clientId, fullName, phone, ordersCount);
            }

            // Показати розклад роботи для усіх працівників кав’ярні сьогодні/завтра/довільна дата.
            String query34 = """
                    SELECT s.id_staff, s.full_name, ss.work_date, ss.start_time, ss.end_time
                    FROM staff s
                    JOIN staff_schedules ss ON s.id_staff = ss.id_staff
                    WHERE ss.work_date = ?
                    ORDER BY ss.start_time
                    """;
            PreparedStatement ps30 = connection.prepareStatement(query34);
            ps30.setObject(1, today);

            ResultSet rsS = ps30.executeQuery();
            while (rsS.next()) {
                int idStaff = rsS.getInt("id_staff");
                String fullName = rsS.getString("full_name");
                LocalTime startTime = rsS.getObject("start_time", LocalTime.class);
                LocalTime endTime = rsS.getObject("end_time", LocalTime.class);
                System.out.printf("[#%d] %s | %s Зміна: %s - %s%n",
                        idStaff, fullName, today, startTime, endTime);
            }
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database: " + e.getMessage());
            // Усі виклики, що призвели до вийнятка
            e.printStackTrace();
        }
    }


    public static void isSuccessfulUpdate(int rowsUpdated)
    {
        if (rowsUpdated > 0) {
            System.out.println("Операцію успішно виконано (рядків: " + rowsUpdated + ")");
        } else {
            System.out.println("Проблема внесення змін");
        }
    }

    // Повне очищення БД зі скиданням лічильників ID до 1
    public static void clearDatabase(Connection connection) throws SQLException {
        String truncateSql = """
            TRUNCATE TABLE
                order_drinks,
                order_desserts,
                orders,
                staff_schedules,
                clients,
                staff,
                desserts,
                drinks,
                positions
            RESTART IDENTITY CASCADE;
        """;

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(truncateSql);
            System.out.println("Базу даних повністю очищено, лічильники ID скинуто!");
        }
    }

    // Наповнення бази узгодженими тестовими даними
    public static void seedDatabase(Connection connection) throws SQLException {
        LocalDate today = LocalDate.now();
        LocalDate nextTuesday = today.with(TemporalAdjusters.next(DayOfWeek.TUESDAY));

        // Дати для замовлень минулого тижня / останніх 10 днів (усі суворо ДО настання today 00:00:00)
        LocalDate d1 = today.minusDays(1); // Вчора
        LocalDate d2 = today.minusDays(2); // 2 дні тому
        LocalDate d3 = today.minusDays(3); // 3 дні тому
        LocalDate d4 = today.minusDays(4); // 4 дні тому
        LocalDate d5 = today.minusDays(5); // 5 днів тому

        String seedSql = """
        -- 1. Посади
        INSERT INTO positions (name) VALUES
            ('Бариста'),
            ('Офіціант'),
            ('Кондитер');

        -- 2. Напої (5 видів)
        INSERT INTO drinks (name_uk, name_en, price) VALUES 
            ('Еспресо', 'Espresso', 45),
            ('Капучино', 'Cappuccino', 65),
            ('Лате', 'Latte', 70),
            ('Флет Вайт', 'Flat White', 85),
            ('Американо', 'Americano', 50);

        -- 3. Десерти (щонайменше 6 видів, щоб у query30 сформувався повноцінний ТОП-5)
        INSERT INTO desserts (name_uk, name_en, price) VALUES 
            ('Чизкейк Нью-Йорк', 'Cheesecake New York', 95),
            ('Тірамісу', 'Tiramisu', 110),
            ('Круасан класичний', 'Classic Croissant', 55),
            ('Макарон шоколадний', 'Chocolate Macaron', 45),
            ('Еклер ванільний', 'Vanilla Eclair', 60),
            ('Брауні з горіхом', 'Nut Brownie', 75),
            ('Десерт на видалення', 'To Delete', 30);

        -- 4. Персонал
        INSERT INTO staff (full_name, phone, address, id_positions) VALUES 
            ('Олександр Мельник', '+380501112233', 'вул. Соборна, 12', 1),   -- 1: Бариста
            ('Анна Ковальчук', '+380672223344', 'вул. Київська, 45', 1),      -- 2: Бариста
            ('Максим Бондаренко', '+380933334455', 'просп. Миру, 8', 2),      -- 3: Офіціант
            ('Олена Шевченко', '+380994445566', 'вул. Шевченка, 20', 3),     -- 4: Кондитер
            ('Дмитро Кравченко', '+380951234567', 'просп. Героїв, 15', 2),   -- 5: Офіціант
            ('Тимчасовий Офіціант', '+380000000001', 'вул. Тестова, 1', 2),  -- 6: для тесту DELETE
            ('Тимчасовий Бариста', '+380000000002', 'вул. Тестова, 2', 1);   -- 7: для тесту DELETE

        -- 5. Графік роботи (є розклад на today для query34!)
        INSERT INTO staff_schedules (id_staff, work_date, start_time, end_time) VALUES 
            (1, '%1$s', '08:00', '16:00'),                                  -- today
            (2, '%1$s', '14:00', '22:00'),                                  -- today
            (3, '%1$s', '09:00', '18:00'),                                  -- today
            (2, '%2$s', '08:00', '16:00'),                                  -- nextTuesday
            (4, '2026-08-10', '08:00', '17:00'),                           -- для тесту DELETE BETWEEN
            (1, '2026-09-30', '09:00', '18:00');                           -- для тесту DELETE конкретного дня

        -- 6. Клієнти (поле full_name узгоджено з query33)
        INSERT INTO clients (full_name, birth_date, phone, address, discount_percent) VALUES 
            ('Іван Петренко', '1995-04-12', '+380509998877', 'вул. Лесі Українки, 5', 5.00),
            ('Марія Сидоренко', '2001-11-23', '+380678887766', 'вул. Незалежності, 18', 10.00),
            ('Тестовий Клієнт', '1990-01-01', '+380000000000', 'вул. Тестова, 99', 0.00); -- 3: для тесту DELETE

        -- 7. Замовлення
        INSERT INTO orders (id_clients, id_staff, order_date, total_amount) VALUES 
            -- Серпень 2026 (для ТОП-3 напоїв, AVG за 20-те число та однакового рекорду по 210 грн):
            (1, 3, '2026-08-15 11:00:00', 130),   -- 1
            (1, 5, '2026-08-18 10:00:00', 140),   -- 2
            (2, 1, '2026-08-20 12:00:00', 210),   -- 3 (рекорд на дату 20-го)
            (1, 3, '2026-08-20 16:30:00', 210),   -- 4 (ще один такий самий рекорд на 20-те!)
            (2, 5, '2026-08-20 18:00:00', 90),    -- 5 (для розрахунку середнього чека)

            -- Останні 10 днів (усі замовлення суворо ДО сьогодні, тому не відсікаються):
            -- Клієнт 1 робить 3 замовлення за тиждень (d1, d2, d3) -> виконається HAVING COUNT >= 3
            (1, 3, '%3$s 10:15:00', 160),         -- 6: Клієнт 1 (d1 - вчора)
            (1, 5, '%4$s 14:00:00', 130),         -- 7: Клієнт 1 (d2 - 2 дні тому)
            (1, 3, '%5$s 17:30:00', 220),         -- 8: Клієнт 1 (d3 - 3 дні тому)
            -- Замовлення інших клієнтів (для формування продажів 5 різних десертів):
            (2, 1, '%6$s 12:30:00', 165),         -- 9: Клієнт 2 (d4 - 4 дні тому)
            (2, 3, '%7$s 15:00:00', 110);         -- 10: Клієнт 2 (d5 - 5 днів тому)

        -- 8. Склад замовлень: Напої
        INSERT INTO order_drinks (id_orders, id_drinks, quantity, unit_price) VALUES 
            (1, 1, 3, 45),  -- 3 шт Еспресо
            (2, 4, 2, 85),  -- 2 шт Флет Вайт
            (3, 2, 3, 65),  -- 3 шт Капучино
            (4, 3, 3, 70),  -- 3 шт Лате
            (5, 5, 2, 50),  -- 2 шт Американо
            (6, 2, 1, 65),
            (7, 2, 2, 65),
            (8, 3, 2, 70),
            (9, 3, 1, 70),
            (10, 1, 1, 45);

        -- 9. Склад замовлень: Десерти (5 різних позицій за останні 10 днів)
        INSERT INTO order_desserts (id_orders, id_desserts, quantity, unit_price) VALUES 
            (6, 1, 4, 95),  -- Чизкейк (4 шт - 1 місце)
            (7, 2, 3, 110), -- Тірамісу (3 шт - 2 місце)
            (8, 5, 2, 60),  -- Еклер (2 шт - 3 місце)
            (9, 6, 2, 75),  -- Брауні (2 шт - 4 місце)
            (6, 4, 1, 45),  -- Макарон (1 шт - 5 місце)
            (10, 3, 1, 55), -- Круасан (1 шт)
            (3, 1, 1, 95);  -- Серпневий десерт
    """.formatted(
                today,        // %1$s
                nextTuesday,  // %2$s
                d1,           // %3$s (today - 1)
                d2,           // %4$s (today - 2)
                d3,           // %5$s (today - 3)
                d4,           // %6$s (today - 4)
                d5            // %7$s (today - 5)
        );

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(seedSql);
            System.out.println("Тестові дані успішно записані в базу даних!");
        }
    }
}

