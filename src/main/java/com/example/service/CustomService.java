package com.example.service;

import com.example.domain.association.Address;
import com.example.domain.association.Employee;
import com.example.domain.association.Project;
import com.example.domain.inheritence.perhierarchy.HigherSecondaryStudent;
import com.example.domain.inheritence.perhierarchy.SecondaryStudent;
import com.example.domain.inheritence.perhierarchy.Student;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.hibernate.sql.JoinType;
import org.hibernate.type.LongType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by bijoy on 17/8/16.
 */
@Service
@Transactional
public class CustomService {
    private Logger logger = LoggerFactory.getLogger(CustomService.class);
    @Autowired
    private SessionFactory sessionFactory;

    @PostConstruct
    public void init() {
        testNaturalId();
    }
    private void testMerge() {
        Session session = sessionFactory.openSession();
        Employee employee1;
        Employee employee2;
        Employee employee3;
        int i = 0;
        String name = (++i) + " : " + UUID.randomUUID();
        logger.warn("********UPDATE -> " + name);
        session.beginTransaction();
        employee1 = (Employee) session.get(Employee.class, 12l);
        employee1.setName(name);
        session.getTransaction().commit();
        session.close();
        session = sessionFactory.openSession();
        name = (++i) + " : " + UUID.randomUUID();
        logger.warn("*******UPDATE -> " + name);
        session.beginTransaction();
        employee2 = (Employee) session.get(Employee.class, 12l);
        logger.warn("*******" + employee2.getAddress().toString());
        employee2.getAddress().setCity("HEllo cITY");
        employee2.setName(name);
        session.merge(employee1);
        session.getTransaction().commit();
        session = sessionFactory.openSession();
        session.beginTransaction();
        employee3 = (Employee) session.get(Employee.class, 12l);
        logger.warn("*******"+employee3.toString());
        logger.warn("*******"+employee3.getAddress().toString());
        session.getTransaction().commit();

    }
    private void testNaturalId() {
        Runnable runnable1 = () -> {
            while (true) {
                Session session = sessionFactory.openSession();
                session.beginTransaction();
                Address address = (Address) session.byNaturalId(Address.class)
                        .using("pincode", 700051l)
                        .using("city", "City 1")
                        .load();
                logger.warn("fetching results..........");
                logger.warn(address.toString());
                session.close();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable runnable2 = () -> {
            while (true) {
                Session session = sessionFactory.openSession();
                session.beginTransaction();
                Address address = (Address) session.createCriteria(Address.class)
                        .add(Restrictions.naturalId()
                                .set("pincode", 700051l)
                                .set("city", "City 1"))
                        .setFirstResult(0)
                        .setMaxResults(2)
                        .setCacheable(true)
                        .setCacheRegion("address_by_natural_id")
                        .uniqueResult();
                logger.warn("fetching results..........");
                logger.warn(address.toString());
                session.close();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable1).start();
    }

    private void testQueryCache() {
        new Thread(
                new Runnable() {
                    Session session = null;
                    List<Address> addresses = null;

                    @Override
                    public void run() {
                        while (true) {
                            session = sessionFactory.openSession();
                            session.beginTransaction();
                            addresses = session.createSQLQuery("SELECT * from address where city like :name")
                                    .addEntity(Address.class)
                                    .setParameter("name", "City%")
                                    .setFirstResult(0)
                                    .setMaxResults(2)
                                    .setCacheable(true)
                                    .setCacheRegion("address_by_city")
                                    .list();
                            for (Address address : addresses) {
                                logger.warn("fetching results..........");
                                logger.warn(address.toString());
                            }
                            session.close();
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        ).start();
    }

    private void testL2C() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                testPersistency();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                testCaching();
            }
        }).start();
    }

    private void testCaching() {
        Session session = null;
        Employee lead = null;

        while (true) {
            session = sessionFactory.openSession();
            session.beginTransaction();
            lead = (Employee) session.get(Employee.class, new Long(12));
            logger.warn(lead.getProjects().toString());
            session.close();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void testSqlQueries() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        List<Employee> employees = session.createSQLQuery("SELECT e.employee_id,e.lead_id,e.version, e.project, e.employee_name,a.employee_fk, a.city, a.state from employee e inner join address a on e.employee_id = a.employee_fk")
                .addEntity("e", Employee.class)
                .addEntity("a", Address.class)
                .setFirstResult(0)
                .setMaxResults(5)
                .list();
        for (Employee employee : employees) {
            logger.warn("employee => " + employee.getId());
            logger.warn(employee.toString());
            logger.warn(employee.getAddress().toString());
        }

    }

    private void testDetachedCriteria() {
        DetachedCriteria criteria = DetachedCriteria.forClass(Employee.class);
        criteria.createAlias("address", "addr", JoinType.LEFT_OUTER_JOIN);
        criteria.setProjection(
                Projections.countDistinct("addr.city")
        );
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        logger.warn("Row Count -> " + criteria.getExecutableCriteria(session).uniqueResult());
    }

    private void testCriteriaProjections() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Criteria criteria = session.createCriteria(Employee.class)
                .createAlias("address", "addr");
        criteria.setProjection(
                Projections.projectionList()
                        .add(Projections.max("version").as("max_version"))
                        .add(Projections.rowCount(), "row_count")
                        .add(Projections.groupProperty("version"), "grp_version")
                        .add(Projections.groupProperty("id"), "emp_id")
                        .add(Projections.property("addr.city"), "city")
        );
        List<Object[]> list = criteria.list();
        for (Object[] o : list) {
            logger.warn(o[0] + " : " + o[1] + " : " + o[2] + " : " + o[3] + " : " + o[4]);
        }

    }

    private void testHQLCOllection() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
//        String query = "from Employee e where e.projects.size > 0";
        String query = "select e from Employee e inner join e.projects proj  where proj.projectId.id = 6 ";
        List<Employee> employees = session.createQuery(query)
                .setFirstResult(0)
                .setMaxResults(10)
                .list();
        for (Employee employee : employees) {
            logger.info(employee.toString());
        }

    }

    private void testCriteria() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Criteria criteria = session.createCriteria(Employee.class, "emp")
                .setFetchMode("emp.address", FetchMode.JOIN)
                .createAlias("emp.address", "addr", JoinType.LEFT_OUTER_JOIN)
                .setFetchMode("emp.project", FetchMode.JOIN)
                .createAlias("emp.project", "proj", JoinType.LEFT_OUTER_JOIN)
                .setFetchMode("emp.projects", FetchMode.JOIN)
                .createAlias("emp.projects", "projs", JoinType.LEFT_OUTER_JOIN)
                .setFetchMode("emp.lead", FetchMode.SELECT)
                .setFirstResult(0)
                .setMaxResults(20);
        /*List<Employee> employees = criteria.add(
                Restrictions.disjunction(
                        Restrictions.conjunction(
                                Restrictions.ilike("emp.name", "Bijoy%"),
                                Restrictions.disjunction()
                                        .add(Restrictions.isNotNull("emp.lead"))
                                        .add(Restrictions.in("addr.pincode", new Long[]{70011l, 70012l, 70013l}))
                        ),
                        Restrictions.eq("proj.description", "Project Description 6")
                )
        )
                .addOrder(Order.desc("addr.pincode"))
                .addOrder(Order.asc("emp.name"))
                .list();*/
        Property empName = Property.forName("emp.name");
        Property empLead = Property.forName("emp.lead");
        Property pincode = Property.forName("addr.pincode");
        Property projectDescription = Property.forName("proj.description");
        Property projects = Property.forName("projs.description");
        List<Employee> employees = criteria.add(
                Restrictions.or(
                        Restrictions.and(
                                empName.like("Bijoy%"),
                                Restrictions.or(
                                        empLead.isNotNull(),
                                        pincode.in(new Long[]{70011l, 70012l, 70013l})
                                )
                        ),
                        projectDescription.eq("Project Description 6"),
                        projects.eq("Project Description 6")
                )
        )
                .addOrder(pincode.desc())
                .addOrder(empName.asc())
                .list();
        for (Employee employee : employees) {
            logger.warn(employee.toString());
        }
    }

