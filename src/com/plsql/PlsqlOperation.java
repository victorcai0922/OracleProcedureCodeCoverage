/**
 * 
 */
package com.plsql;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.SQLExec;
import org.apache.tools.ant.types.EnumeratedAttribute;

import com.util.FuncXmlTransfer;
import com.utils.FileUtils;

/**
 * @author caiql
 * 
 */
public class PlsqlOperation {

	private Connection con;

	private Statement statement;

	public String statisticSql;

	public Map<String, String> code_coverage_table;

	public Map<String, List<String>> pk_src_table;

	public Map<String, List<String>> pk_src_code_table;

	public Map<String, List<String>> pk_line_cov_table;

	public Map<String, List<String>> pk_line_time_table;

	public Map<String, String> pk_line_table;

	public Map<String, String> pk_text_table;

	public Map<String, String> source_code;

	public Map<String, String> line_time;

	public Map<String, String> pk_line_cov;

	public Map<String, String> pk_line_time;

	public int sumCovered = 0;

	public int sumLineNum;

	public Set<String> packageName;

	public String jdbcurl;

	public String username;

	public String password;

	public String schema;
	
	private Map<String,Map<String,String>> lineTotalOccur;

	// private List<StaticsBean> staticList;

	// private Map<String,List<SourceCodeBean>> sourceCodeMap;

	public Map<String, Map<String, String>> getLineTotalOccur() {
		return lineTotalOccur;
	}

	public void setLineTotalOccur(Map<String, Map<String, String>> lineTotalOccur) {
		this.lineTotalOccur = lineTotalOccur;
	}

	/***
	 */
	public void initialize() {
		Map<String, String> datasource = FileUtils
				.readDataSourceFromPropertiesFile();
		statisticSql = datasource.get("statistic");
		jdbcurl = datasource.get("url");
		username = datasource.get("username");
		password = datasource.get("password");
		schema = datasource.get("schema");
	}

