package dev.markmburu.urlshortner.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Claim {
    private String name;
    private String value;
}
