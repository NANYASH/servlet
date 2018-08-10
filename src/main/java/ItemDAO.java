import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;

import javax.transaction.Transactional;

public class ItemDAO {
    private static SessionFactory sessionFactory;
    private static final String DELETE_ITEM_BY_ID = "DELETE FROM ITEM WHERE ID = ?";


    public Item save(Item item) throws InternalServerError {
        Transaction tr = null;
        try(Session session = createSessionFactory().openSession()) {
            tr = session.getTransaction();
            tr.begin();
            session.save(item);
            tr.commit();
        }catch (HibernateException e) {
            System.err.println("Save is failed");
            System.err.println(e.getMessage());
            if (tr != null)
                tr.rollback();
            throw new InternalServerError(e.getMessage());
        }
        System.out.println("Save is done");
        return item;
    }


    public Item update(Item item) throws InternalServerError, BadRequestException {
        Transaction tr = null;
        try (Session session = createSessionFactory().openSession()){
            if (findById(Item.class,item.getId())!=null){
            tr = session.getTransaction();
            tr.begin();
            session.update(item);
            tr.commit();}
            else throw new BadRequestException("No items");
        }catch (HibernateException e) {
            System.err.println("Update is failed");
            System.err.println(e.getMessage());
            if (tr != null)
                tr.rollback();
            throw new InternalServerError(e.getMessage());
        }
        System.out.println("Update is done");
        return item;
    }


    public void delete(long id) throws InternalServerError, BadRequestException {
        Transaction transaction;
        try( Session session = createSessionFactory().openSession()) {
            NativeQuery query = session.createNativeQuery(DELETE_ITEM_BY_ID);
            if (findById(Item.class,id)!=null) {
                transaction = session.getTransaction();
                query.addEntity(Item.class);
                query.setParameter(1, id);
                transaction.begin();
                query.executeUpdate();
                transaction.commit();
            }else throw new BadRequestException("No items");
        }catch (HibernateException e){
            System.err.println(e.getMessage());
            throw new InternalServerError(e.getMessage());
        }
    }


    public Item findById(Class c, long id) throws InternalServerError, BadRequestException {
        Item item;
        try(Session session = createSessionFactory().openSession()) {
            item = (session.get(Item.class, id));
            if (item!=null)return item;
            throw new BadRequestException("No items with such parameters");
        }catch (HibernateException e) {
            System.err.println(c.getName() + " with such id \"" + id + "\" does not exist.");
            System.err.println(e.getMessage());
            throw new InternalServerError(e.getMessage());
        }
    }

    public static SessionFactory createSessionFactory(){
        if (sessionFactory == null) {
            return new Configuration().configure().buildSessionFactory();}
        return sessionFactory;
    }

}
