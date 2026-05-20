
package com.app.server;

import com.app.common.Message;
import java.sql.*;
import java.util.*;

public class ChatDB {
    private static final String URL  = "jdbc:mysql://localhost:3306/chatapp";
    private static final String USER = "root";
    private static final String PASS = "yourpassword";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static void saveMessage(Message msg) {
        try (Connection conn = getConnection()) {
            if (msg.getType() == Message.Type.TEXT) {
                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO messages (sender, type, content) VALUES (?, 'TEXT', ?)");
                stmt.setString(1, msg.getSender());
                stmt.setString(2, msg.getText());
                stmt.executeUpdate();

            } else if (msg.getType() == Message.Type.IMAGE) {
                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO messages (sender, type, image_data) VALUES (?, 'IMAGE', ?)");
                stmt.setString(1, msg.getSender());
                stmt.setBytes(2, msg.getImageData());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Message> getLast50() {
        List<Message> history = new ArrayList<>();
        try (Connection conn = getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT sender, type, content, image_data FROM messages ORDER BY sent_at DESC LIMIT 50");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                if ("TEXT".equals(rs.getString("type"))) {
                    history.add(new Message(Message.Type.TEXT,
                            rs.getString("sender"), rs.getString("content")));
                } else {
                    history.add(new Message(rs.getString("sender"), rs.getBytes("image_data")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Collections.reverse(history); // oldest first
        return history;
    }
}