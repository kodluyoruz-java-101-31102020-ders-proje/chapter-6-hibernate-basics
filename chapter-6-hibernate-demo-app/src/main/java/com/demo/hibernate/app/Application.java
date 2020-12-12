package com.demo.hibernate.app;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import com.demo.hibernate.app.entity.Department;
import com.demo.hibernate.app.entity.Employee;
import com.demo.hibernate.app.entity.Salary;
import com.demo.hibernate.app.entity.SalaryPK;
import com.demo.hibernate.app.session.HibernateSessionFactoryManager;

public class Application {

	public static void main(String[] args) {
		
		// listAllDepartments();
		// listAllEmployees();
		// saveEmployee();
		// updateEmployee();
		deleteEmployee();
	}
	
	public static void deleteEmployee() {
		
		Long empNo = 10058L;
		
		String sql = "DELETE FROM employees WHERE emp_no = :empId";

		SessionFactory factory = HibernateSessionFactoryManager.getSessionFactory();
		Session session = factory.openSession();
		
		NativeQuery<Employee> nativeQuery = session.createNativeQuery(sql, Employee.class);
		nativeQuery.setParameter("empId", empNo);
				
		session.beginTransaction();
		
		nativeQuery.executeUpdate();
		
		session.getTransaction().commit();
		
		session.close();
	}
	
	public static void updateEmployee() {
		
		Long empNo = 10058L;
		
		SessionFactory factory = HibernateSessionFactoryManager.getSessionFactory();
		Session session = factory.openSession();
		
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Employee> criteriaQuery = builder.createQuery(Employee.class);
		
		Root<Employee> from = criteriaQuery.from(Employee.class);
		
		Predicate empNoEquality = builder.equal( from.get("empNo"), empNo );
		Predicate genderEquality = builder.equal( from.get("gender"), "F" );
		Predicate andClause = builder.and(empNoEquality, genderEquality);
		
		criteriaQuery.where(andClause);

		Query<Employee> realQuery = session.createQuery(criteriaQuery);	
		Employee employee = realQuery.getSingleResult();
		
		if(employee != null) {
			
			employee.setName("Yudum");
			employee.setLastName("Ocak");
			
			session.beginTransaction();
			
			session.update(employee);
			
			session.getTransaction().commit();
		}
		
		session.close();
	}
	
	public static void saveEmployee() {
		
		Date birthDate = Date.from( LocalDate.of(1988, 10, 22).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant() );
		Date hireDate = new Date();
		
		Employee newEmp = new Employee();
		newEmp.setEmpNo(10058L);
		newEmp.setName("Yeşim");
		newEmp.setLastName("Çiçek");
		newEmp.setBirthDate(birthDate);
		newEmp.setHireDate(hireDate);
		newEmp.setGender("F");
		
		SalaryPK pk = new SalaryPK();
		pk.setEmpNo(10058L);
		pk.setFromDate(hireDate);
		
		Salary salary = new Salary();
		salary.setId(pk);
		salary.setSalary(1900L);
		salary.setToDate(new Date());
		
		
		newEmp.setSalaries(Arrays.asList(salary));
		
		Session session = HibernateSessionFactoryManager.getSessionFactory().openSession();
		
		session.beginTransaction();
		
		session.saveOrUpdate(newEmp);
		
		session.getTransaction().commit();
		
		session.close();
	}
	
	private static void listAllEmployees() {
		
		SessionFactory sessionFactory = HibernateSessionFactoryManager.getSessionFactory();
		Session session = sessionFactory.openSession();
		
		String hql = "SELECT emp FROM Employee emp";
		
		Query<Employee> query = session.createQuery(hql, Employee.class);
		List<Employee> employeeList = query.getResultList();
		
		for(Employee empEntity : employeeList) {
			
			System.out.println("Çalışan Bilgileri");
			System.out.println(empEntity.getEmpNo());
			System.out.println(empEntity.getName() + " " + empEntity.getLastName());
			
			System.out.println("Maaş Bilgileri");
			// burada getSalaries metodunu çağırdığında SQL sorgusu atacak
			for(Salary s : empEntity.getSalaries()) {
				System.out.println(s.getSalary());
			}
			
			System.out.println("Departman Bilgileri");
			// burada getDepartments metodunu çağırdığında SQL sorgusu atacak
			for(Department d : empEntity.getDepartments()) {
				System.out.println(d.getName());
			}
			
			System.out.println("----------------------");
		}
	}
	
	private static void listAllDepartments() {
		
		SessionFactory sessionFactory = HibernateSessionFactoryManager.getSessionFactory();
		
		Session session = sessionFactory.openSession();
		String hql = "SELECT d FROM Department d";
		Query<Department> query = session.createQuery(hql, Department.class);
		List<Department> departmentList = query.getResultList();
		
		for(Department d : departmentList) {
			System.out.println(d.getName());
		}
		
		session.close();
	}

}
