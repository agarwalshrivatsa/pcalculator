package com.pmspod.pcalculator.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne
    @JoinColumn(name = "account_id")
    private String accountId;

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

}
