package dh.backend.maxisoriano.ClinicaMVC.dao.implementacion;

import dh.backend.maxisoriano.ClinicaMVC.dao.IDao;
import dh.backend.maxisoriano.ClinicaMVC.db.H2Connection;
import dh.backend.maxisoriano.ClinicaMVC.model.Odontologo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class OdontologoIDaoH2 implements IDao<Odontologo> {
    private static Logger LOGGER = LoggerFactory.getLogger(OdontologoIDaoH2.class);
    private static String SQL_INSERT = "INSERT INTO ODONTOLOGOS VALUES (DEFAULT, ?, ?, ?);";
    private static String SQL_SELECT_ALL = "SELECT * FROM ODONTOLOGOS;";
    private static String SQL_SELECT_ID = "SELECT * FROM ODONTOLOGOS WHERE ID=?;";


    @Override
    public Odontologo registrar(Odontologo odontologo) {
        Connection connection = null;
        Odontologo odontologoPersistido = null;

        try{
            connection = H2Connection.getConnection();
            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, odontologo.getMatricula());
            preparedStatement.setString(2, odontologo.getNombre().toUpperCase());
            preparedStatement.setString(3, odontologo.getApellido().toUpperCase());

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            while (resultSet.next()){
                Integer id = resultSet.getInt(1);
                odontologoPersistido = new Odontologo(id, odontologo.getMatricula(), odontologo.getNombre(), odontologo.getApellido());
            }
            LOGGER.info("OONTOLOGO PERSISTIDO = "+ odontologoPersistido);

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

        return odontologoPersistido;
    }

    @Override
    public Odontologo buscarPorId(Integer id) {
        Odontologo odontologo = null;
        Connection connection = null;
        try{
            connection = H2Connection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                odontologo =  new Odontologo(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getString(3),
                        resultSet.getString(4)
                );
            }
            LOGGER.info("Odontologo encontrado "+ odontologo);
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
        return odontologo;
    }

    @Override
    public List<Odontologo> buscarTodos() {
        List<Odontologo> odontologos = new ArrayList<>();
        Connection connection = null;

        try{
            connection = H2Connection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL);

            while (resultSet.next()){
                Odontologo odontologo =  new Odontologo(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getString(3),
                        resultSet.getString(4)
                );
                LOGGER.info("Odontologo encontrado "+ odontologo);
                odontologos.add(odontologo);
            }

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

        return odontologos;
    }

}
