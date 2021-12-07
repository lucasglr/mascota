package com.mascotaTinder.mascotaTider.Repositorios;

import com.mascotaTinder.mascotaTider.Entidades.Mascota;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MascotaRepositorio extends JpaRepository<Mascota, String>{
    @Query("SELECT m FROM Mascota m WHERE m.usuario.id =:idUsuario AND m.baja IS NULL")
    public List<Mascota> buscarMascotaPorUsuario(@Param ("idUsuario") String idUsuario);
}
