package com.homework.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.homework.model.entities.EntityBase;

/**
 * Базовый сервис, реализует основные методы CRUD
 * */
@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public abstract class BaseEntityService<TEntity extends EntityBase, TRepository extends JpaRepository<TEntity, Integer>> {

	@Autowired
	private TRepository rep;

	protected TRepository getRepository() {
		return rep;
	}

	@Transactional(rollbackFor = Exception.class)
	public TEntity insert(TEntity entity) {
		return getRepository().save(entity);
	}

	@Transactional(rollbackFor = Exception.class)
	public TEntity update(TEntity entity) {
		return getRepository().save(entity);
	}

	@Transactional(rollbackFor = Exception.class)
	public void delete(int id) throws Exception {
		getRepository().delete(findOne(id));
	}

	@Transactional(rollbackFor = Exception.class)
	public void deleteAll() {
		getRepository().deleteAll();
	}

	public TEntity findOne(Integer id) {
		return getRepository().findOne(id);
	}

	public List<TEntity> findAll() {
		return getRepository().findAll();
	}

}
