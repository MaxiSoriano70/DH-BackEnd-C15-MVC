package dh.backend.maxisoriano.ClinicaMVC.service;

import dh.backend.maxisoriano.ClinicaMVC.model.Paciente;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IPacienteService {
    Paciente registrarPaciente(Paciente paciente);

    Paciente buscarPorId(Integer id);

    List<Paciente> buscarTodos();
}
