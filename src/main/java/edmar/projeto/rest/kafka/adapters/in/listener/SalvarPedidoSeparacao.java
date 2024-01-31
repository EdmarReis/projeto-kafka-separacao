package edmar.projeto.rest.kafka.adapters.in.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edmar.projeto.rest.kafka.adapters.in.entity.PedidoEntity;
import edmar.projeto.rest.kafka.adapters.in.lmapper.PedidoDataToPedidoEntity;
import edmar.projeto.rest.kafka.adapters.in.repository.PedidoRepository;
import edmar.projeto.rest.kafka.adapters.out.producer.EnviarPedidoSeparacao;
import edmar.projeto.rest.kafka.data.PedidoData;
import edmar.projeto.rest.kafka.service.PedidoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SalvarPedidoSeparacao {

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    PedidoEntity pedidoEntity;

    @Autowired
    PedidoDataToPedidoEntity pedidoToPedidoEntity;

    @Autowired
    PedidoService pedidoService;

    @Autowired
    EnviarPedidoSeparacao enviarPedidoSeparacao;

    @KafkaListener(topics = "SalvarPedidoSeparacao", groupId = "MicrosservicoSalvaPedido")
    private void executar(ConsumerRecord<String, String> record) throws InterruptedException {

        log.info("Inicio fluxo separacao");
        //log.info("Chave = {}", record.key());
        //log.info("Cabecalho = {}", record.headers());
        //log.info("Particao = {}", record.partition());

        String strDados = record.value();

        ObjectMapper mapper = new ObjectMapper();
        PedidoData pedidoData;

        try {
            pedidoData = mapper.readValue(strDados, PedidoData.class);
        } catch (JsonProcessingException ex) {
            log.error("Falha converter evento [dado={}}]", strDados, ex);
            return;
        }

        log.info("Evento Recebido no topico SalvarPedidoSeparacao = {}", pedidoData);
        pedidoService.mudarStatusSeparacao(pedidoData);
        log.info("Status de separacao alterado. Id {}, Status {} ",pedidoData.getId(),pedidoData.isSeparado());
        //pedidoService.verificaStatusAnterior(pedidoData);
        pedidoRepository.save(pedidoToPedidoEntity.toPedidoEntity(pedidoData));
        log.info("Evento Salvo no mongo (fluxo separacao). Id {}",pedidoData);
        enviarPedidoSeparacao.metodoQueEnviaProdutoParaKafka(pedidoData);
        log.info("Evento devolvido para topico devolvePedidoSeparacao. Id {}",pedidoData);

    }
}
