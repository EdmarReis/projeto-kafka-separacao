package edmar.projeto.rest.kafka.service;

import edmar.projeto.rest.kafka.adapters.in.entity.PedidoEntity;
import edmar.projeto.rest.kafka.adapters.in.repository.PedidoRepository;
import edmar.projeto.rest.kafka.data.PedidoData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PedidoService {

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    Optional<PedidoEntity> pedidoEntity;

    public void mudarStatusSeparacao(PedidoData pedidoData){
        pedidoData.setSeparado(true);
    }

    public PedidoData verificaStatusAnterior(PedidoData pedidoData) {
        pedidoEntity = pedidoRepository.findById(pedidoData.getId());
        if(pedidoEntity.isEmpty()){
            return pedidoData;
        }else {
            PedidoEntity pedido = pedidoEntity.orElse(new PedidoEntity());
            //pedidoData.setSeparado(pedido.isSeparado());
            //pedidoData.setPago(pedido.isPago());
            return pedidoData;
        }
    }

}
