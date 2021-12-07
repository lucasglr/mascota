package com.mascotaTinder.mascotaTider.Servicios;

import com.mascotaTinder.mascotaTider.Entidades.Foto;
import com.mascotaTinder.mascotaTider.Errores.ErroresServicios;
import com.mascotaTinder.mascotaTider.Repositorios.FotoRepositorio;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FotoServicio {

    @Autowired
    private FotoRepositorio fotoRepositorio;

    @Transactional
    public Foto guardar(MultipartFile archivo) throws ErroresServicios {
        if (archivo != null && !archivo.isEmpty()) {
            try {
                Foto foto = new Foto();
                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getName());
                foto.setContenido(archivo.getBytes());
                fotoRepositorio.save(foto);
                return foto;
            } catch (IOException ex) {
                Logger.getLogger(FotoServicio.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return null;
    }
    @Transactional
    public Foto actualizar(String idFoto, MultipartFile archivo) throws ErroresServicios {
        if (archivo != null && !archivo.isEmpty()) {
            try {
                Foto foto = new Foto();
                if (idFoto != null) {//si el id es nulo , es por que habia una foto anterior y se crea una nueva
                    Optional<Foto> respuesta = fotoRepositorio.findById(idFoto);
                    if (respuesta.isPresent()) {
                         foto = respuesta.get();
                    }
                }
                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getName());
                foto.setContenido(archivo.getBytes());
                fotoRepositorio.save(foto);
                return foto;
            } catch (IOException ex) {
                Logger.getLogger(FotoServicio.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

}
