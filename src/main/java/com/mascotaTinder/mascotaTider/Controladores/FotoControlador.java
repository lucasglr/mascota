package com.mascotaTinder.mascotaTider.Controladores;

import com.mascotaTinder.mascotaTider.Entidades.Mascota;
import com.mascotaTinder.mascotaTider.Entidades.Usuario;
import com.mascotaTinder.mascotaTider.Errores.ErroresServicios;
import com.mascotaTinder.mascotaTider.Servicios.FotoServicio;
import com.mascotaTinder.mascotaTider.Servicios.MascotaServicio;
import com.mascotaTinder.mascotaTider.Servicios.UsuarioServicio;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.header.Header;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/foto")
public class FotoControlador {

    @Autowired
    private FotoServicio fotoServicio;
    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private MascotaServicio mascotaServicio;

    @GetMapping("/usuario/{id}")
    public ResponseEntity<byte[]> fotoFormulario(@PathVariable String id) { //ENVIAR FOTO DE BBDD HASTA LA PAGINA SE DEBE PASAR LA FOTO,HEADER Y EL STATUS DEL HTTP 200
        try {
            Usuario usuario = usuarioServicio.buscarUsuarioPorId(id);
            if (usuario.getFoto() == null) {
                throw new ErroresServicios("EL usuario no posee foto asignada");
            }
            byte[] foto = usuario.getFoto().getContenido();
            HttpHeaders header = new HttpHeaders();//EN LOS ENCABEZADOS HTTP SE LE AVISA AL NAVEGADOR QUE VA SER DE TIPO FOTO
            header.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(foto, header, HttpStatus.OK);
        } catch (ErroresServicios ex) {
            Logger.getLogger(FotoControlador.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
    @GetMapping("/mascota/{id}")
    public ResponseEntity<byte[]> fotoMascota(@PathVariable String id) {
        try {
            Mascota mascota = mascotaServicio.buscarMascotaPorId(id);
            if (mascota.getFoto() == null) {
                throw new ErroresServicios("La mascota no posee foto asignada");
            }
            byte[] foto = mascota.getFoto().getContenido();
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(foto,header,HttpStatus.OK);
        } catch (ErroresServicios ex) {
            Logger.getLogger(FotoControlador.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
