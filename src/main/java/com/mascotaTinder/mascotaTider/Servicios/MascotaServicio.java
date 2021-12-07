package com.mascotaTinder.mascotaTider.Servicios;

import com.mascotaTinder.mascotaTider.Entidades.Foto;
import com.mascotaTinder.mascotaTider.Entidades.Mascota;
import com.mascotaTinder.mascotaTider.Entidades.Usuario;
import com.mascotaTinder.mascotaTider.Enum.Sexo;
import com.mascotaTinder.mascotaTider.Enum.Tipo;
import com.mascotaTinder.mascotaTider.Errores.ErroresServicios;
import com.mascotaTinder.mascotaTider.Repositorios.MascotaRepositorio;
import com.mascotaTinder.mascotaTider.Repositorios.UsuarioRepositorio;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.transaction.Transactional;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MascotaServicio {

    @Autowired
    private MascotaRepositorio mascotaRepositorio;

    @Autowired
    private FotoServicio fotoServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Transactional
    public void agregar(MultipartFile archivo, String nombre, String idUsuario, Sexo sexo, Tipo tipo) throws ErroresServicios {
        validar(nombre, sexo);
        Mascota mascota = new Mascota();
        mascota.setNombre(nombre);
        mascota.setAlta(new Date());
        mascota.setSexo(sexo);
        mascota.setTipo(tipo);
        Usuario usuario = usuarioServicio.buscarUsuarioPorId(idUsuario);
        mascota.setUsuario(usuario);
        Foto foto = fotoServicio.guardar(archivo);
        mascota.setFoto(foto);
        mascotaRepositorio.save(mascota);

    }

    @Transactional
    public void modificar(MultipartFile archivo, String id, String nombre, String idUsuario, Sexo sexo, Tipo tipo) throws ErroresServicios {
        validar(nombre, sexo);
        Optional<Mascota> respuesta = mascotaRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Mascota mascota = respuesta.get();
            if (mascota.getUsuario().getId().equals(idUsuario)) {
                mascota.setNombre(nombre);
                mascota.setSexo(sexo);
                mascota.setTipo(tipo);

                String idFoto = null;
                if (mascota.getFoto() != null) {
                    idFoto = mascota.getFoto().getId();
                }
                Foto foto = fotoServicio.actualizar(idFoto, archivo);
                mascota.setFoto(foto);
                mascotaRepositorio.save(mascota);
            } else {
                throw new ErroresServicios("El usuario ingresado no pertenece a la mascota");
            }
        } else {
            throw new ErroresServicios("No existe mascota para modificar");
        }
    }

    @Transactional
    public void eliminar(String id, String idUsuario) throws ErroresServicios {
        Optional<Mascota> respuesta = mascotaRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Mascota mascota = respuesta.get();
            if (mascota.getUsuario().getId().equals(idUsuario)) {
                mascota.setBaja(new Date());
                mascotaRepositorio.save(mascota);
            } else {
                throw new ErroresServicios("El usuario ingresado no pertenece a la mascota");
            }
        } else {
            throw new ErroresServicios("No existe mascota para eliminar");
        }

    }

    @Transactional
    public Mascota buscarMascotaPorId(String id) throws ErroresServicios {
        Optional<Mascota> respuesta = mascotaRepositorio.findById(id);
        if (respuesta.isPresent()) {
            return respuesta.get();
        } else {
            throw new ErroresServicios("La mascota solicitada no existe");
        }

    }

    @Transactional
    public List<Mascota> buscarMascotaPorUsuario(String idUsuario) throws ErroresServicios{
        List<Mascota> mascotas= mascotaRepositorio.buscarMascotaPorUsuario(idUsuario);
        if(mascotas==null ){
            throw new ErroresServicios("El usuario no posee Mascotas asignadas");
        }
        return mascotas;
    }
    public void validar(String nombre, Sexo sexo) throws ErroresServicios {
        if (nombre.isEmpty() || nombre == null) {
            throw new ErroresServicios("Nombre de mascota no debe ser nula");
        }
        if (sexo == null) {
            throw new ErroresServicios("El sexo de la mascota no debe ser nulo");
        }
    }
}
