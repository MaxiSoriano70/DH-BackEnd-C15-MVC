package dh.backend.maxisoriano.ClinicaMVC.dao.implementacion;

import dh.backend.maxisoriano.ClinicaMVC.dao.IDao;
import dh.backend.maxisoriano.ClinicaMVC.db.H2Connection;
import dh.backend.maxisoriano.ClinicaMVC.model.Domicilio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

public class DomicilioDaoH2 implements IDao<Domicilio> {
    private static Logger LOGGER = LoggerFactory.getLogger(DomicilioDaoH2.class);

    private static String SQL_INSERT = "INSERT INTO DOMICILIOS VALUES (DEFAULT, ?, ?, ?, ?);";

    private static String SQL_SELECT_ID = "SELECT * FROM DOMICILIOS WHERE ID=?;";
    @Override
    public Domicilio registrar(Domicilio domicilio) {
        Connection connection = null;
        Domicilio domicilioRetornar = null;
        try{
            connection = H2Connection.getConnection();
            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, domicilio.getCalle());
            preparedStatement.setInt(2, domicilio.getNumero());
            preparedStatement.setString(3, domicilio.getLocalidad());
            preparedStatement.setString(4, domicilio.getProvincia());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()){
                Integer id = resultSet.getInt(1);
                domicilioRetornar = new Domicilio(id, domicilio.getCalle(), domicilio.getNumero(), domicilio.getLocalidad(), domicilio.getProvincia());
                LOGGER.info("Domicilio persistido: "+ domicilioRetornar);
            }


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
        return domicilioRetornar;
    }

    @Override
    public Domicilio buscarPorId(Integer id) {
        Connection connection = null;
        Domicilio domicilioEncontrado = null;
        try{
            connection = H2Connection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                domicilioEncontrado = new Domicilio(resultSet.getInt(1),resultSet.getNString(2), resultSet.getInt(3), resultSet.getNString(4), resultSet.getNString(5));
            }
            LOGGER.info("Domicilio encontrado: "+domicilioEncontrado);

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
        return domicilioEncontrado;
    }

    @Override
    public List<Domicilio> buscarTodos() {
        return null;
    }
}
