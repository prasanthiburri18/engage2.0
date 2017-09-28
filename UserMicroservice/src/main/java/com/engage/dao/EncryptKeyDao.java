package com.engage.dao;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.engage.model.EncryptionKey;

@Repository
@Transactional
public class EncryptKeyDao extends AbstractDao{

	
	public EncryptionKey getKeyById(int id){
		
		return getSession().get(EncryptionKey.class, id);
	}
	
public EncryptionKey getFirstKey(){
		
	Criteria crit = getSession().createCriteria(EncryptionKey.class);
	  
	EncryptionKey key = (EncryptionKey) crit.list().get(1);
		return key;
	}
}
