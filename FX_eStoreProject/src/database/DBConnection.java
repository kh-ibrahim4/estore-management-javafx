package database;

import java.sql.*;


public class DBConnection {

    // 1. تعريف بيانات الاتصال
    static String className = "com.mysql.cj.jdbc.Driver";
    static String url = "jdbc:mysql://localhost:3306/estore_db?user=root&password=";
    static Connection con;
    static String db = "estore_db";

    /**
     * دالة للحصول على اتصال بقاعدة البيانات
     */
    public static Connection getConnection() throws SQLException {
        try {
            // 2. تحميل درايفر MySQL
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found", e);
        }

        // 3. إنشاء الاتصال
        con = DriverManager.getConnection(url);

        return con;
    }
}