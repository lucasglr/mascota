package com.mascotaTinder.mascotaTider.Servicios;

import com.mascotaTinder.mascotaTider.Entidades.Mascota;
import com.mascotaTinder.mascotaTider.Entidades.Voto;
import com.mascotaTinder.mascotaTider.Errores.ErroresServicios;
import com.mascotaTinder.mascotaTider.Repositorios.MascotaRepositorio;
import com.mascotaTinder.mascotaTider.Repositorios.VotoRepositorio;
import java.util.Date;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VotoServicio {

    @Autowired
    private MascotaRepositorio mascotaRepositorio;

    @Autowired
    private VotoRepositorio votoRepositorio;
    
    @Autowired 
    private NotificacionServicio notificacionServicio;
    
    @Transactional
    public void votar(String idUsuario, String idMascota, String idMascota2) throws ErroresServicios {

        Voto voto = new Voto();
        voto.setFecha(new Date());

        //controlar que no se auto vote la mascota
        if (idMascota.equals(idMascota2)) {
            throw new ErroresServicios("No puede votarse a si mismo");
        }

        Optional<Mascota> respuesta = mascotaRepositorio.findById(idMascota);
        if (respuesta.isPresent()) {
            Mascota mascota1 = respuesta.get();
            if (mascota1.getUsuario().getId().equals(idUsuario)) {
                voto.setMascota1(mascota1);
            } else {
                throw new ErroresServicios("El usuario no corresponde a esa mascota para votar");
            }
        } else {
            throw new ErroresServicios("no exite mascota con ese Id para votar");
        }
        Optional<Mascota> respuesta2 = mascotaRepositorio.findById(idMascota2);
        if (respuesta2.isPresent()) {
            Mascota mascota2 = respuesta2.get();
            voto.setMascota2(mascota2);
            notificacionServicio.enviarMail("TU MASCOTA FUE VOTADA ",mascota2.getUsuario().getEmail(), "TINDER DE MASCOTA");
        } else {
            throw new ErroresServicios("No existe la mascota2 para votar");
        }

    }
    @Transactional
    public void responder(String idVoto, String idUsuario) throws ErroresServicios {
        Optional<Voto> repuesta = votoRepositorio.findById(idVoto);
        if (repuesta.isPresent()) {
            Voto voto = repuesta.get();
            voto.setRespuesta(new Date());
            if (voto.getMascota2().getId().equals(idUsuario)) {
                votoRepositorio.save(voto);
                notificacionServicio.enviarMail("TU VOTO FUE CORRESPONDIDO ",voto.getMascota1().getUsuario().getEmail(), "TINDER DE MASCOTA");
            } else {
                throw new ErroresServicios("El usuario no pertenece a la mascota para responde voto");
            }

        } else {
            throw new ErroresServicios("No existe el voto solitario");
        }

    }

}
