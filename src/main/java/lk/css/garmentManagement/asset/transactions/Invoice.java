package lk.css.garmentManagement.asset.transactions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lk.css.garmentManagement.security.entity.User;
import lk.css.garmentManagement.asset.transactions.Enum.InvoicePrintOrNot;
import lk.css.garmentManagement.asset.transactions.Enum.PaymentMethod;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(value = {"createdAt", "updatedAt","balance","discountAmount","bankName","cardNumber"}, allowGetters = true)
public class Invoice {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number", nullable = false, unique = true)
    private Long number;

    @Column(name = "payment_method", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;


    @Column(name = "totalPrice", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;


    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "discountAmount",  precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "amountTendered", precision = 10, scale = 2)
    private BigDecimal amountTendered;

    @Column(name = "balance", precision = 10, scale = 2)
    private BigDecimal balance;

    @Column(name = "bank_name")
    private String bankName;


    @Column(name = "card_number")
    private Long cardNumber;


    @Column(name = "remarks", length = 150)
    private String remarks;

    @Enumerated(EnumType.STRING)
    private InvoicePrintOrNot invoicePrintOrNot;

    @Column(name = "created_at", nullable = false, updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdAt;

    @Column(nullable = false, updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime invoicedAt;



    @ManyToOne
    private DiscountRatio discountRatio;

    @ManyToOne
    private User user;



/*
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "invoice_id")
    private List<> invoiceHasLabTests = new ArrayList<>();*/


}