	/**
	 * 
	 * @param ds
	 * @return
	 */
	public boolean connect() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			con = DriverManager.getConnection(jdbcurl, username, password);
			statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			con.setAutoCommit(false);
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		if (statement != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @return
	 */
	public boolean execute() {
		// String sql = String.format(statisticSql, runid);
		System.out.println("start execute:" + new Date());
		if (statement != null) {
			try {
				ResultSet rs = statement.executeQuery(statisticSql);
				sumLineNum = 0;
				code_coverage_table = new LinkedHashMap<String, String>();
				pk_line_cov = new LinkedHashMap<String, String>();
				source_code = new LinkedHashMap<String, String>();
				pk_line_table = new LinkedHashMap<String, String>();
				pk_text_table = new LinkedHashMap<String, String>();
				pk_line_time = new LinkedHashMap<String, String>();
				System.out.println("----------Start read records-----------");
				while (!rs.isLast()) {
					rs.next();
					String owner = rs.getString("OWNER");
					String name = rs.getString("NAME");
					String unit = owner + "." + name;
					String line = rs.getString("LINE");
					String totalOccur = rs.getString("TOTAL_OCCUR");
					String totalTime = rs.getString("TOTAL_TIME");
					// StringBuffer Covered = new
					// StringBuffer(rs.getString("COVERED"));
					StringBuffer Text = new StringBuffer(rs.getString("TEXT"));
					// String Time = rs.getString("Avg Time (sec)");
					// if (Covered != null && Covered.equals("C")) {
					// pk_line_cov.put(rs.getString("LINE"),
					// Covered.toString());
					// sumCovered++;
					// }
					// String Covered = "0";
					// if (totalOccur!=null) {
					// Covered = "C";
					// pk_line_cov.put(line, Covered.toString());
					// sumCovered++;
					// }
					if (Text.toString() != null
							&& !Text.toString().equals("\n")) {
						if ((Text.toString().trim().length() != 0)) {
							code_coverage_table.put(line, totalOccur.trim());
							source_code.put(line, Text.toString());
							pk_line_table.put(line, unit);
							pk_text_table.put(Text.toString(), unit);
							pk_line_time.put(line, totalTime);
							sumLineNum++;
						}
					}

				}
				// System.out.println("code_coverage_table: ".toUpperCase().+code_coverage_table);
				System.out.println("--------End read records---------");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
		}
		System.out.println("end execute: " + new Date());
		return true;
	}

	/**
	 */
	@Deprecated
	public void createPackageSrcMapping() {
		pk_src_table = new LinkedHashMap<String, List<String>>();
		pk_src_code_table = new LinkedHashMap<String, List<String>>();
		pk_line_cov_table = new LinkedHashMap<String, List<String>>();
		pk_line_time_table = new LinkedHashMap<String, List<String>>();
		Set<Entry<String, String>> entryset = pk_line_table.entrySet();
		Iterator<Entry<String, String>> iter = entryset.iterator();
		Entry<String, String> en1 = iter.next();
		List<String> text = null;
		List<String> sourcecode = null;
		List<String> covered = null;
		List<String> time = null;
		while (iter.hasNext()) {
			text = new ArrayList<String>();
			sourcecode = new ArrayList<String>();
			covered = new ArrayList<String>();
			time = new ArrayList<String>();
			String key1 = en1.getKey();
			String value1 = en1.getValue();
			System.out.println("package name: " + value1);
			text.add(key1);
			sourcecode.add(source_code.get(key1));
			covered.add(code_coverage_table.get(key1));
			time.add(pk_line_time.get(key1));
			Entry<String, String> en2 = null;
			Entry<String, String> temp = null;
			String key2 = null;
			while (iter.hasNext()) {
				en2 = iter.next();
				if (en1.getValue().equals(en2.getValue())) {
					key2 = en2.getKey();
					text.add(key2);
					sourcecode.add(source_code.get(key2));
					covered.add(code_coverage_table.get(key2));
					time.add(pk_line_time.get(key2));
					en1 = en2;
				} else {
					temp = en1;
					en1 = en2;
					break;
				}
			}
			pk_src_table.put(value1, text);
			pk_src_code_table.put(value1, sourcecode);
			pk_line_cov_table.put(value1, covered);
			pk_line_time_table.put(value1, time);
			if (temp != null) {
				if (!iter.hasNext() && (temp.getValue() != en2.getValue())) {
					List<String> text1 = new ArrayList<String>();
					List<String> sourcecode1 = new ArrayList<String>();
					List<String> covered1 = new ArrayList<String>();
					List<String> time1 = new ArrayList<String>();
					String key = en2.getKey();
					String value2 = en2.getValue();
					text1.add(key);
					sourcecode1.add(source_code.get(key));
					covered1.add(code_coverage_table.get(key));
					time1.add(pk_line_time.get(key));
					pk_src_table.put(value2, text1);
					pk_src_code_table.put(value2, sourcecode1);
					pk_line_cov_table.put(value2, covered1);
					pk_line_time_table.put(value2, time1);
				}
			}
		}

	}

	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public boolean close() throws SQLException {
		if (statement != null) {
			statement.close();
		}
		if (con != null) {
			con.close();
		}
		return true;
	}

	/**
	 * 
	 * @throws SQLException
	 */
	public void start(String filePath, String funcId) throws SQLException {
		FuncXmlTransfer.readFuncXml(filePath, funcId);
		String funcName = FuncXmlTransfer.spPackage;
		String fn = funcName.substring(funcName.indexOf("SP"),
				funcName.length());
		String sp = "{call dbms_profiler.start_profiler(?)}";
		CallableStatement proc = con.prepareCall(sp);
		String str = String.format(
				"'%s:'||TO_CHAR(SYSDATE,'YYYY-MM-DD HH:MI:SS'", fn);
		proc.setString(1, str);
		proc.execute();
	}
	
	public void startProfiler(){
		String sp = "{call dbms_profiler.start_profiler('startprofiler')}";
	}

	/**
	 * 
	 * @throws SQLException
	 */
	public void stop() throws SQLException {
		String sp = "{call dbms_profiler.stop_profiler}";
		CallableStatement proc = con.prepareCall(sp);
		proc.execute();
	}

	/**
	 */
	public void create() {
		SQLExec exec = new SQLExec();
		exec.setDriver("oracle.jdbc.driver.OracleDriver");
		exec.setUrl(jdbcurl);
		exec.setUserid(username);
		exec.setPassword(password);
		// this.new File("createtab.sql")
		exec.setSrc(new File("createtab.sql"));
		exec.setOnerror((SQLExec.OnError) (EnumeratedAttribute.getInstance(
				SQLExec.OnError.class, "abort")));
		exec.setOutput(new File("createtab.out"));
		exec.setProject(new Project());
		try {
			exec.execute();
		} catch (Exception e) {
			String errorMsg = e.getMessage();
			System.out.println(errorMsg);
		}
	}

	/**
	 * plsql_profiler_units�� plsql_profiler_runs
	 * 
	 * @param runId
	 * @return
	 * @throws SQLException
	 */
	public void delete() throws SQLException {
		con.setAutoCommit(false);
		String sql1 = String.format("delete  from plsql_profiler_data");
		String sql2 = String.format("delete  from plsql_profiler_units");
		String sql3 = String.format("delete  from plsql_profiler_runs");
		statement.addBatch(sql1);
		statement.addBatch(sql2);
		statement.addBatch(sql3);
		int[] count = statement.executeBatch();
		System.out.println(count.length);
		con.commit();
	}

	/**
	 * 
	 * @throws SQLException
	 */
	public void drop() throws SQLException {
		String sql1 = "drop table plsql_profiler_data cascade constraints";
		String sql2 = "drop table plsql_profiler_units cascade constraints";
		String sql3 = "drop table plsql_profiler_runs cascade constraints";
		String sql4 = "drop sequence plsql_profiler_runnumber";
		statement.addBatch(sql1);
		statement.addBatch(sql2);
		statement.addBatch(sql3);
		statement.addBatch(sql4);
		statement.executeBatch();
	}

	/***
	 * 获取覆盖率统计信息
	 */
	public Map<String,Map<String,String>> getStaticsData() {
		String sql = "select u.unit_owner,u.unit_name, u.unit_type, d.line#,d.total_occur, d.total_time/1000000 total_time from plsql_profiler_data d, plsql_profiler_units u where u.runid = d.runid and u.unit_number = d.unit_number and u.unit_type in ('PROCEDURE','PACKAGE BODY')and u.unit_owner not in ('SYSTEM','ULOG','<anonymous>')";
		Map<String,String> lineTime = new LinkedHashMap<String, String>();
		Map<String,String> lineOccur = new LinkedHashMap<String, String>();
		Map<String,Map<String,String>> lineOccurMap = new LinkedHashMap<String, Map<String,String>>();
		Map<String,Map<String,String>> lineTimeMap = new LinkedHashMap<String, Map<String,String>>();
		try {
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				String owner = rs.getString("UNIT_OWNER");
				String name = rs.getString("UNIT_NAME");
				String type = rs.getString("UNIT_TYPE");
				String line = rs.getString("LINE#");
				String totalOccur = rs.getString("total_occur");
				String totalTime = rs.getString("total_time");
				if(totalOccur == null){
					totalOccur = "0";
				}
				if(totalTime == null){
					totalTime = "0";
				}
				lineTime.put(line, totalTime);
				lineOccur.put(line, totalOccur);
				
				lineTimeMap.put(owner+"."+name, lineTime);
				lineOccurMap.put(owner+"."+name, lineOccur);
				setLineTotalOccur(lineOccurMap);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}

		return lineTimeMap;

	}

	public Map<String, String> getSourceCode(String owner, String name) {
		String sql = String
				.format("select s.line,s.TEXT from all_source s where s.OWNER='%s' and s.name ='%s' and s.TYPE  in ('PROCEDURE','PACKAGE BODY')",
						owner, name);
		Map<String, String> sourceCode = new LinkedHashMap<String, String>();
		try {
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				//源代码中剔除空行
				String text = rs.getString("TEXT");
				String line = rs.getString("LINE");
				if (text!=null&& text.trim() != null && !text.equals("")){
					sourceCode.put(line, text.trim());
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return sourceCode;
	}
	
//	public String getCoveredSourceCode(String owner,String name,String line)

	/***
	 * 
	 * @return
	 */
	public boolean executeSp() {
		return true;
	}

	public void main(String[] args) {
		// PlsqlOperation.initialize();
		// PlsqlOperation.create();
	}
}
