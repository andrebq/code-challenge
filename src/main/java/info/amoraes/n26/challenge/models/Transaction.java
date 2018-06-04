package info.amoraes.n26.challenge.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Valid
public class Transaction implements Serializable {

    @NotNull
    private Double amount;

    @NotNull
    private Long timestamp;
}
