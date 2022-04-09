package com.example.cityapi.dto.request;

import com.example.cityapi.enums.State;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CityDTO {

    private Long id;

    @NotBlank
    private String name;

    @Enumerated(EnumType.STRING)
    private State state;
}
