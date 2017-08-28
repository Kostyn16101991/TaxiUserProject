package com.example.vmm408.taxiuserproject.models;

public class OrderModel {
    private String idUserOrder;
    private String fromOrder;
    private String destinationOrder;
    private String priceOrder;
    private String commentOrder;
    private String timeOrder;
    private String orderAcceptedDriver;

    public void setIdUserOrder(String idUserOrder) {
        this.idUserOrder = idUserOrder;
    }

    public String getIdUserOrder() {
        return idUserOrder;
    }

    public void setFromOrder(String fromOrder) {
        this.fromOrder = fromOrder;
    }

    public String getFromOrder() {
        return fromOrder;
    }

    public void setDestinationOrder(String destinationOrder) {
        this.destinationOrder = destinationOrder;
    }

    public String getDestinationOrder() {
        return destinationOrder;
    }

    public void setPriceOrder(String priceOrder) {
        this.priceOrder = priceOrder;
    }

    public String getPriceOrder() {
        return priceOrder;
    }

    public void setCommentOrder(String commentOrder) {
        this.commentOrder = commentOrder;
    }

    public String getCommentOrder() {
        return commentOrder;
    }

    public void setTimeOrder(String timeOrder) {
        this.timeOrder = timeOrder;
    }

    public String getTimeOrder() {
        return timeOrder;
    }

    public void setOrderAcceptedDriver(String orderAcceptedDriver) {
        this.orderAcceptedDriver = orderAcceptedDriver;
    }

    public String getOrderAcceptedDriver() {
        return orderAcceptedDriver;
    }

    @Override
    public String toString() {
        return "OrderModel{" +
                "idUserOrder='" + idUserOrder + '\'' +
                ", fromOrder='" + fromOrder + '\'' +
                ", destinationOrder='" + destinationOrder + '\'' +
                ", priceOrder=" + priceOrder +
                ", commentOrder='" + commentOrder + '\'' +
                ", timeOrder=" + timeOrder +
                ", orderAcceptedDriver=" + orderAcceptedDriver +
                '}';
    }

    public static class CurrentOrder {
        private static OrderModel orderModel;

        public static void setOrderModel(OrderModel orderModel) {
            CurrentOrder.orderModel = orderModel;
        }

        public static OrderModel getOrderModel() {
            return orderModel;
        }
    }
}
