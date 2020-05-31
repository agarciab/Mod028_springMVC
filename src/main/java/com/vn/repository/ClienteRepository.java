package com.vn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vn.entity.Cliente;

/* Bean de Spring que efectua el acceso a datos */
@Repository("clienteRepository")
/* La interficie principal que nos obrece las operaciones básicas es CrudRepository, 
 * JPARepository ofrece metodos de paginación y ordenación entre otros adicionalmente */
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

	/* Podemos crear métodos de acceso a datos simplemente usando la nomenclatura de Spring Data JPQL 
	 * Spring se encargará, en tiempo de ejecución, de crear un objeto que implemente la interfaz */
	public List<Cliente> findByLastName(String lastname);
	
}
