package com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.exception;

public class ApprovalNotFoundException extends RuntimeException{
    public ApprovalNotFoundException(String message) {
        super(message);
    }
}
