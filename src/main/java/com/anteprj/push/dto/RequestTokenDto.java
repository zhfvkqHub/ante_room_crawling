package com.anteprj.push.dto;

import jakarta.validation.constraints.NotEmpty;

public record RequestTokenDto(
       @NotEmpty String token
) {
}
