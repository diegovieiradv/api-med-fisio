package api.med.fisio.domain.endereco;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ViaCepService {

    private static final Logger logger = LoggerFactory.getLogger(ViaCepService.class);
    private static final String VIA_CEP_URL = "https://viacep.com.br/ws/{cep}/json/";

    public EnderecoViaCep buscarEnderecoPorCep(String cep) {
        try {
            var restTemplate = new RestTemplate();
            var url = VIA_CEP_URL.replace("{cep}", cep.replaceAll("[^0-9]", ""));
            var response = restTemplate.getForObject(url, EnderecoViaCep.class);

            if (response != null && response.erro() == null) {
                logger.info("CEP validado via ViaCEP: cep={}, logradouro={}, cidade={}/{}",
                        cep, response.logradouro(), response.localidade(), response.uf());
                return response;
            }
        } catch (Exception e) {
            logger.warn("Erro ao consultar ViaCEP para o CEP {}: {}", cep, e.getMessage());
        }

        return null;
    }

    public record EnderecoViaCep(
            String cep,
            String logradouro,
            String complemento,
            String bairro,
            String localidade,
            String uf,
            String erro
    ) {}
}
