
package com.mascotaTinder.mascotaTider.Repositorios;

import com.mascotaTinder.mascotaTider.Entidades.Zona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ZonaRepositorio extends JpaRepository<Zona, String>{
    @Query("SELECT z FROM Zona z WHERE z.id=:id")
    public Zona getOne(@Param("id")String id);
}
