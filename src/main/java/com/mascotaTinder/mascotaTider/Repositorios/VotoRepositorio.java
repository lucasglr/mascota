
package com.mascotaTinder.mascotaTider.Repositorios;

import com.mascotaTinder.mascotaTider.Entidades.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotoRepositorio extends JpaRepository<Voto, String>{
    
}
