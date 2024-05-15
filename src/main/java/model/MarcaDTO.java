package model;

import com.fasterxml.jackson.annotation.JsonAlias;

public record MarcaDTO(@JsonAlias("codigo") String codigo,
                       @JsonAlias("nome") String nome) {
}
