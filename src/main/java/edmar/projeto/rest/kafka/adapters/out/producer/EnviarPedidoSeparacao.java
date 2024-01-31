package edmar.projeto.rest.kafka.adapters.out.producer;

import edmar.projeto.rest.kafka.data.PedidoData;
import edmar.projeto.rest.kafka.service.KafkaServices;
import edmar.projeto.rest.kafka.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EnviarPedidoSeparacao {

    private final KafkaServices kafkaServices;

    @Autowired
    public EnviarPedidoSeparacao(KafkaServices kafkaServices) {
        this.kafkaServices = kafkaServices;
    }

    @Autowired
    PedidoService pedidoService;

    public void metodoQueEnviaProdutoParaKafka(PedidoData pedidoData) {
        kafkaServices.enviarProdutoParaKafka(pedidoData);
    }



}
