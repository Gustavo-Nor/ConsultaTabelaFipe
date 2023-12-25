package com.consultatabelafipe.ConsultaTabelaFipe.principal;

import com.consultatabelafipe.ConsultaTabelaFipe.model.Dados;
import com.consultatabelafipe.ConsultaTabelaFipe.model.Modelos;
import com.consultatabelafipe.ConsultaTabelaFipe.model.Veiculo;
import com.consultatabelafipe.ConsultaTabelaFipe.service.ConsumoApi;
import com.consultatabelafipe.ConsultaTabelaFipe.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://parallelum.com.br/fipe/api/v1/";

    public void exibirMenuInicial(){

        System.out.println("Voce deseja buscar na tabela FIPE valores de:\n" +
                "Carros\n" +
                "Motos\n" +
                "Caminhões");
        var tipoVeiculo = leitura.nextLine().toLowerCase();

        var json = consumo.obterDados(ENDERECO + tipoVeiculo + "/marcas");
        List<Dados> dadosMarcas = conversor.obterLista(json, Dados.class);

        dadosMarcas.stream()
                        .sorted(Comparator.comparing(Dados::nome))
                .forEach(e -> System.out.println(
                "Código: " + e.codigo() +
                        " Marca: " + e.nome()
        ));

        System.out.println("Digite o código da marca do veículo que voce deseja consultar: ");
        var codigoMarca = leitura.nextInt();
        leitura.nextLine();

        json = consumo.obterDados(ENDERECO + tipoVeiculo + "/marcas/" + codigoMarca + "/modelos");
        var dadosModelos = conversor.obterDados(json, Modelos.class);

        dadosModelos.modelos().stream()
                .sorted(Comparator.comparing(Dados::nome))
                .forEach(e -> System.out.println(
                        "Modelo: " + e.nome()
                ));

        System.out.println("Refine sua busca digitando o nome do modelo: ");
        var trechoModelo = leitura.nextLine();

        List<Dados> modelosBuscados = dadosModelos.modelos().stream()
                .filter(e -> e.nome().toUpperCase().contains(trechoModelo.toUpperCase()))
                .collect(Collectors.toList());

        modelosBuscados.forEach(System.out::println);

        System.out.println("Digite o código do modelo do veículo que voce deseja obter informações: ");
        var codigoModelo = leitura.nextInt();
        leitura.nextLine();

        json = consumo.obterDados(ENDERECO + tipoVeiculo + "/marcas/" + codigoMarca + "/modelos/" + codigoModelo + "/anos");
        List<Dados> anos = conversor.obterLista(json, Dados.class);
        List<Veiculo> informacoesVeiculos = new ArrayList<>();

        for (int i = 0; i < anos.size(); i++){
            json = consumo.obterDados(ENDERECO + tipoVeiculo + "/marcas/" + codigoMarca + "/modelos/" + codigoModelo + "/anos/" + anos.get(i).codigo());
            Veiculo dadosVeiculo = conversor.obterDados(json, Veiculo.class);
            informacoesVeiculos.add(dadosVeiculo);
        }
        informacoesVeiculos.forEach(e -> System.out.println(
                "Marca: " + e.marca() +
                        "| Modelo: " + e.modelo() +
                        "| Ano: " + e.anoModelo() +
                        "| Combustível: " + e.combustivel() +
                        "| Valor: " + e.valor()
        ));
    }
}
