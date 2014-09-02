/**
 * 
 */
package com.plsql;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.bean.SourceCodeBean;
import com.bean.StaticsBean;
import com.report.CodeCoverageReportGenerator;

/**
 * 
 * 
 * @author caiql
 * 
 */
public class Coverage {
	private Logger logger = Logger.getLogger(Coverage.class);

	public Map<String, String> pkcoverage = new Hashtable<String, String>();

	public Map<String, String> pkuncoverage = new Hashtable<String, String>();

	private Map<String, Map<String, String>> pkDetailCoverageInfo;

	private Map<String, Map<String, String>> pkDetailTimeInfo;

	private Map<String, Map<String, String>> pkDetailLineOccurInfo;

	private Map<String, String> indexDetail;

	public Map<String, String> pktotalline = new Hashtable<String, String>();

	public Map<String, String> pkanalyzedline = new Hashtable<String, String>();

	public Map<String, String> pksrcline = new Hashtable<String, String>();

	public List<String> packageList = new ArrayList<String>();

	CodeCoverageReportGenerator reporter = new CodeCoverageReportGenerator();

	private PlsqlOperation plsqlOperation;

	private Map<String, Map<String, String>> sourceCodeMap;

	private int totalLine;
	
	private int coveredLine;

	public int getCoveredLine() {
		return coveredLine;
	}

	public void setCoveredLine(int coveredLine) {
		this.coveredLine = coveredLine;
	}

	public int getTotalLine() {
		return totalLine;
	}

	public void setTotalLine(int totalLine) {
		this.totalLine = totalLine;
	}

	private int analyzedLine;

	public Map<String, String> getIndexDetail() {
		return indexDetail;
	}

	public void setIndexDetail(Map<String, String> indexDetail) {
		this.indexDetail = indexDetail;
	}

	public int getAnalyzedLine() {
		return analyzedLine;
	}

	public void setAnalyzedLine(int analyzedLine) {
		this.analyzedLine = analyzedLine;
	}

	public Coverage() {
		plsqlOperation = new PlsqlOperation();
	}

