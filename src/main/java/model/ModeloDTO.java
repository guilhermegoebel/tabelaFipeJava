package model;

import com.fasterxml.jackson.annotation.JsonAlias;

public record ModeloDTO(@JsonAlias("codigo") Integer codigo,
                        @JsonAlias("nome") String nome) {
}
