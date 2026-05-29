package com.minipos.pos.controller.cashier;

import com.minipos.pos.config.DatabaseConfig;
import com.minipos.pos.util.I18nUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import java.sql.*;

public class ActiveOrdersController {

    @FXML private ListView<String> ordersListView;
    @FXML private Button nextStatusButton;

    @FXML
    public void initialize() {
        updateLanguage();
        refreshOrdersList();
    }

    /**
     * Обновляем тексты кнопок при смене языка
     */
    private void updateLanguage() {
        nextStatusButton.setText(I18nUtil.get("btn.next.status"));
    }

    @FXML
    private void refreshOrdersList() {
        ordersListView.getItems().clear();
        String sql = "SELECT id, status, total_amount FROM orders WHERE status != 'DELIVERED' ORDER BY id ASC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                long id = rs.getLong("id");
                String status = rs.getString("status");
                double amount = rs.getDouble("total_amount");

                // Используем локализованные ключи для статусов
                String statusTranslated = I18nUtil.get("status." + status.toLowerCase());

                // Формируем строку: Order №X | Status: Y | Amount: Z
                String orderRow = String.format(I18nUtil.get("order.row.format"), id, statusTranslated, amount);
                ordersListView.getItems().add(orderRow);
            }
        } catch (SQLException e) {
            System.err.println(I18nUtil.get("error.db.refresh") + ": " + e.getMessage());
        }
    }

    @FXML
    private void advanceSelectedOrderStatus() {
        String selectedItem = ordersListView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) return;

        // Парсим ID из строки
        long orderId = Long.parseLong(selectedItem.split(" ")[1].replace("№", ""));

        String getCurrentStatusSql = "SELECT status FROM orders WHERE id = ?";
        String updateStatusSql = "UPDATE orders SET status = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(getCurrentStatusSql);
             PreparedStatement updateStmt = conn.prepareStatement(updateStatusSql)) {

            checkStmt.setLong(1, orderId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                String currentStatus = rs.getString("status");
                String nextStatus;

                switch (currentStatus) {
                    case "NEW": nextStatus = "PREPARING"; break;
                    case "PREPARING": nextStatus = "READY"; break;
                    case "READY": nextStatus = "DELIVERED"; break;
                    default: return;
                }

                updateStmt.setString(1, nextStatus);
                updateStmt.setLong(2, orderId);
                updateStmt.executeUpdate();

                refreshOrdersList();
            }
        } catch (SQLException e) {
            System.err.println(I18nUtil.get("error.status.update") + ": " + e.getMessage());
        }
    }
}