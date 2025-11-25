package org.pokeherb.deliveryservice.infrastructure.persistence;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.pokeherb.deliveryservice.application.service.request.DeliverySearchConditionRequestDto;
import org.pokeherb.deliveryservice.application.service.response.DeliverySummaryResponseDto;
import org.pokeherb.deliveryservice.domain.repository.DeliveryQueryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import static org.pokeherb.deliveryservice.domain.entity.QDelivery.delivery;

@Repository
@RequiredArgsConstructor
public class DeliveryQueryDaoImpl implements DeliveryQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<DeliverySummaryResponseDto> search(DeliverySearchConditionRequestDto condition, Pageable pageable) {

        var builder = new BooleanBuilder();

        if (condition.deliveryId() != null)
            builder.and(delivery.id.eq(condition.deliveryId()));

        if (condition.orderId() != null)
            builder.and(delivery.orderId.eq(condition.orderId()));

        if (condition.status() != null)
            builder.and(delivery.deliveryStatus.eq(condition.status()));

        if (condition.receiverName() != null && !condition.receiverName().isBlank())
            builder.and(delivery.receiverName.containsIgnoreCase(condition.receiverName()));

        if (condition.receiverSlackId() != null)
            builder.and(delivery.receiverSlackId.eq(condition.receiverSlackId()));

        if (condition.deliveryDriverId() != null)
            builder.and(delivery.deliveryDriverId.eq(condition.deliveryDriverId()));

        // 소프트 삭제 제외
        builder.and(delivery.deletedAt.isNull());

        var result = queryFactory
                .selectFrom(delivery)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(DeliverySummaryResponseDto::from)
                .toList();

        var countQuery = queryFactory
                .select(delivery.count())
                .from(delivery)
                .where(builder);

        return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);
    }
}