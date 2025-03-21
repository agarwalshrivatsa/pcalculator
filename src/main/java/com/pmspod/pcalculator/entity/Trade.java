package com.pmspod.pcalculator.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Data
public class Trade {

    @Column(name = "external_order_id", unique = true)
    private String externalOrderId;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "trade_date")
    private String tradeDate;

    @Column(name = "ticker")
    private String ticker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "order_type")
    private String orderType;

    @Column(name = "quantity")
    private String quantity;

    @Column(name = "price")
    private String price;

    @Column(name = "currency")
    private String currency;

    @Column(name = "exchange")
    private String exchange;

    @Column(name = "position_id")
    private UUID positionId;

}