	/**
	 */
	public void start(String filePath, String funcId) {
		try {
			initialize();
			plsqlOperation.start(filePath, funcId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("" + e.getMessage());
		}
	}
	
	public void statrtProfiler(){
		initialize();
	}

	/**
	 */
	public void stop() {
		try {
			plsqlOperation.stop();
			plsqlOperation.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 */
	public void initialize() {
		plsqlOperation.initialize();
		plsqlOperation.connect();
	}

	/**
	 */
	public void cleanUp() {
		try {
			plsqlOperation.delete();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("" + e.getMessage());
		}
	}

	public void report() {
		System.out.println("start generate static data");
		detailsDataMap();
		try {
			// reporter.report(plsqlOperation, plsqlOperation.schema,
			// pktotalline,
			// pkcoverage, pkuncoverage, pkanalyzedline);
			reporter.report(plsqlOperation.schema, sourceCodeMap, totalLine,
					analyzedLine, coveredLine,pkcoverage, pkuncoverage,
					pkDetailCoverageInfo, pkDetailTimeInfo,pkDetailLineOccurInfo);
			System.out.println("report generate finishing");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/***
	 * 
	 * @param runId
	 */
	public void deleteProfilerdataWithRunid(int runId) {
		try {
			plsqlOperation.delete();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("删除信息错误" + e.getMessage());
		}
	}

	public void quoteConditionString(String sql) {

	}

	/**
	 */
	public void createProfilertables() {
		plsqlOperation.create();
	}

	/**
	 */
	private void createStaticFiles() {
		plsqlOperation.execute();
		plsqlOperation.createPackageSrcMapping();
		Set<Entry<String, List<String>>> entryset = plsqlOperation.pk_src_table
				.entrySet();
		Iterator<Entry<String, List<String>>> iter = entryset.iterator();
		int sumLine = 0;
		float analyzedLine = 0;
		while (iter.hasNext()) {
			Entry<String, List<String>> entry = iter.next();
			List<String> src = entry.getValue();
			float c = 0;
			float cov = 0;
			float uncov = 0;
			int totalline = src.size();
			for (String line : src) {
				String covered = plsqlOperation.code_coverage_table.get(line);
				if (covered.equals("C")) {
					c++;
				}
			}
			cov = c / totalline * 100;
			uncov = 100 - cov;
			String pakname = entry.getKey();
			pkcoverage.put(pakname, String.format("%.2f", cov));
			pkuncoverage.put(pakname, String.format("%.2f", uncov));
			pktotalline.put(pakname, String.valueOf(totalline));
			pkanalyzedline.put(pakname, String.valueOf((int) c));
			packageList.add(pakname);
			sumLine += totalline;
			analyzedLine += c;
		}
		System.out.println("package sum: " + pkcoverage.size());
		System.out.println("total line num: " + sumLine);
		System.out.println("analyzed line num: " + analyzedLine);
		float tcov = analyzedLine / sumLine * 100;
		float tucov = 100 - tcov;
		reporter.setTotalPackage(pkcoverage.size());
		reporter.setTotalLine(sumLine);
		reporter.setTotalAnalyzedLine((int) analyzedLine);
		reporter.setTotalCoverage(String.format("%.2f", tcov));
		reporter.setTotalUncoverage(String.format("%.2f", tucov));
	}

	/**
	 * 詳細數據匹配
	 */
	public void detailsDataMap() {
		Map<String, Map<String,String>> staticsMap = plsqlOperation
				.getStaticsData();
		Iterator<String> unitKeys = staticsMap.keySet().iterator();
		sourceCodeMap = new LinkedHashMap<String, Map<String, String>>();
		pkDetailCoverageInfo = new LinkedHashMap<String, Map<String, String>>();
		pkDetailTimeInfo = new LinkedHashMap<String, Map<String, String>>();
		pkDetailLineOccurInfo = new LinkedHashMap<String, Map<String,String>>();
		indexDetail = new LinkedHashMap<String, String>();
		int totalLineSum = 0;
		int analyzedLineSum = 0;
		int coveredLineSum = 0;
		// get analyzed code details,get every unit name
		while (unitKeys.hasNext()) {
			String unit = unitKeys.next();
			String[] str = unit.split("\\.");
			Map<String,String> unitMapCode = staticsMap.get(unit);
			Map<String,String> unitLineOccur = plsqlOperation.getLineTotalOccur().get(unit);
			Map<String, String> coverage = new LinkedHashMap<String, String>();
			Map<String, String> lineTime = new LinkedHashMap<String, String>();
			Map<String, String> lineOccur = new LinkedHashMap<String, String>();
			String owner = str[0];
			String name = str[1];
			// depend on the unit name,get the source code from all_source table
			Map<String, String> sourceCode4unit = plsqlOperation.getSourceCode(owner, name);
			totalLineSum += sourceCode4unit.size();
			analyzedLineSum += unitMapCode.size();
			// get the details static from every unit
			Iterator<String> keys = sourceCode4unit.keySet().iterator();
			while(keys.hasNext()){
				String line = keys.next();
				String text = sourceCode4unit.get(line);
				String occur = unitLineOccur.get(line);
				if(occur == null){
					occur = "0";
				}
				String covered = "0";
				//判断代码是否被覆盖到，若被覆盖到，判断代码是否为注释行，若为注释行则忽略此代码行
				if (text.startsWith("--")){
					covered = "I";
				}
				if(unitMapCode.containsKey(line)){
					covered = "C";
					}
				//存储覆盖率信息
				coverage.put(text, covered);
				String time = unitMapCode.get(line);
				if(time==null){
					time = "0";
				}
				lineTime.put(text, time);
				lineOccur.put(text, occur);
			}

				/***
				 * calculate the coverage rate of every package
				 */
			    coveredLineSum  += coverage.size();
				double c = coverage.size();
				double cov = 0;
				double uncov = 0;
				cov = c / sourceCode4unit.size() * 100;
				uncov = 100 - cov;
				pkcoverage.put(unit, String.format("%.2f", cov));
				pkuncoverage.put(unit, String.format("%.2f", uncov));
				/***
				 * the detail coveragre source code line
				 */
				pkDetailCoverageInfo.put(unit, coverage);
				pkDetailTimeInfo.put(unit, lineTime);
				pkDetailLineOccurInfo.put(unit, lineOccur);
			

			sourceCodeMap.put(unit, sourceCode4unit);
		}
		// indexDetail.put("Package Sum", arg1)
		System.out.println("Total source code line: " + totalLineSum);
		System.out.println("Analyzed source code line: " + analyzedLineSum);
		System.out.println("Covered source code line: " + coveredLineSum);
		setAnalyzedLine(analyzedLineSum);
		setTotalLine(totalLineSum);
		setCoveredLine(coveredLineSum);

	}

	public void indexReport() {

	}

}
