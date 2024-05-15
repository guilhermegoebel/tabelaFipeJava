package model;

import com.fasterxml.jackson.annotation.JsonAlias;

public record AnosDTO(@JsonAlias("codigo") String codigo,
                      @JsonAlias("nome") String nome) {
}
