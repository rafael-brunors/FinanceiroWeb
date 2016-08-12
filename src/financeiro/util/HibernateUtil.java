package financeiro.util;

import org.hibernate.SessionFactory;
// PAckage para quando trabalhamos com Annotations
import org.hibernate.cfg.AnnotationConfiguration;
// Package para quando trabalhamos com Hibernate
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	
	private static final SessionFactory sessionFactory = buildSessionFactory();
	
	// para n�o exibir a warning
	@SuppressWarnings("deprecation")
	private static SessionFactory buildSessionFactory() {
		try {
			// Objeto para quando trabalhamos com XML e Hibernate
			// Configuration cfg = new Configuration();
			
			// Objeto para quando trabalhamos com Annotations - colocar os @ (annotations) na classe que possui as anota��es
			AnnotationConfiguration cfg = new AnnotationConfiguration();
			
			cfg.configure("hibernate.cfg.xml");
			return cfg.buildSessionFactory();
		} catch (Throwable e) {
			System.out.println("Cria��o inicial do objeto SessionFactory falhou. Erro: " + e);
			throw new ExceptionInInitializerError(e);
		}
	}
	
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}
