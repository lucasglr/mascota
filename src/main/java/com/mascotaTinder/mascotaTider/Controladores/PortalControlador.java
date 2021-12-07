package com.mascotaTinder.mascotaTider.Controladores;

import com.mascotaTinder.mascotaTider.Entidades.Zona;
import com.mascotaTinder.mascotaTider.Errores.ErroresServicios;
import com.mascotaTinder.mascotaTider.Repositorios.ZonaRepositorio;
import com.mascotaTinder.mascotaTider.Servicios.UsuarioServicio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/") //URL QUE VA ESCUCHAR EL CONTROLADOR 
public class PortalControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private ZonaRepositorio zonaRepositorio;

    @GetMapping("/")
    public String index() {
        return "index.html";
    }
    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")//autoriza a entrar al usuario cuyo roles ROLE_USUARIO_REGISTRADO
    @GetMapping("/inicio")
    public String inicio(){
        return "inicio";
    }
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, @RequestParam(required = false) String logout ,ModelMap modelo) {//@RequestParam(required=false) el parametro puede venir o no
        if (error != null) {
            modelo.put("error", "El usuario o contraseña es incorrecta");
        }
        if(logout!=null){
            modelo.put("logout", "Has salido correctamente");
        }
        return "login.html";
    }

    @GetMapping("/registro")
    public String registro(ModelMap modelo) {
        List<Zona> zonas = zonaRepositorio.findAll();
        modelo.put("zonas", zonas);
        return "registro";
    }

    @PostMapping("/registrar")
    public String registro(ModelMap modelo, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String mail, @RequestParam MultipartFile archivo, @RequestParam String clave1, @RequestParam String clave2, @RequestParam String idZona) {

        try {
            usuarioServicio.registrar(archivo, nombre, clave1, mail, apellido, clave2, idZona);
        } catch (ErroresServicios ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("clave1", clave1);
            modelo.put("clave2", clave2);
            modelo.put("mail", mail);
            modelo.put("archivo", archivo);
            Zona zonaCargada = zonaRepositorio.getOne(idZona);
            modelo.put("zonaCargada", zonaCargada);
            List<Zona> zonas = zonaRepositorio.findAll();
            modelo.put("zonas", zonas);
            Logger.getLogger(PortalControlador.class.getName()).log(Level.SEVERE, null, ex);
            return "registro.html";
        }
        modelo.put("titulo", "BIENVENIDO A TINDER DE MASCOTA");
        modelo.put("descripcion", "EL USUARIO FUE REGISTRADO CORRECTAMENTE");
        return "exito.html";
    }
   
}
