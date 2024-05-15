package tabelaFipe;

import model.AnosDTO;
import model.MarcaDTO;
import model.ModeloDTO;
import model.VeiculoDTO;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import service.Api;
import service.Converter;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@SpringBootApplication
public class TabelaFipeApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(TabelaFipeApplication.class, args);
	}

	Api api = new Api();
	Converter converter = new Converter();
	Scanner scanner = new Scanner(System.in);

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Escolha o tipo de veículo:");
		System.out.println("1 - Carros");
		System.out.println("2 - Motos");
		System.out.println("3 - Caminhões");
		int opcao = scanner.nextInt();

		String veiculo;
		switch (opcao) {
			case 1:
				veiculo = "carros";
				break;
			case 2:
				veiculo = "motos";
				break;
			case 3:
				veiculo = "caminhoes";
				break;
			default:
				System.out.println("Opção inválida.");
				return;
		}

		String url = "https://parallelum.com.br/fipe/api/v1/" + veiculo + "/marcas";

		String json = api.getJson(url);
		List<MarcaDTO> marcas = converter.stringToList(json, MarcaDTO.class);

		marcas.stream()
				.sorted(Comparator.comparing(marca -> marca.nome().toLowerCase()))
				.map(marca -> String.format("Código: %s, Marca: %s", marca.codigo(), marca.nome()))
				.forEach(System.out::println);

		System.out.println("Escolha o código da marca que deseja ver os modelos:");
		int marcaCodigo = scanner.nextInt();

		String urlModelos = String.format("%s/%d/modelos", url, marcaCodigo);

		String jsonModelos = api.getJson(urlModelos);
		List<ModeloDTO> modelos = converter.stringToList(jsonModelos, ModeloDTO.class, "modelos");

		modelos.stream()
				.sorted(Comparator.comparing(modelo -> modelo.nome().toLowerCase()))
				.map(modelo -> String.format("Código: %d, Modelo: %s", modelo.codigo(), modelo.nome()))
				.forEach(System.out::println);

		System.out.println("Escolha o código do modelo para ver os anos:");
		int modeloCodigo = scanner.nextInt();
		scanner.nextLine();

		String urlAnos = String.format("%s/%d/anos", urlModelos, modeloCodigo);

		String jsonAnos = api.getJson(urlAnos);
		List<AnosDTO> anos = converter.stringToList(jsonAnos, AnosDTO.class);

		anos.stream()
				.sorted(Comparator.comparing(AnosDTO::nome))
				.map(ano -> String.format("Código: %s, Modelo: %s", ano.codigo(), ano.nome()))
				.forEach(System.out::println);

		System.out.println("Escolha o código do ano para consultar o valor do veículo:");
		String anoCodigo = scanner.nextLine();

		String urlVeiculo = String.format("%s/%s", urlAnos, anoCodigo);

		String jsonVeiculo = api.getJson(urlVeiculo);
		VeiculoDTO dadosVeiculo = converter.stringToDTO(jsonVeiculo, VeiculoDTO.class);

        String sb = "Valor: " + dadosVeiculo.valor() + "\n" +
                "Marca: " + dadosVeiculo.marca() + "\n" +
                "Modelo: " + dadosVeiculo.modelo() + "\n" +
                "Ano do Modelo: " + dadosVeiculo.anoModelo().toString() + "\n" +
                "Combustível: " + dadosVeiculo.combustivel() + "\n" +
                "Mês de Referência: " + dadosVeiculo.mesReferencia();
		System.out.println("------------------------------------------------------------------");
		System.out.println(sb);
		System.out.println("------------------------------------------------------------------");
	}
}
