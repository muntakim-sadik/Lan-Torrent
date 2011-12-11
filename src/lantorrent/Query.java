package lantorrent;

import java.util.ArrayList;
import java.util.Properties;
import java.sql.*;

class Range{
    long from,to;
    boolean has;
    Range(long f,long t){
        from=f;
        to=t;
        has = false;
    }
    Range(){
    }
    void setFinished(){
        has = true;
    }
}

class QueryData{
	String ip;
	int port;
	ArrayList<Range> segments;
	QueryData(String i,int p,ArrayList<Range> r){
		ip=i;
		port =p;
		segments=r;
	}
}

public class Query {
	
    String  preprocess(ArrayList<Range> l){
        String ret="";
        for(Range r:l){
            ret+=r.from+"-"+r.to+", ";
        }
        return ret;
    }
    
    ArrayList<Range> postprocess(String s){
        ArrayList<Range> l=new ArrayList<Range>();
        int from=0;
        String ss[]=s.split(",");
        
       for(int i=0;i<ss.length;i++){
            ss[i]=ss[i].trim();
            String sss[]=ss[i].split("-");
            int f=Integer.parseInt(sss[0]);
            int t=Integer.parseInt(sss[1]);
            l.add(new Range(f,t));
            //System.out.println(f+" "+t);
        }
        return l;
    }
	
	private static final String dbClassName = "com.mysql.jdbc.Driver";
	private static final String CONNECTION = "jdbc:mysql://127.0.0.1/LanTorrent";
	ArrayList<QueryData> ret;
	Query(String fileName)throws ClassNotFoundException,SQLException{
		ret=new ArrayList<QueryData>();
		try {
			Class.forName(dbClassName);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Properties properties=new Properties();
		properties.put("user", "root");
		properties.put("password", "s");
		Connection connection=null;
		try {
			 connection=DriverManager.getConnection(CONNECTION, properties);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Statement st=connection.createStatement();
		st=connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
		ResultSet uprs=st.executeQuery("select * from LanTorrent.data where name='"+fileName+"'");
		
	    while(uprs.next()){

	    	String ip=uprs.getString("IP");
	    	//String name=uprs.getString("NAME");
	    	int port=uprs.getInt("PORT");
	    	String downloaded=uprs.getString("DOWNLOADED");	
	    	//System.out.println(ip+"  "+port+" "+name+" "+downloaded);
	    	//ArrayList<Range> r=postprocess(downloaded);
	    	//System.out.println(r.size());
	    	//System.out.println(downloaded);
	    	//postprocess(downloaded);
	    	ret.add(new QueryData(ip,port,postprocess(downloaded)));
	    	
	    }
	}
	
	ArrayList<QueryData> get(){
		return ret;
	}
	
	public static void main(String[] args){
		Query q = null;
		try {
			q=new Query("file.pdf");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<QueryData> data=q.get();
		for(QueryData qd:data){
			System.out.print(qd.ip+" "+qd.port+" ");
			for(int i=0;i<qd.segments.size();i++){
				System.out.print(qd.segments.get(i).from+" "+qd.segments.get(i).to+" ");
			}
			//System.out.println(qd.segments.size());
			System.out.println();
		}
		
	}
}
