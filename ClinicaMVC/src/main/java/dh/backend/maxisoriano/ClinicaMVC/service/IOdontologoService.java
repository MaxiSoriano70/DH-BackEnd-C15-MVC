package dh.backend.maxisoriano.ClinicaMVC.service;

import dh.backend.maxisoriano.ClinicaMVC.model.Odontologo;
import dh.backend.maxisoriano.ClinicaMVC.model.Paciente;

import java.util.List;

public interface IOdontologoService {
    Odontologo crearOdontologo(Odontologo odontologo);

    Odontologo buscarPorId(Integer id);
    List<Odontologo> traerTodosLosOdontologos();
}
