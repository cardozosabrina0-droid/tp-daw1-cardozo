package com.cardozo.canchas.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record ReservaRequest(
        @NotNull Long canchaId,
        @NotNull Long clienteId,
        @NotNull @Future LocalDateTime inicio,
        @NotNull @Min(1) @Max(3) Integer horas
) {
}
