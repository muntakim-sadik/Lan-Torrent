package Tracker;

import java.util.ArrayList;
import java.util.Properties;
import java.sql.*;

public class Update {
	String preprocess(ArrayList<Range> l) {
		String ret = "";
		for (Range r : l) {
			ret += r.from + "-" + r.to + ", ";
		}
		return ret;
	}

	private static final String dbClassName = "com.mysql.jdbc.Driver";
	private static final String CONNECTION = "jdbc:mysql://127.0.0.1/LanTorrent";

	Update(String ip, int port, String fileName, ArrayList<Range> r)
			throws ClassNotFoundException, SQLException {
		try {
			Class.forName(dbClassName);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Properties properties = new Properties();
		properties.put("user", "root");
		properties.put("password", "s");
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(CONNECTION, properties);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Statement st = connection.createStatement();
		st = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_UPDATABLE);
		ResultSet uprs = st
				.executeQuery("select * from LanTorrent.data where name='"
						+ fileName + "' and ip='" + ip + "' and port='" + port
						+ "'");

		while (uprs.next()) {
			
			String up = preprocess(r);
			//System.out.println(up);
			uprs.updateString("DOWNLOADED", up);
			uprs.updateRow();
		}

	}

	public static void main(String[] args) {
		Query q = null;
		try {
			q = new Query("file.pdf");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int ii=0;
		ArrayList<QueryData> data = q.get();
		for (QueryData qd : data) {
			//System.out.println(ii++);
			//System.out.print(qd.ip + " " + qd.port + " ");
			for (int i = 0; i < qd.segments.size(); i++) {
				qd.segments.get(i).from +=12;
				qd.segments.get(i).to +=12;
				//System.out.println(qd.segments.get(i).from);
				//System.out.println(qd.segments.get(i).to);
			}
			try {
				Update up=new Update(qd.ip,qd.port,"file.pdf",qd.segments);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// System.out.println(qd.segments.size());
			//System.out.println();
		}
		

	}
}
