package com.demo.hibernate.app.session;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateSessionFactoryManager {

	private static SessionFactory sessionFactory = null;
	
	public static SessionFactory getSessionFactory() {
		
		if(sessionFactory == null) {
			
			StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
	        MetadataSources sources = new MetadataSources(registry);
	        Metadata metadata = sources.getMetadataBuilder().build();

	        sessionFactory = metadata.getSessionFactoryBuilder().build();
		}
			
		return sessionFactory;
	}
}