    private void testIdClass() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery("from Project p where p.projectId.id IN (52000,53000,54000,55000)");
        List<Project> projects = query.list();
        for (Project project : projects) {
            logger.info(project.toString());
        }
    }

    private void testPolyformicClass() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery("from java.lang.Object o");
        for (Object o : query.list()) {
            logger.info(o.toString());
        }
    }

    private void testHQLProjections() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
//        Query query = session.createQuery("select new list(e.id, e.name) from Employee e");
//        Query query = session.createQuery("select new Employee(e.name) from Employee e");
        Query query = session.createQuery("select new map(e.id as empid, e.name as nm) from Employee e");
        query.setFirstResult(0);
        query.setMaxResults(10);
        /*List<List<Object>> lists = query.list();
        for(List<Object> list : lists){
            logger.info("ID   -> "+list.get(0));
            logger.info("NAME -> "+list.get(1));
        }*/
        /*List<Employee> employees = query.list();
        for(Employee employee : employees){
            logger.info("NAME -> "+employee.getName());
        }*/
        List<Map<String, Object>> lists = query.list();
        for (Map<String, Object> map : lists) {
            logger.info("ID   -> " + map.get("empid"));
            logger.info("NAME -> " + map.get("nm"));
        }
    }

    private void testOrphanRemoval() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Employee employee = (Employee) session.load(Employee.class, 53l);
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        session.delete(employee);
        session.getTransaction().commit();
    }

    private void testLockPersistency() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Employee employee1 = (Employee) session.get(Employee.class, 53l);
        employee1.setName("NAME 11");
        session.getTransaction().commit();
        session.close();
        session = sessionFactory.openSession();
        session.beginTransaction();
        session.lock(employee1, LockMode.NONE);
        session.close();
    }

    private void testPersistency() {
        Session session = sessionFactory.openSession();
        Employee employee1;
        Employee employee2;
        int i = 0;
        String name = "";
        while (i < 5) {
            name = (++i) + " : " + UUID.randomUUID();
            logger.warn("UPDATE -> " + name);
            session.beginTransaction();
            employee1 = (Employee) session.get(Employee.class, 12l);
            employee1.setName(name);
            session.getTransaction().commit();
            session.close();
            session = sessionFactory.openSession();
            session.beginTransaction();
            employee2 = (Employee) session.get(Employee.class, 12l);
            session.merge(employee1);
            session.getTransaction().commit();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void listLeads() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery("select e from Employee e where e.id=:id");
        query.setLong("id", new Long(52));
        Employee lead = (Employee) query.uniqueResult();
        Employee employee = (Employee) session.createFilter(
                lead.getEmployees(),
                "where this.id=:id"
        )
                .setParameter("id", 53l)
                .uniqueResult();
        logger.info(employee.toString());
        session.getTransaction().commit();
    }

    private void testCreateQueryInList() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Query query = session.getNamedQuery("emp.empList");
        query.setParameterList("ids", new Object[]{51l, 53l, 55l}, LongType.INSTANCE);
        List<Object[]> names = query.list();
        for (Object[] objects : names) {
            logger.info("name -> " + objects[1]);
        }
        session.getTransaction().commit();
    }

    private void testCreatQuery() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery("select e.lead from Employee e where e.id=:id");
        query.setLong("id", new Long(51));
        Employee lead = (Employee) query.uniqueResult();
        logger.info(lead.toString());
        session.getTransaction().commit();
    }

    private void testCreateQueryList() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery("select e from Employee e where e.lead = NULL");
        int i = 0;
        List<Employee> leads;
        while (true) {
            logger.info("*******************" + i + "**********************");
            query.setFirstResult(i);
            query.setMaxResults(10);
            leads = query.list();
            if (leads == null || leads.size() == 0) {
                break;
            }
            for (Employee lead : leads) {
                logger.info(lead.toString());
            }
            i += leads.size();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        session.getTransaction().commit();

    }

    private void tablePerClass() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, -2);
        HigherSecondaryStudent higherSecondaryStudent = new HigherSecondaryStudent("Bijoy", cal.getTime(), date);
        SecondaryStudent secondaryStudent = new SecondaryStudent("Bijoy 2", date);
        Student student = new Student("Bijoy 3");
        session.save(higherSecondaryStudent);
        session.save(secondaryStudent);
        session.save(student);
        session.getTransaction().commit();
    }

    private void bootStrapEmployee() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Employee boss = null;
        Project project1 = null;
        Project project2 = null;
        Employee employee = null;
        for (long i = 5000l; i < 5100; i++) {
            boss = new Employee("Boss " + i);
            boss.addAddress(new Address(new Long(70005 + i), "Street " + i, "City " + i, "State " + i));
            project1 = new Project(new Long(i), "Project Title " + i, "Project Description " + i);
            project2 = new Project(new Long((i + 1) * 1000), "Project Title " + ((i + 1) * 1000), "Project Description " + ((i + 1) * 1000));
            boss.addOwningProject(project1);
            boss.addOwningProject(project2);
            employee = new Employee("Bijoy " + i);
            employee.addAddress(new Address(new Long(80005 + i), "Street " + (i + 1), "City " + (i + 1), "State " + (i + 1)));
            employee.addProject(project1);
            employee.addAncestor(boss);
            session.save(employee);
        }
        session.getTransaction().commit();
    }
}
