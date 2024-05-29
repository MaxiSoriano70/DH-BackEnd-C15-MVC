package dh.backend.maxisoriano.ClinicaMVC.controller;

import dh.backend.maxisoriano.ClinicaMVC.model.Odontologo;
import dh.backend.maxisoriano.ClinicaMVC.model.Paciente;
import dh.backend.maxisoriano.ClinicaMVC.service.IOdontologoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OdontologoController {
    private IOdontologoService iOdontologoService;

    public OdontologoController(IOdontologoService iOdontologoService) {
        this.iOdontologoService = iOdontologoService;
    }

    @GetMapping("/buscarOdontologo")
    public String buscarPorId(Model model, @RequestParam Integer id){
        Odontologo odontologo = iOdontologoService.buscarPorId(id);

        model.addAttribute("nombre", odontologo.getNombre());
        model.addAttribute("apellido", odontologo.getApellido());
        return "index";
    }
}
