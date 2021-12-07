
package com.mascotaTinder.mascotaTider.Repositorios;

import com.mascotaTinder.mascotaTider.Entidades.Foto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FotoRepositorio extends JpaRepository<Foto, String>{
    
}
