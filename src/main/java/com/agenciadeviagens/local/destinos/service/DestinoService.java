package com.agenciadeviagens.local.destinos.service;

import com.agenciadeviagens.global.exceptions.RecursoNaoEncontrado;
import com.agenciadeviagens.global.interfaces.InterfaceService;
import com.agenciadeviagens.local.destinos.dto.DestinoDTO;
import com.agenciadeviagens.local.destinos.mapper.DestinoMapper;
import com.agenciadeviagens.local.destinos.repository.DestinoRepository;
import com.agenciadeviagens.local.destinos.validation.DestinoException;
import com.agenciadeviagens.local.destinos.validation.DestinoValidation;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DestinoService implements InterfaceService<DestinoDTO> {

    private final DestinoRepository destinoRepository;
    private final DestinoMapper destinoMapper;
    public DestinoService(DestinoRepository destinoRepository, DestinoMapper destinoMapper) {
        this.destinoRepository = destinoRepository;
        this.destinoMapper = destinoMapper;
    }

    @Override
    public void salvar(DestinoDTO destinoDTO) {
        String capitalized = DestinoValidation.capitalizeTodasPalavras(destinoDTO.getNome());
        destinoDTO.setNome(capitalized);
        if (destinoDTO.getPais() == null) throw new DestinoException("Destino sem um Pais");
        try{
            destinoRepository.save(destinoMapper.map(destinoDTO));
        } catch(DataIntegrityViolationException e){
            throw new DestinoException("Ja existe uma destino com este nome");
        } catch (Exception e) {
            throw new DestinoException(e.getMessage());
        }
    }

    @Override
    public List<DestinoDTO> listarTodos() {
        return destinoRepository.findAll().stream()
                .map(destinoMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public DestinoDTO buscarPorId(Long id) {
        return destinoMapper.map(destinoRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontrado("Destino Nao Encontrado")));
    }

    @Override
    public void update(Long id, DestinoDTO destinoNovo) {
        //TODO
        DestinoDTO destinoExistente = buscarPorId(id);

    }

    @Override
    public void excluir(Long id) {
        buscarPorId(id);
        destinoRepository.deleteById(id);
    }
}
