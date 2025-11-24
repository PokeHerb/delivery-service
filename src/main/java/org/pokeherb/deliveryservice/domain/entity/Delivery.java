package org.pokeherb.deliveryservice.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.pokeherb.deliveryservice.domain.command.DeliveryCreateCommand;
import org.pokeherb.deliveryservice.domain.command.DeliveryStatusUpdateCommand;
import org.pokeherb.deliveryservice.domain.command.DeliveryUpdateCommand;
import org.pokeherb.deliveryservice.domain.exception.DeliveryErrorCode;
import org.pokeherb.deliveryservice.global.domain.Auditable;
import org.pokeherb.deliveryservice.global.infrastructure.exception.CustomException;
import org.pokeherb.deliveryservice.infrastructure.persistence.LongListConverter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Access(AccessType.FIELD)
@Table(name = "p_delivery")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "delivery_id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Convert(converter = LongListConverter.class)
    @Column(name = "sequence")
    private List<Long> sequence;

    @Column(name = "start_hub_id")
    private Long startHubId;

    @Column(name = "end_hub_id")
    private Long endHubId;

    @Column(name = "end_vendor_id")
    private UUID endVendorId;

    @Column(name = "end_vendor_address")
    private String endVendorAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status", length = 30)
    private DeliveryStatus deliveryStatus;

    // ERD 오타(delivey_driver_id) → DB 컬럼 그대로 따라감
    @Column(name = "delivery_driver_id")
    private UUID deliveryDriverId;

    @Column(name = "receiver_slack_id")
    private UUID receiverSlackId;

    @Column(name = "receiver_name")
    private String receiverName;

    @Column(name = "actual_duration_min")
    private Integer actualDurationMin;

    @Column(name = "actual_duration_km")
    private Integer actualDurationKm;

    @Column(name = "deleted_by")
    private String deletedBy;

    @Builder
    private Delivery(
            UUID id,
            UUID orderId,
            List<Long> sequence,
            Long startHubId,
            Long endHubId,
            UUID endVendorId,
            String endVendorAddress,
            DeliveryStatus deliveryStatus,
            UUID deliveryDriverId,
            UUID receiverSlackId,
            String receiverName,
            Integer actualDurationMin,
            Integer actualDurationKm,
            String deletedBy
    ) {
        this.id = id;
        this.orderId = orderId;
        this.sequence = sequence;
        this.startHubId = startHubId;
        this.endHubId = endHubId;
        this.endVendorId = endVendorId;
        this.endVendorAddress = endVendorAddress;
        this.deliveryStatus = deliveryStatus != null ? deliveryStatus : DeliveryStatus.CREATED;
        this.deliveryDriverId = deliveryDriverId;
        this.receiverSlackId = receiverSlackId;
        this.receiverName = receiverName;
        this.actualDurationMin = actualDurationMin;
        this.actualDurationKm = actualDurationKm;
        this.deletedBy = deletedBy;
    }

    /* ============================================================
       Factory Method
     ============================================================ */

    public static Delivery create(DeliveryCreateCommand command) {
        return Delivery.builder()
                .orderId(command.orderId())
                .sequence(command.sequence())
                .startHubId(command.startHubId())
                .endHubId(command.endHubId())
                .endVendorId(command.endVendorId())
                .endVendorAddress(command.endVendorAddress())
                .deliveryStatus(DeliveryStatus.CREATED)
                .receiverName(command.receiverName())
                .receiverSlackId(command.receiverSlackId())
                .build();
    }

    /* ============================================================
       Domain Methods
     ============================================================ */
    public void update(DeliveryUpdateCommand command) {
        ensureNotDeleted();
        if (!this.deliveryStatus.isEditable()) {
            throw new CustomException(DeliveryErrorCode.CANNOT_UPDATE_DELIVERY);
        }

        if (command.receiverName() != null && !command.receiverName().isBlank()) {
            this.receiverName = command.receiverName();
        }
        if (command.receiverSlackId() != null) {
            this.receiverSlackId = command.receiverSlackId();
        }
        if (command.endVendorAddress() != null) {
            this.endVendorAddress = command.endVendorAddress();
        }

        this.updatedAt = LocalDateTime.now();
    }

    public void applyStatusUpdate(DeliveryStatusUpdateCommand command) {
        ensureNotDeleted();

        DeliveryStatus newStatus = command.newStatus();

        if (!this.deliveryStatus.canTransitionTo(newStatus)) {
            throw new CustomException(DeliveryErrorCode.INVALID_STATUS_TRANSITION);
        }

        this.deliveryStatus = newStatus;

        if (command.deliveryDriverId() != null) {
            this.deliveryDriverId = command.deliveryDriverId();
        }

        this.updatedAt = command.changedAt() != null
                ? command.changedAt()
                : LocalDateTime.now();
    }

    public void complete(Integer durationMin, Integer distanceKm) {
        ensureNotDeleted();

        if (!this.deliveryStatus.canComplete()) {
            throw new CustomException(DeliveryErrorCode.CANNOT_COMPLETE_DELIVERY);
        }

        this.deliveryStatus = DeliveryStatus.COMPLETED;
        this.actualDurationMin = durationMin;
        this.actualDurationKm = distanceKm;
        this.updatedAt = LocalDateTime.now();
    }

    public void delete(String username) {
        softDelete(username);
    }

    /* ============================================================
       Guards
     ============================================================ */

    private void ensureNotDeleted() {
        if (this.deletedAt != null) {
            throw new CustomException(DeliveryErrorCode.DELIVERY_ALREADY_DELETED);
        }
    }
}

