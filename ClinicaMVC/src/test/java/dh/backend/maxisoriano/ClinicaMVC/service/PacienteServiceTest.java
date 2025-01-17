package dh.backend.maxisoriano.ClinicaMVC.service;

import dh.backend.maxisoriano.ClinicaMVC.dao.implementacion.PacienteDaoH2;
import dh.backend.maxisoriano.ClinicaMVC.model.Domicilio;
import dh.backend.maxisoriano.ClinicaMVC.model.Paciente;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PacienteServiceTest {
    public static Logger LOGGER = LoggerFactory.getLogger(PacienteServiceTest.class);
    public static PacienteService pacienteService = new PacienteService(new PacienteDaoH2());
    @BeforeAll
    static void crearTablas(){
        Connection connection = null;
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:~/clinicaMVC1;INIT=RUNSCRIPT from 'create.sql'","sa","sa");
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }finally {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.error(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Test
    @DisplayName("Testear que un paciente fue guardado")
    void testPacienteGuardado() {
        Paciente paciente = new Paciente("Cosme", "Menganito", "456464", LocalDate.of(2024, 04, 22),
                new Domicilio("Calle Falsa", 456, "Springfield", "Montana"));
        Paciente pacienteDesdeLaBD =  pacienteService.registrarPaciente(paciente);

        assertNotNull(pacienteDesdeLaBD);
    }

    @Test
    @DisplayName("Testear busqueda paciente por id")
    void testPacientePorId(){
        Integer id = 1;
        Paciente pacienteEncontrado = pacienteService.buscarPorId(id);

        assertEquals(id, pacienteEncontrado.getId());
    }

    @Test
    @DisplayName("Testear busqueda todos los pacientes")
    void testBusquedaTodos() {
        Paciente paciente = new Paciente("Cosme","Menganito","456464", LocalDate.of(2024,04,22),
                new Domicilio("Calle Falsa", 456, "Springfield","Montana"));

        pacienteService.registrarPaciente(paciente);

        List<Paciente> pacientes = pacienteService.buscarTodos();

        assertEquals(3, pacientes.size());

    }
}