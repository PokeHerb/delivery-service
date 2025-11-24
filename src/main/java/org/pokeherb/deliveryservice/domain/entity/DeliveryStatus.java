package org.pokeherb.deliveryservice.domain.entity;

public enum DeliveryStatus {
    CREATED,
    ASSIGNED,
    PICKED_UP,
    IN_DELIVERY,
    COMPLETED,
    CANCELLED;

    public boolean canTransitionTo(DeliveryStatus target){
        if(this == target)
            return true;
        return switch (this){
            case CREATED -> target == ASSIGNED || target == CANCELLED;
            case ASSIGNED -> target == PICKED_UP || target == CANCELLED;
            case PICKED_UP -> target == IN_DELIVERY || target == CANCELLED;
            case IN_DELIVERY -> target == COMPLETED || target == CANCELLED;
            case COMPLETED, CANCELLED -> false; // 완료/취소된 주문은 변화 불가
        };
    }
    public boolean canComplete(){
        return this == IN_DELIVERY;
    }
    public boolean isEditable(){
        return this == CREATED || this == ASSIGNED;
    }
}
