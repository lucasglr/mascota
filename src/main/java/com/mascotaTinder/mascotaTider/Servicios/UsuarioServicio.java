package com.mascotaTinder.mascotaTider.Servicios;

import com.mascotaTinder.mascotaTider.Entidades.Foto;
import com.mascotaTinder.mascotaTider.Entidades.Usuario;
import com.mascotaTinder.mascotaTider.Entidades.Zona;
import com.mascotaTinder.mascotaTider.Errores.ErroresServicios;
import com.mascotaTinder.mascotaTider.Repositorios.FotoRepositorio;
import com.mascotaTinder.mascotaTider.Repositorios.UsuarioRepositorio;
import com.mascotaTinder.mascotaTider.Repositorios.ZonaRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private FotoServicio fotoServicio;
    
     
    @Autowired
    private ZonaRepositorio zonaRepositorio;
    

    @Transactional
    public void registrar(MultipartFile archivo, String nombre, String clave1, String email, String apellido,String clave2,String idZona) throws ErroresServicios {
        Zona zona =zonaRepositorio.getOne(idZona);
        validar(nombre, clave1, email, apellido,clave2,zona);

        Usuario user = new Usuario();
        user.setNombre(nombre);
        user.setApellido(apellido);
        String encriptada = new BCryptPasswordEncoder().encode(clave1);
        user.setClave(encriptada);
        user.setEmail(email);
        user.setAlta(new Date());
        user.setActivo(Boolean.TRUE);
        user.setZona(zona);
        Foto foto = fotoServicio.guardar(archivo);
        user.setFoto(foto);

        usuarioRepositorio.save(user);
        //notificacionServicio.enviarMail("BIENVENIDO AL TINDER DE MASCOTA , SE REGISTRO CORRECTAMENTE", user.getEmail(), "TINDER DE MASCOTA");
    }

    @Transactional
    public void modificar(MultipartFile archivo, String id, String nombre, String clave, String email, String apellido,String clave2,String idZona) throws ErroresServicios {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario user = respuesta.get();
            Zona zona =zonaRepositorio.getOne(idZona);
            validar(nombre, clave, email, apellido,clave2,zona);
            user.setNombre(nombre);
            user.setApellido(apellido);
            String encriptada = new BCryptPasswordEncoder().encode(clave);
            user.setClave(encriptada);
            user.setEmail(email);
            user.setZona(zona);
            String idFoto = null;
            if (user.getFoto() != null) {
                idFoto = user.getFoto().getId();
            }
            Foto foto = fotoServicio.actualizar(idFoto, archivo);
            user.setFoto(foto);
            usuarioRepositorio.save(user);
        } else {
            throw new ErroresServicios("No se encontro usuario con ese ID");
        }
    }

    @Transactional
    public void deshabilitar(String id) {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario user = respuesta.get();
            user.setActivo(Boolean.FALSE);
            user.setBaja(new Date());
            usuarioRepositorio.save(user);
        }
    }
    
    public Usuario buscarUsuarioPorMail(String nombre){
        Usuario usuario = usuarioRepositorio.buscarPorMail(nombre);
        return usuario;
    }

    public Usuario buscarUsuarioPorId(String id) throws ErroresServicios{
        Optional<Usuario> respuesta= usuarioRepositorio.findById(id);
        if(respuesta.isPresent()){
            return respuesta.get();
        }else{
            throw new ErroresServicios("No se encontro usuario registrado");
        }
 
    }
    public void validar(String nombre, String clave, String email, String apellido,String clave2,Zona zona) throws ErroresServicios {
        if (nombre.isEmpty() || nombre == null) {
            throw new ErroresServicios("El nombre no debe ser Nulo o estar vacio");
        }

        if (apellido.isEmpty() || apellido == null) {
            throw new ErroresServicios("El apellido no debe ser Nulo o estar vacio");
        }

        if (email.isEmpty() || email == null) {
            throw new ErroresServicios("El email no debe ser Nulo o estar vacio");
        }
        if (clave.isEmpty() || clave == null || clave.length() < 6) {
            throw new ErroresServicios("La contraseña no debe ser nula y tiene que tener más de 6 caracteres");
        }
        if(!clave.equals(clave2)){
            throw new ErroresServicios("La contraseña deben ser iguales ");
        }
        if(zona==null){
            throw new ErroresServicios("Se debe seleccionar la zona");
        }

    }

    /**
     * Este metodo se llama para cuando nos queremos autentificar al ingresar al
     * login
     *
     * @param mail
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.buscarPorMail(mail);
        if (usuario != null) {

            List<GrantedAuthority> permisos = new ArrayList();

            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_USUARIO_REGISTRADO");
            permisos.add(p1);
            
            //obtener el usuario registrado para mostrar en la vista , una vez que se autentifico
            //usuariosesion tengo guardado el dato del usuario
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);
            
            
            User user = new User(usuario.getEmail(), usuario.getClave(), permisos);
            return user;
        } else {
            return null;
        }
    }
}
