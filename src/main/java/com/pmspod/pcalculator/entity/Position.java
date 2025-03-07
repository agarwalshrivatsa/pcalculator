package com.pmspod.pcalculator.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity(name="position_history")
public class Position {

    @Column(name = "ticker")
    private String ticker;

    @Column(name = "total_qty")
    private String totalQty;

    @Column(name = "avg_price")
    private String avgPrice;

    @Column(name = "currency")
    private String currency;

//    @ManyToOne
    @JoinColumn(name = "account_id")
    private String accountId;

    @Column(name = "daily_pnl")
    private String dailyPnl;

    @Column(name = "ytd_pnl")
    private String ytdPnl;

    @Column(name = "itd_pnl")
    private String itdPnl;

    @Id
    @Column(name = "position_id")
    private String positionId;

}