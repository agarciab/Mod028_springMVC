package com.vn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vn.entity.Cliente;

/* Bean de Spring que efectua el acceso a datos */
@Repository("clienteRepository")
/* La interficie principal que nos obrece las operaciones b�sicas es CrudRepository, 
 * JPARepository ofrece metodos de paginaci�n y ordenaci�n entre otros adicionalmente */
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

	/* Podemos crear m�todos de acceso a datos simplemente usando la nomenclatura de Spring Data JPQL 
	 * Spring se encargar�, en tiempo de ejecuci�n, de crear un objeto que implemente la interfaz */
	public List<Cliente> findByLastName(String lastname);
	
}
