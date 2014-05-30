package sz.future.dao;

import java.net.UnknownHostException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;



public class MgFutureDao {
	protected static final Log log = LogFactory.getLog(MgFutureDao.class);
	
	private Mongo mongo;
	private DB db;
	private DBCollection collection;
	private static MgFutureDao dao;
	
	private MgFutureDao(){
		try {
			mongo = new MongoClient("localhost", 27017);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		db = mongo.getDB("local");
		collection = db.getCollection("tick");
	}
	
	public static MgFutureDao getInstance(){
		if(dao==null){
			return new MgFutureDao();
		} else {
			return dao;
		}
	}
	
	public void saveMdTick(List<DBObject> data){
		collection.insert(data);
	}

	/**
	 * @return the collection
	 */
	public DBCollection getCollection() {
		return collection;
	}

	/**
	 * @param collection the collection to set
	 */
	public void setCollection(DBCollection collection) {
		this.collection = collection;
	}
	
}
