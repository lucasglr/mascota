package com.mascotaTinder.mascotaTider.Repositorios;

import com.mascotaTinder.mascotaTider.Entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String>{
    @Query("SELECT u FROM Usuario u WHERE u.email =:email ")
    public Usuario buscarPorMail(@Param("email")String email);
}