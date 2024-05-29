package dh.backend.maxisoriano.ClinicaMVC.service;



import dh.backend.maxisoriano.ClinicaMVC.dao.IDao;
import dh.backend.maxisoriano.ClinicaMVC.model.Odontologo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OdontologoService implements IOdontologoService{
    private IDao<Odontologo> odontologoIDao;

    public OdontologoService(IDao<Odontologo> odontologoIDao) {
        this.odontologoIDao = odontologoIDao;
    }

    public Odontologo crearOdontologo(Odontologo odontologo){
        return odontologoIDao.registrar(odontologo);
    }

    @Override
    public Odontologo buscarPorId(Integer id) {
        return odontologoIDao.buscarPorId(id);
    }

    public List<Odontologo> traerTodosLosOdontologos(){
        return odontologoIDao.buscarTodos();
    }
}
