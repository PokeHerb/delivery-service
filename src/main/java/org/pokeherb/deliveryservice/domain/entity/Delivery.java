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

    @Column(name = "delivery_driver_id")
    private UUID deliveryDriverId;

    @Column(name = "receiver_slack_id")
    private UUID receiverSlackId;

    @Column(name = "receiver_name")
    private String receiverName;

    @Column(name = "actual_duration_min")
    private Double actualDurationMin;

    @Column(name = "actual_duration_km")
    private Double actualDurationKm;

    @Column(name = "expected_duration_min")
    private Double expectedDurationMin;
    //finalDuration
    @Column(name = "expected_distance_km")
    private Double expectedDistanceKm;
    //finalDistance
    private UUID productId;

    @Column
    private LocalDateTime dueAt;

    @Column
    private UUID orderUserId;

    @Column
    private String productName;

    @Column
    private UUID driverId;

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
            Double actualDurationMin,
            Double actualDurationKm,
            Double expectedDurationMin,
            Double expectedDistanceKm,
            UUID productId,
            LocalDateTime dueAt,
            UUID orderUserId,
            String productName,
            UUID driverId
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
        this.expectedDurationMin = expectedDurationMin;
        this.expectedDistanceKm = expectedDistanceKm;
        this.productId = productId;
        this.dueAt = dueAt;
        this.orderUserId = orderUserId;
        this.productName = productName;
        this.driverId = driverId;
    }

    /* ============================================================
       Factory Method
     ============================================================ */

    public static Delivery create(DeliveryCreateCommand command) {
        return Delivery.builder()
                .orderId(command.orderId())
                .deliveryStatus(DeliveryStatus.CREATED)
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
        if (command.sequence() != null) {
            this.sequence = command.sequence();
        }
        if (command.startHubId() != null) {
            this.startHubId = command.startHubId();
        }
        if (command.endHubId() != null) {
            this.endHubId = command.endHubId();
        }
        if (command.endVendorId() != null) {
            this.endVendorId = command.endVendorId();
        }
        if (command.endVendorAddress() != null && !command.endVendorAddress().isBlank()) {
            this.endVendorAddress = command.endVendorAddress();
        }
        if (command.receiverSlackId() != null) {
            this.receiverSlackId = command.receiverSlackId();
        }
        if (command.receiverName() != null && !command.receiverName().isBlank()) {
            this.receiverName = command.receiverName();
        }
        if (command.expectedDurationMin() != null) {
            this.expectedDurationMin = command.expectedDurationMin();
        }
        if (command.expectedDistanceKm() != null) {
            this.expectedDistanceKm = command.expectedDistanceKm();
        }
        if (command.productId() != null) {
            this.productId = command.productId();
        }
        if (command.dueAt() != null) {
            this.dueAt = command.dueAt();
        }
        if (command.orderUserId() != null) {
            this.orderUserId = command.orderUserId();
        }
        if (command.productName() != null && !command.productName().isBlank()) {
            this.productName = command.productName();
        }
        if (command.driverId() != null) {
            this.driverId = command.driverId();
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
        this.updatedAt = command.changedAt() != null
                ? command.changedAt()
                : LocalDateTime.now();
    }

    public void complete(Double durationMin, Double distanceKm) {
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

