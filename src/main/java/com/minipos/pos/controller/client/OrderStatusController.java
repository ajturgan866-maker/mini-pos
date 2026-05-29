package com.minipos.pos.controller.client;

import com.minipos.pos.model.Order;
import com.minipos.pos.service.OrderService;
import com.minipos.pos.util.I18nUtil;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.List;

public class OrderStatusController {

    @FXML private Label titleLabel;
    @FXML private Label statusLabel;
    @FXML private Label orderNumberLabel;

    private final OrderService orderService = new OrderService();
    private long currentOrderId = -1; // ID заказа, который мы отслеживаем

    @FXML
    public void initialize() {
        updateLanguage();

        // Запускаем таймер: обновление статуса каждые 3 секунды
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> checkOrderStatus()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void checkOrderStatus() {
        if (currentOrderId == -1) return;

        // Получаем все активные заказы
        List<Order> activeOrders = orderService.getActiveOrders();

        // Ищем наш заказ в списке
        for (Order order : activeOrders) {
            if (order.getId() == currentOrderId) {
                updateStatus(order.getStatus(), order.getId());
                return;
            }
        }
        // Если заказ не найден в активных, можно показать статус "Выдан"
        statusLabel.setText(I18nUtil.get("status.completed"));
    }

    public void updateLanguage() {
        titleLabel.setText(I18nUtil.get("status.order.title"));
    }

    public void updateStatus(String status, long orderId) {
        this.currentOrderId = orderId;
        orderNumberLabel.setText(I18nUtil.get("order.number") + " #" + orderId);

        // Ключи в properties: status.pending, status.preparing, status.ready
        String key = "status." + status.toLowerCase();
        statusLabel.setText(I18nUtil.get(key));
    }
}