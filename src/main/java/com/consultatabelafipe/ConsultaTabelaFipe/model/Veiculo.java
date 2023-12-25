package com.consultatabelafipe.ConsultaTabelaFipe.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Veiculo(Integer TipoVeiculo,
                      String Valor,
                      String Marca,
                      String Modelo,
                      Integer AnoModelo,
                      String Combustivel,
                      String CodigoFipe,
                      String MesReferencia,
                      String SiglaCombustivel) {
}
