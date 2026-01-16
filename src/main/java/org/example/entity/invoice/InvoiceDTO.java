import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record InvoiceDTO(
        UUID id,
        String number,
        LocalDateTime dueDate,
        String status,
        BigDecimal totalNetAmount,
        BigDecimal totalVatAmount,
        BigDecimal totalGrossAmount,
        List<org.example.dto.invoice.InvoiceItemDTO> items
) {}