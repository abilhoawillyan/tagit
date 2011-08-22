package com.luciotbc.tagit.dao.gae;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.luciotbc.tagit.model.Entity;
import com.luciotbc.tagit.model.Evaluation;
import com.luciotbc.tagit.model.User;

import br.com.caelum.vraptor.ioc.Component;

@Component
public class DAO{
	
	//CADA DAO DE UMA SESS�O � ARMAZENADO EM UMA THREAD!
	//XXX REMOVER ISSO E TRABALHAR DE FORMA MAIS SIMPLES
	private static final ThreadLocal<EntityManager> ENTITY_MANAGER_STORE = new ThreadLocal<EntityManager>();

	public static void setEntityManager(EntityManager em) {
		ENTITY_MANAGER_STORE.set(em);
	}
	
	public static void resetEntityManager() {
		setEntityManager(null);
	}
	
	public static EntityManager getEntityManager() {
		return ENTITY_MANAGER_STORE.get();
	}

	public void save(Entity obj) {	
		EntityManager em = getEntityManager();
		em.persist(obj);
		em.flush();
	}

	public void delete(Entity obj) {
		EntityManager em = getEntityManager();
		if( !em.contains(obj) ) {
			obj = em.find(obj.getClass(), obj.getId());
		}
		em.remove(obj);
		em.flush();
	}

	public void update(Entity obj) {
		EntityManager em = getEntityManager();
		em.persist(obj);
		em.flush();
	}

	public Entity load(Entity obj) {
		EntityManager em = getEntityManager();
		obj = em.find(obj.getClass(), obj.getId());
		return obj;
	}
	
	public <T extends Entity> T findById(Class<T> clazz, Long id) {
		EntityManager em = getEntityManager();
		return em.find(clazz, id);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Entity> List<T> findAll(Class<T> clazz) {
		EntityManager em = getEntityManager();
		Query query = em.createQuery("select from " + clazz.getName());
		return query.getResultList();
	}

	public User findUserByEmail(String email) {
		EntityManager em = getEntityManager();              
		Query query = em.createQuery("SELECT FROM " + User.class.getName() + " WHERE email = :email");
		query.setParameter("email", email);
		try {
			return (User) query.getSingleResult();
		} catch(NoResultException e) {
			return null;
		}
	}

	public List<Evaluation> findAllEvaluationsByModerator(Long id) {
		EntityManager em = getEntityManager();
		Query query = em.createQuery("SELECT FROM " + Evaluation.class.getName() + " WHERE moderate = :moderate");
		query.setParameter("moderate", id);
		try {
			return (List<Evaluation>) query.getResultList();
		} catch(NoResultException e) {
			return null;
		}
	}
	
}
