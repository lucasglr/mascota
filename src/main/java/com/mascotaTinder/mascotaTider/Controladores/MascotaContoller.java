/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mascotaTinder.mascotaTider.Controladores;

import com.mascotaTinder.mascotaTider.Entidades.Mascota;
import com.mascotaTinder.mascotaTider.Entidades.Usuario;
import com.mascotaTinder.mascotaTider.Entidades.Zona;
import com.mascotaTinder.mascotaTider.Enum.Sexo;
import com.mascotaTinder.mascotaTider.Enum.Tipo;
import com.mascotaTinder.mascotaTider.Errores.ErroresServicios;
import com.mascotaTinder.mascotaTider.Servicios.FotoServicio;
import com.mascotaTinder.mascotaTider.Servicios.MascotaServicio;
import com.mascotaTinder.mascotaTider.Servicios.UsuarioServicio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@RequestMapping("/mascota")
public class MascotaContoller {

    @Autowired
    private MascotaServicio mascotaServicio;

    @Autowired
    private FotoServicio fotoServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/editar-perfil")
    public String editarPerfil(HttpSession session, @RequestParam(required = false) String id, ModelMap modelo, @RequestParam(required = false) String accion) {
        if (accion == null) {
            accion = "Crear";
        }

        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null) {
            return "redirect:/inicio";
        }
        Mascota mascota = new Mascota();
        if (id != null && !id.isEmpty()) {
            try {
                mascota = mascotaServicio.buscarMascotaPorId(id);
            } catch (ErroresServicios ex) {
                Logger.getLogger(MascotaContoller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        modelo.put("accion", accion);
        modelo.put("perfil", mascota);
        modelo.put("sexos", Sexo.values());
        modelo.put("tipos", Tipo.values());
        return "mascota";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @PostMapping("/actualizar-perfil")
    public String actualizarPerfil(ModelMap modelo, HttpSession session, @RequestParam String id, @RequestParam Sexo sexo, @RequestParam Tipo tipo, MultipartFile archivo, @RequestParam String nombre) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        if (usuario == null) {
            return "redirect:/inicio";
        }
        try {
            String idUsuario = usuario.getId();
            if (id == null || id.isEmpty()) {
                mascotaServicio.agregar(archivo, nombre, idUsuario, sexo, tipo);
            } else {
                Mascota mascota = mascotaServicio.buscarMascotaPorId(id);
                mascotaServicio.modificar(archivo, id, nombre, idUsuario, sexo, tipo);

            }

        } catch (ErroresServicios e) {
            Mascota mascota = new Mascota();
            modelo.put("accion", "Actualizar");
            modelo.put("sexos", Sexo.values());
            modelo.put("tipos", Tipo.values());
            modelo.put("perfil", mascota);
            modelo.put("error", e.getMessage());
            return "mascota";
        }
        return "redirect:/inicio";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/mis-mascotas")
    public String misMascota(HttpSession session, ModelMap modelo) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        if (usuario == null) {
            return "redirect:/login";
        }
        try {
            List<Mascota> mascotas = mascotaServicio.buscarMascotaPorUsuario(usuario.getId());
            modelo.put("mascotas", mascotas);
            return "mis-mascotas.html";
        } catch (ErroresServicios ex) {
            Logger.getLogger(MascotaContoller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "redirect:/inicio";

    }

    @PostMapping("/eliminar-perfil")
    public String eliminar(HttpSession session, @RequestParam String id) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        try {
            mascotaServicio.eliminar(id, usuario.getId());        
        } catch (ErroresServicios ex) {
            Logger.getLogger(MascotaContoller.class.getName()).log(Level.SEVERE, null, ex);     
        }
        return "redirect:/mascota/mis-mascotas";
    }

}
