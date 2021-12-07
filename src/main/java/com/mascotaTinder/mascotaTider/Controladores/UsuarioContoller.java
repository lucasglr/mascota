/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mascotaTinder.mascotaTider.Controladores;

import com.mascotaTinder.mascotaTider.Entidades.Usuario;
import com.mascotaTinder.mascotaTider.Entidades.Zona;
import com.mascotaTinder.mascotaTider.Errores.ErroresServicios;
import com.mascotaTinder.mascotaTider.Repositorios.ZonaRepositorio;
import com.mascotaTinder.mascotaTider.Servicios.UsuarioServicio;
import java.util.List;
import javax.servlet.http.HttpSession;
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
@RequestMapping("/usuario")
public class UsuarioContoller {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private ZonaRepositorio zonaRepositorio;

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/editar-perfil")
    public String editarPerfil(HttpSession session, @RequestParam String id, ModelMap modelo) {

        List<Zona> zonas = zonaRepositorio.findAll();
        modelo.put("zonas", zonas);
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null || !login.getId().equals(id)) {
            return "redirect:/inicio";
        }
        try {

            Usuario usuario = usuarioServicio.buscarUsuarioPorId(id);
            modelo.addAttribute("perfil", usuario);
        } catch (ErroresServicios ex) {
            modelo.addAttribute("error", ex.getMessage());
        }
        return "perfil";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @PostMapping("/actualizar-perfil")
    public String actualizarPerfil(ModelMap modelo, HttpSession session, MultipartFile archivo, @RequestParam String id, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String mail, @RequestParam String clave1, @RequestParam String clave2, @RequestParam String idZona) {
        Usuario usuario = null;
        Usuario login = (Usuario) session.getAttribute("usuariosession");//verificar si el usuario que edita es el mismo que inicio session
        if (login == null || !login.getId().equals(id)) {
            return "redirect:/inicio";
        }
        try {
            usuario = usuarioServicio.buscarUsuarioPorId(id);
             
            usuarioServicio.modificar(archivo, id, nombre, clave1, mail, apellido, clave2, idZona);
            session.setAttribute("usuariosession", usuario);
            return "redirect:/inicio";
        } catch (ErroresServicios ex) {
            List<Zona> zonas = zonaRepositorio.findAll();
            modelo.put("zonas", zonas);
            modelo.put("error", ex.getMessage());
            modelo.addAttribute("perfil", usuario);
            return "perfil";
        }
    }

}
