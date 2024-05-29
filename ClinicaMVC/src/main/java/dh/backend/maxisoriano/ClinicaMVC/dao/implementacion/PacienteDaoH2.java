package dh.backend.maxisoriano.ClinicaMVC.dao.implementacion;


import dh.backend.maxisoriano.ClinicaMVC.dao.IDao;
import dh.backend.maxisoriano.ClinicaMVC.db.H2Connection;
import dh.backend.maxisoriano.ClinicaMVC.model.Domicilio;
import dh.backend.maxisoriano.ClinicaMVC.model.Paciente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@Component
public class PacienteDaoH2 implements IDao<Paciente> {

    private static Logger LOGGER = LoggerFactory.getLogger(PacienteDaoH2.class);
    private static String SQL_INSERT = "INSERT INTO PACIENTES VALUES (DEFAULT,?,?,?,?,?)";

    private static String SQL_SELECT_ID = "SELECT * FROM PACIENTES WHERE ID = ?;";
    private static String SQL_SELECT_ALL = "SELECT * FROM PACIENTES;";

    @Override
    public Paciente registrar(Paciente paciente) {
        Connection connection = null;
        Paciente pacienteRetornar = null;
        DomicilioDaoH2 domicilioDaoH2 = new DomicilioDaoH2();
        try{
            connection = H2Connection.getConnection();
            connection.setAutoCommit(false);
            Domicilio domicilioGuardado = domicilioDaoH2.registrar(paciente.getDomicilio());


            PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, paciente.getApellido());
            preparedStatement.setString(2, paciente.getNombre());
            preparedStatement.setString(3, paciente.getDni());
            preparedStatement.setDate(4, Date.valueOf(paciente.getFechaIngreso()));
            preparedStatement.setInt(5, domicilioGuardado.getId());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()){
                Integer id = resultSet.getInt(1);
                pacienteRetornar = new Paciente(id, paciente.getApellido(), paciente.getNombre(), paciente.getDni(), paciente.getFechaIngreso(), domicilioGuardado);
            }
            LOGGER.info("Paciente Retornado: "+ pacienteRetornar);



            connection.commit();
            connection.setAutoCommit(true);
        }catch (Exception e){
            if(connection!=null){
                try{
                    connection.rollback();
                }catch (SQLException ex) {
                    LOGGER.error(ex.getMessage());
                    ex.printStackTrace();
                }
            }
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
        return pacienteRetornar;
    }

    @Override
    public Paciente buscarPorId(Integer id) {
        Connection connection = null;
        Paciente pacienteEncontrado = null;
        DomicilioDaoH2 domicilioDaoH2 = new DomicilioDaoH2();
        try{
            connection = H2Connection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ID);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Integer idDomicilio = resultSet.getInt(6);
                Domicilio domicilioEncontrado = domicilioDaoH2.buscarPorId(idDomicilio);
                pacienteEncontrado = new Paciente(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getDate(5).toLocalDate(),
                        domicilioEncontrado);
            }

            LOGGER.info("Paciente Encontrado = "+ pacienteEncontrado);

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
        return pacienteEncontrado;
    }

    @Override
    public List<Paciente> buscarTodos() {
        List<Paciente> pacientes = new ArrayList<>();
        Connection connection = null;
        DomicilioDaoH2 domicilioDaoH2 = new DomicilioDaoH2();
        try{
            connection = H2Connection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL);
            while (resultSet.next()){
                Integer idDomicilio = resultSet.getInt(6);
                Domicilio domicilioEncontrado = domicilioDaoH2.buscarPorId(idDomicilio);
                Paciente pacienteEncontrado = new Paciente(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getDate(5).toLocalDate(),
                        domicilioEncontrado);
                LOGGER.info("Paciente Encontrado = "+ pacienteEncontrado);
                pacientes.add(pacienteEncontrado);
            }


            LOGGER.info("Pacientes = "+ pacientes);
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
        return pacientes;
    }
}
